package com.taskmanager.taskmngr_backend.repository;

import com.taskmanager.taskmngr_backend.model.EquipeModel;
import com.taskmanager.taskmngr_backend.model.ProjetoModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EquipeRepository extends MongoRepository<EquipeModel, String> {
}
