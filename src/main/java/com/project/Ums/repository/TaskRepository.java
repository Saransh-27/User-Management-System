package com.project.Ums.repository;

import com.project.Ums.entity.Task;
import com.project.Ums.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByAssignedTo(String username);

    List<Task> findByStatusContainingIgnoreCase(String query);

    List<Task> findByStatusContaining(String inProgress);

}
