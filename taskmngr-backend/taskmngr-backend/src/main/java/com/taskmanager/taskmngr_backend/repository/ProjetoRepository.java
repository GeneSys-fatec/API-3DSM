package com.taskmanager.taskmngr_backend.repository;

import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;

import java.util.List;

public interface ProjetoRepository extends MongoRepository<ProjetoModel, String> {

    List<ProjetoModel> findByEquipeIn(List<EquipeModel> equipes);

}
