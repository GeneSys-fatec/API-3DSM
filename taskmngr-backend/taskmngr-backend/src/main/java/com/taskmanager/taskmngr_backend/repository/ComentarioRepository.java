package com.taskmanager.taskmngr_backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.taskmanager.taskmngr_backend.model.entidade.ComentarioModel;

public interface ComentarioRepository extends MongoRepository<ComentarioModel, String> {
    List<ComentarioModel> findBytarId(String tarId);
    @Query("{ 'comResposta': ?0 }")
    List<ComentarioModel> findByRespostaComentario(String comResposta);
}
