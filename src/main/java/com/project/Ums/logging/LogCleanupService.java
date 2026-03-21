package com.project.Ums.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogCleanupService {

    private static final String FIELD_TIMESTAMP = "timestamp";

    private final ActivityLogRepository logRepository;
    private final MongoTemplate mongoTemplate;

    @Value("${spring.logging.retention.days}")
    private int retentionDays;

    @Value("${spring.logging.cleanup.batch-size}")
    private int batchSize;

    @PostConstruct
    public void initializeIndexes() {
        try {
            IndexOperations indexOps = mongoTemplate.indexOps(ActivityLog.class);
            
            // Create compound index for username + timestamp (most common query)
            indexOps.createIndex(
                new org.springframework.data.mongodb.core.index.CompoundIndexDefinition(
                    new org.bson.Document()
                        .append("username", 1)
                        .append(FIELD_TIMESTAMP, -1)
                ).named("idx_username_timestamp")
            );
            
            // Create index for action filtering
            indexOps.createIndex(
                new Index().on("action", Sort.Direction.ASC).named("idx_action")
            );
            
            // Create index for timestamp sorting
            indexOps.createIndex(
                new Index().on(FIELD_TIMESTAMP, Sort.Direction.DESC).named("idx_timestamp")
            );
            
            log.info("MongoDB Atlas indexes created for activity_logs collection");
            
        } catch (Exception e) {
            log.error("Error creating MongoDB Atlas indexes", e);
        }
    }

    public void cleanupOldLogs() {
        try {
            Instant cutoffDate = Instant.now().minus(retentionDays, ChronoUnit.DAYS);
            
            long totalDeleted = 0;
            long batchDeleted;
            
            do {
                Query query = new Query();
                query.addCriteria(Criteria.where(FIELD_TIMESTAMP).lt(cutoffDate));
                query.limit(batchSize);
                
                batchDeleted = mongoTemplate.remove(query, ActivityLog.class).getDeletedCount();
                totalDeleted += batchDeleted;
                
                log.info("Deleted {} old logs from Atlas (batch), total deleted: {}", batchDeleted, totalDeleted);
                
            } while (batchDeleted > 0 && batchDeleted >= batchSize);
            
            if (totalDeleted > 0) {
                log.info("Atlas log cleanup completed. Total logs deleted: {} (older than {} days)", 
                    totalDeleted, retentionDays);
            } else {
                log.info("No old logs found for cleanup (older than {} days)", retentionDays);
            }
            
        } catch (Exception e) {
            log.error("Error during Atlas log cleanup", e);
        }
    }

    public long getLogCount() {
        return logRepository.count();
    }

    public long getOldLogCount() {
        Instant cutoffDate = Instant.now().minus(retentionDays, ChronoUnit.DAYS);
        Query query = new Query(Criteria.where(FIELD_TIMESTAMP).lt(cutoffDate));
        return mongoTemplate.count(query, ActivityLog.class);
    }
}
