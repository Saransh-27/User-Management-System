package com.project.Ums.repository;

import com.project.Ums.entity.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends MongoRepository<VerificationToken, String> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUserId(String userId);
    void deleteByUserId(String userId);
    void deleteByToken(String token);
}
