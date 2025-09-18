package com.taskmanager.taskmngr_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.taskmanager.taskmngr_backend.model.UsuarioModel;

public interface UsuarioRepository extends MongoRepository<UsuarioModel, String> {
    
}
