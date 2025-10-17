package com.taskmanager.taskmngr_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;

public interface TarefaRepository extends MongoRepository<TarefaModel, String> {

    @Query("{ 'projId': { '$in': ?0 } }")
    List<TarefaModel> findByProjIdIn(List<String> projetoIds);

    @Query("{ 'projId': ?0 }")
    List<TarefaModel> findByProjId(String projId);

    List<TarefaModel> findByTarPrazo(LocalDate tarPrazo); 

    List<TarefaModel> findByTarPrazoBeforeAndTarStatusNot(LocalDate tarPrazo, String status);
}