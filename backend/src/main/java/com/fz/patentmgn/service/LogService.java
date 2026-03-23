package com.fz.patentmgn.service;

import com.fz.patentmgn.model.OperationLog;
import com.fz.patentmgn.repository.JsonLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LogService {
    private final JsonLogRepository repository;
    public LogService(JsonLogRepository repository) { this.repository = repository; }

    public void recordLog(String username, String action) {
        if (username == null || username.trim().isEmpty()) {
            username = "System";
        }
        OperationLog log = new OperationLog(
            UUID.randomUUID().toString(),
            username,
            action,
            LocalDateTime.now()
        );
        repository.save(log);
    }

    public List<OperationLog> getAllLogs() {
        return repository.findAll();
    }
}
