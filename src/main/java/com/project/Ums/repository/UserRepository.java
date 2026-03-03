package com.project.Ums.repository;

import com.project.Ums.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {
    void deleteUserByUserName(String username);
    Optional<User> findByUserName(String userName);
    Optional<User> findByIdAndEmail(String id, String email);
}
