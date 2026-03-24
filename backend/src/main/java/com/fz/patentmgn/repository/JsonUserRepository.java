package com.fz.patentmgn.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fz.patentmgn.exception.DataWriteException;
import com.fz.patentmgn.model.User;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class JsonUserRepository {

    private static final Logger log = LoggerFactory.getLogger(JsonUserRepository.class);

    @Value("${patent.users.file:data/users.json}")
    private String dataFilePath;

    private final ObjectMapper objectMapper;
    private List<User> usersCache;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public JsonUserRepository() {
        this.objectMapper = new ObjectMapper();
        this.usersCache = new ArrayList<>();
    }

    @PostConstruct
    public void init() {
        loadFromFile();
    }

    private void loadFromFile() {
        lock.writeLock().lock();
        try {
            File file = new File(dataFilePath);
            if (file.exists() && file.length() > 0) {
                usersCache = objectMapper.readValue(file, new TypeReference<List<User>>() {
                });
                log.info("Load users from {} with {} rows", dataFilePath, usersCache.size());
            } else {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
                // Create default admin user
                usersCache.add(new User("admin", "123456", "ADMIN"));
                saveToFile();
                log.info("Build default user: admin / 123456 (write to {})", dataFilePath);
            }
        } catch (IOException e) {
            log.error("Failed to load data file", e);
            throw new DataWriteException("Failed to initialize repository file", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void saveToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(dataFilePath), usersCache);
        } catch (IOException e) {
            log.error("Failed to write data file", e);
            throw new DataWriteException("Failed to write changes to file", e);
        }
    }

    public Optional<User> findByUsername(String username) {
        lock.readLock().lock();
        try {
            return usersCache.stream()
                    .filter(u -> u.getUsername().equals(username))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<User> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(usersCache);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void save(User user) {
        lock.writeLock().lock();
        try {
            usersCache.removeIf(u -> u.getUsername().equals(user.getUsername()));
            usersCache.add(user);
            saveToFile();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void deleteByUsername(String username) {
        lock.writeLock().lock();
        try {
            usersCache.removeIf(u -> u.getUsername().equals(username));
            saveToFile();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
