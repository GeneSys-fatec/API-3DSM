package com.taskmanager.taskmngr_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;

public interface UsuarioRepository extends MongoRepository<UsuarioModel, String> {
    @Query("{ 'usuEmail' : ?0 }")
    Optional<UsuarioModel> findByEmail(String email);

    @Query("{ 'usuEmail' : { $in: ?0 } }")
    List<UsuarioModel> findAllByEmails(List<String> emails);

    List<UsuarioModel> findByUsuEmailIn(List<String> emails);
}