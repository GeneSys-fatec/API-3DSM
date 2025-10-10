package com.taskmanager.taskmngr_backend.repository;

import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EquipeRepository extends MongoRepository<EquipeModel, String> {
    Optional<EquipeModel> findByEquNome(String equNome);
    @Query("{ 'usuarios.usuId' : ?0 }")
    List<EquipeModel> findByUsuariosUsuId(String usuId);
}