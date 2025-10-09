package com.taskmanager.taskmngr_backend.repository;

import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;

import java.util.List;
import java.util.Optional;

public interface EquipeRepository extends MongoRepository<EquipeModel, String> {
    Optional<EquipeModel> findByEquNome(String equNome);
    List<EquipeModel> findByUsuariosContaining(UsuarioModel usuario);//encontra todas as equipes de um usuario especifico
}
