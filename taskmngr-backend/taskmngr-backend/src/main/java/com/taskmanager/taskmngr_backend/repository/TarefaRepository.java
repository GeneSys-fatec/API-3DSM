package com.taskmanager.taskmngr_backend.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.taskmanager.taskmngr_backend.model.TarefaModel;

public interface TarefaRepository extends MongoRepository<TarefaModel, String> {

    @Query("{ 'proj_id': { '$in': ?0 } }")
    List<TarefaModel> findByProjIdIn(List<String> projetoIds);

    @Query("{ 'proj_id': ?0 }")
    List<TarefaModel> findByProjId(String projId);
}