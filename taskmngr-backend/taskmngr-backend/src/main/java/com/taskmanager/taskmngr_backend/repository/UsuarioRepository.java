package com.taskmanager.taskmngr_backend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.taskmanager.taskmngr_backend.model.UsuarioModel;

public interface UsuarioRepository extends MongoRepository<UsuarioModel, String> {

    /**
     * Busca um usuário pelo seu email, usando uma consulta explícita do MongoDB.
     * A anotação @Query garante que a busca será feita no campo correto ('usu_email').
     * O '?0' é um placeholder para o primeiro parâmetro do método (a String 'email').
     */
    @Query("{ 'usu_email' : ?0 }")
    Optional<UsuarioModel> findByEmail(String email);
}