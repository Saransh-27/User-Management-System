package com.project.Ums.controller;

import com.project.Ums.logging.ActivityLogRepository;
import com.project.Ums.service.LogCleanupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/logs")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class LogController {

    private final ActivityLogRepository logRepository;
    private final LogCleanupService logCleanupService;

    @Operation(summary = "Get all activity logs", description = "Retrieves paginated activity logs")
    @GetMapping
    public ResponseEntity<Page<com.project.Ums.logging.ActivityLog>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(logRepository.findAll(pageable));
    }

    @Operation(summary = "Get logs by username", description = "Retrieves activity logs for specific user")
    @GetMapping("/user/{username}")
    public ResponseEntity<Page<com.project.Ums.logging.ActivityLog>> getLogsByUsername(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return ResponseEntity.ok(logRepository.findByUsername(username, pageable));
    }

    @Operation(summary = "Get recent logs", description = "Retrieves most recent 50 activity logs")
    @GetMapping("/recent")
    public ResponseEntity<List<com.project.Ums.logging.ActivityLog>> getRecentLogs() {
        Pageable pageable = PageRequest.of(0, 50, Sort.by("timestamp").descending());
        return ResponseEntity.ok(logRepository.findAll(pageable).getContent());
    }
    
    @Operation(summary = "Get log statistics", description = "Retrieves log storage statistics from Atlas")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getLogStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLogs", logCleanupService.getLogCount());
        stats.put("oldLogs", logCleanupService.getOldLogCount());
        stats.put("retentionDays", 90); // Default retention period
        return ResponseEntity.ok(stats);
    }
    
    @Operation(summary = "Manual log cleanup", description = "Manually trigger Atlas log cleanup process")
    @PostMapping("/cleanup")
    public ResponseEntity<Map<String, String>> triggerCleanup() {
        logCleanupService.cleanupOldLogs();
        Map<String, String> response = new HashMap<>();
        response.put("status", "Atlas cleanup process started");
        return ResponseEntity.ok(response);
    }
}
