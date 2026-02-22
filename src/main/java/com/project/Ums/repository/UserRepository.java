package com.project.Ums.repository;

import com.project.Ums.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {
    void deleteUserByUserName(String username);
    User findByUserName(String userName);
}
