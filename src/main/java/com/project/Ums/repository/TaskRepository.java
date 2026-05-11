package com.project.Ums.repository;

import com.project.Ums.entity.Task;
import com.project.Ums.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {

    List<Task> findByAssignedTo(String userId);

    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    Page<Task> findByAssignedTo(String userId, Pageable pageable);
}
