package com.taskmanager.taskmngr_backend.repository;

import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProjetoRepository extends MongoRepository<ProjetoModel, String> {
    @Query("{ 'equipe.$id' : { $in: ?0 } }")
    List<ProjetoModel> findByEquipeEquIdIn(List<String> equipeIds);
}