package com.taskmanager.taskmngr_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.taskmanager.taskmngr_backend.model.TarefaModel;

public interface TarefaRepository extends MongoRepository<TarefaModel, String> {

}