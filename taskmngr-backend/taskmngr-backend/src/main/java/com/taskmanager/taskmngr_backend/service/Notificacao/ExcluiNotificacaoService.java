package com.taskmanager.taskmngr_backend.service.Notificacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.repository.NotificacaoRepository;

@Service
public class ExcluiNotificacaoService {
    @Autowired
    private NotificacaoRepository repository;

    public void deletarNotificacao(String id) {
        repository.deleteById(id);
    }
}
