package com.fz.patentmgn.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fz.patentmgn.exception.DataWriteException;
import com.fz.patentmgn.model.OperationLog;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Repository
public class JsonLogRepository {

    private static final Logger log = LoggerFactory.getLogger(JsonLogRepository.class);

    @Value("${patent.logs.file:data/operation_logs.json}")
    private String dataFilePath;

    private final ObjectMapper objectMapper;
    private List<OperationLog> logsCache;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public JsonLogRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.logsCache = new ArrayList<>();
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
                logsCache = objectMapper.readValue(file, new TypeReference<List<OperationLog>>() {});
            } else {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
                saveToFile();
            }
        } catch (IOException e) {
            log.error("無法載入操作日誌檔案", e);
            throw new DataWriteException("系統無法初始化操作日誌", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void saveToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(dataFilePath), logsCache);
        } catch (IOException e) {
            log.error("寫入日誌資料失敗", e);
        }
    }

    public void save(OperationLog operationLog) {
        lock.writeLock().lock();
        try {
            logsCache.add(operationLog);
            saveToFile();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<OperationLog> findAll() {
        lock.readLock().lock();
        try {
            // Return sorted by date explicitly with latest first
            return logsCache.stream()
                .sorted(Comparator.comparing(OperationLog::getTimestamp).reversed())
                .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }
}
