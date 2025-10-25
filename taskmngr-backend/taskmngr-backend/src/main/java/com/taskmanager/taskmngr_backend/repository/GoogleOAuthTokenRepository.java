package com.taskmanager.taskmngr_backend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.taskmanager.taskmngr_backend.model.entidade.GoogleOAuthToken;

public interface GoogleOAuthTokenRepository extends MongoRepository<GoogleOAuthToken, String> {
    Optional<GoogleOAuthToken> findByUserKey(String userKey);
    boolean existsByUserKey(String userKey);
}