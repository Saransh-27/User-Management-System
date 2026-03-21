package com.project.Ums.logging;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository repository;

    @Async
    public void saveLog(ActivityLog log) {
        log.setTimestamp(Instant.now());
        repository.save(log);
    }
}