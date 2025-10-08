package com.taskmanager.taskmngr_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;

public interface EquipeRepository extends MongoRepository<EquipeModel, String> {
    boolean existsByEquNome(String equNome);
}
