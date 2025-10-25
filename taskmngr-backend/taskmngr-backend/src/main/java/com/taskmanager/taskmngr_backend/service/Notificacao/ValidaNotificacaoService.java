package com.taskmanager.taskmngr_backend.service.Notificacao;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.entidade.NotificacaoModel;
import com.taskmanager.taskmngr_backend.repository.NotificacaoRepository;

@Service
public class ValidaNotificacaoService {
    @Autowired
    private NotificacaoRepository repository;

    public NotificacaoModel salvarSeValido(NotificacaoModel notificacao, String usuarioOrigemId) {
        if (notificacao.getNotUsuarioId().equals(usuarioOrigemId)) {
            return null;
        }

        notificacao.setNotDataCriacao(LocalDateTime.now());
        notificacao.setNotLida(false);
        return repository.save(notificacao);
    }
}
