package com.project.Ums.logging;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ActivityLogRepository extends MongoRepository<ActivityLog, String> {
    Page<ActivityLog> findByUsername(String username, Pageable pageable);
    Page<ActivityLog> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    Page<ActivityLog> findByActionContainingIgnoreCase(String action, Pageable pageable);
    Page<ActivityLog> findByUsernameContainingIgnoreCaseAndActionContainingIgnoreCase(String username, String action, Pageable pageable);
}
