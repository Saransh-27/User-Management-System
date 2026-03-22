package com.project.Ums.controller;

import com.project.Ums.entity.User;
import com.project.Ums.logging.ActivityLog;
import com.project.Ums.logging.ActivityLogRepository;
import com.project.Ums.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reports", description = "System report generation and download")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final UserRepository userRepository;
    private final ActivityLogRepository activityLogRepository;

    @Operation(summary = "Download full system report", description = "Generates a CSV report containing system summary, all users, and recent activity logs")
    @GetMapping("/system")
    public ResponseEntity<byte[]> downloadSystemReport() {
        log.info("Generating full system report...");
        
        List<User> users = userRepository.findAll();
        List<ActivityLog> logs = activityLogRepository.findAll(
            PageRequest.of(0, 500, Sort.by("timestamp").descending())
        ).getContent();

        StringBuilder csv = new StringBuilder();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // ── Section 1: System Summary ──
        csv.append("=== SYSTEM REPORT ===\r\n");
        csv.append("Generated At,").append(timestamp).append("\r\n");
        csv.append("\r\n");
        csv.append("=== SYSTEM SUMMARY ===\r\n");
        csv.append("Metric,Value\r\n");
        csv.append("Total Users,").append(users.size()).append("\r\n");
        csv.append("Active Users,").append(users.stream().filter(u -> "ACTIVE".equals(u.getStatus())).count()).append("\r\n");
        csv.append("Pending Users,").append(users.stream().filter(u -> "PENDING".equals(u.getStatus())).count()).append("\r\n");
        csv.append("Admin Users,").append(users.stream().filter(u -> u.getRoles() != null && u.getRoles().contains("ADMIN")).count()).append("\r\n");
        csv.append("Regular Users,").append(users.stream().filter(u -> u.getRoles() != null && !u.getRoles().contains("ADMIN")).count()).append("\r\n");
        csv.append("Total Activity Logs,").append(activityLogRepository.count()).append("\r\n");
        csv.append("\r\n");

        // ── Section 2: All Users ──
        csv.append("=== ALL USERS ===\r\n");
        csv.append("ID,Username,Email,Roles,Status,Created At\r\n");
        for (User user : users) {
            csv.append(escapeCsv(user.getId())).append(",");
            csv.append(escapeCsv(user.getUserName())).append(",");
            csv.append(escapeCsv(user.getEmail())).append(",");
            csv.append(escapeCsv(user.getRoles() != null ? String.join(";", user.getRoles()) : "")).append(",");
            csv.append(escapeCsv(user.getStatus())).append(",");
            csv.append(escapeCsv(user.getCreatedAt() != null ? user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A"));
            csv.append("\r\n");
        }
        csv.append("\r\n");

        // ── Section 3: Activity Logs ──
        csv.append("=== RECENT ACTIVITY LOGS (Last 500) ===\r\n");
        csv.append("Username,Action,Description,Timestamp,Success,IP Address\r\n");
        for (ActivityLog logEntry : logs) {
            csv.append(escapeCsv(logEntry.getUsername())).append(",");
            csv.append(escapeCsv(logEntry.getAction())).append(",");
            csv.append(escapeCsv(logEntry.getDescription())).append(",");
            csv.append(escapeCsv(logEntry.getTimestamp() != null ? logEntry.getTimestamp().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A")).append(",");
            csv.append(logEntry.isSuccess() ? "Yes" : "No").append(",");
            csv.append(escapeCsv(logEntry.getIpAddress()));
            csv.append("\r\n");
        }

        byte[] content = csv.toString().getBytes();
        String filename = "UMS_System_Report_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";

        log.info("System report generated: {} bytes, {} users, {} logs", content.length, users.size(), logs.size());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(content.length)
                .body(content);
    }

    @Operation(summary = "Download users report", description = "Generates a CSV report of all users")
    @GetMapping("/users")
    public ResponseEntity<byte[]> downloadUsersReport() {
        List<User> users = userRepository.findAll();

        StringBuilder csv = new StringBuilder();
        csv.append("ID,Username,Email,Roles,Status,Created At\r\n");
        for (User user : users) {
            csv.append(escapeCsv(user.getId())).append(",");
            csv.append(escapeCsv(user.getUserName())).append(",");
            csv.append(escapeCsv(user.getEmail())).append(",");
            csv.append(escapeCsv(user.getRoles() != null ? String.join(";", user.getRoles()) : "")).append(",");
            csv.append(escapeCsv(user.getStatus())).append(",");
            csv.append(escapeCsv(user.getCreatedAt() != null ? user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A"));
            csv.append("\r\n");
        }

        byte[] content = csv.toString().getBytes();
        String filename = "UMS_Users_Report_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(content.length)
                .body(content);
    }

    @Operation(summary = "Download activity logs report", description = "Generates a CSV report of activity logs")
    @GetMapping("/activity-logs")
    public ResponseEntity<byte[]> downloadActivityLogsReport() {
        List<ActivityLog> logs = activityLogRepository.findAll(
            PageRequest.of(0, 1000, Sort.by("timestamp").descending())
        ).getContent();

        StringBuilder csv = new StringBuilder();
        csv.append("Username,Action,Description,Timestamp,Success,IP Address,User Agent\r\n");
        for (ActivityLog logEntry : logs) {
            csv.append(escapeCsv(logEntry.getUsername())).append(",");
            csv.append(escapeCsv(logEntry.getAction())).append(",");
            csv.append(escapeCsv(logEntry.getDescription())).append(",");
            csv.append(escapeCsv(logEntry.getTimestamp() != null ? logEntry.getTimestamp().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A")).append(",");
            csv.append(logEntry.isSuccess() ? "Yes" : "No").append(",");
            csv.append(escapeCsv(logEntry.getIpAddress())).append(",");
            csv.append(escapeCsv(logEntry.getUserAgent()));
            csv.append("\r\n");
        }

        byte[] content = csv.toString().getBytes();
        String filename = "UMS_Activity_Logs_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(content.length)
                .body(content);
    }

    /**
     * Escapes a value for CSV format (handles commas, quotes, newlines)
     */
    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
