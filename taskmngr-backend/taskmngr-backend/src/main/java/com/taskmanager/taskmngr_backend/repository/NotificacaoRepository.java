package com.taskmanager.taskmngr_backend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.taskmanager.taskmngr_backend.model.entidade.NotificacaoModel;

public interface  NotificacaoRepository extends MongoRepository<NotificacaoModel, String> {
    @Query("{ 'NotUsuarioId': ?0 }")
    List<NotificacaoModel> findByUsuario(String usuarioId);

    List<NotificacaoModel> findByNotUsuarioIdAndNotLidaFalse(String notUsuarioId);

    List<NotificacaoModel> findByNotUsuarioIdOrderByNotDataCriacaoDesc(String usuarioId);
}
