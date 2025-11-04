package com.taskmanager.taskmngr_backend.repository;

import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import org.springframework.data.mongodb.repository.MongoRepository;
// import org.springframework.data.mongodb.repository.Query; // Não é mais necessário

import java.util.List;

public interface ProjetoRepository extends MongoRepository<ProjetoModel, String> {

    List<ProjetoModel> findByEquipeIdIn(List<String> equipeIds);
}