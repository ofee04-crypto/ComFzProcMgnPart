package com.fz.patentmgn.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fz.patentmgn.exception.DataWriteException;
import com.fz.patentmgn.model.PatentCase;
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
public class JsonPatentCaseRepository {

    private static final Logger log = LoggerFactory.getLogger(JsonPatentCaseRepository.class);

    @Value("${patent.data.file:data/patent_cases.json}")
    private String dataFilePath;

    private final ObjectMapper objectMapper;
    private List<PatentCase> casesCache;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public JsonPatentCaseRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.casesCache = new ArrayList<>();
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
                casesCache = objectMapper.readValue(file, new TypeReference<List<PatentCase>>() {
                });
                log.info("Load cases {} from {}s", casesCache.size(), dataFilePath);
            } else {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
                saveToFile();
                log.info("Build new file: {}", dataFilePath);
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
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(dataFilePath), casesCache);
        } catch (IOException e) {
            log.error("Failed to write data file", e);
            throw new DataWriteException("Failed to write changes to file", e);
        }
    }

    public List<PatentCase> findAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(casesCache);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Optional<PatentCase> findById(String eventNo) {
        lock.readLock().lock();
        try {
            return casesCache.stream()
                    .filter(c -> c.getEventNo().equals(eventNo))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void deleteById(String eventNo) {
        lock.writeLock().lock();
        try {
            casesCache.removeIf(c -> c.getEventNo().equals(eventNo));
            saveToFile();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void save(PatentCase patentCase) {
        lock.writeLock().lock();
        try {
            boolean updated = false;
            for (int i = 0; i < casesCache.size(); i++) {
                if (casesCache.get(i).getEventNo().equals(patentCase.getEventNo())) {
                    casesCache.set(i, patentCase);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                casesCache.add(patentCase);
            }
            saveToFile();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
