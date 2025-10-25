package com.taskmanager.taskmngr_backend.service.Notificacao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.entidade.NotificacaoModel;
import com.taskmanager.taskmngr_backend.repository.NotificacaoRepository;

@Service
public class BuscaNotificacaoService {
    @Autowired
    private NotificacaoRepository repository;

    public List<NotificacaoModel> listarTodas() {
        return repository.findAll();
    }

    public Optional<NotificacaoModel> listarPorId(String id) {
        return repository.findById(id);
    }

    public List<NotificacaoModel> listarPorUsuario(String usuarioId) {
        if (usuarioId == null || usuarioId.isBlank()) {
            return Collections.emptyList();
        }
        return repository.findByNotUsuarioIdOrderByNotDataCriacaoDesc(usuarioId);
    }
}

