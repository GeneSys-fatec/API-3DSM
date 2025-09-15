package com.taskmanager.taskmngr_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.taskmanager.taskmngr_backend.model.ProjetoModel;

public interface ProjetoRepository extends MongoRepository<ProjetoModel, String> {

}
