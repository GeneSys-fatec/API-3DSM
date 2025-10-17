package com.taskmanager.taskmngr_backend.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.converter.NotificacaoConverter;
import com.taskmanager.taskmngr_backend.model.entidade.NotificacaoModel;
import com.taskmanager.taskmngr_backend.repository.NotificacaoRepository;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository repository;

    @Autowired
    private NotificacaoConverter converter;

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
        return repository.findByUsuario(usuarioId);
    }

    private NotificacaoModel salvarSeValido(NotificacaoModel notificacao, String usuarioOrigemId) {
        if (notificacao.getNotUsuarioId().equals(usuarioOrigemId)) {
            return null;
        }

        notificacao.setNotDataCriacao(LocalDateTime.now());
        notificacao.setNotLida(false);
        return repository.save(notificacao);
    }

    public NotificacaoModel criarNotificacaoAtribuicao(String usuarioOrigemId, String usuarioDestinoId, String nomeCriador, String tarefaId, String nomeTarefa) {
        NotificacaoModel notificacao = new NotificacaoModel();
        notificacao.setNotUsuarioId(usuarioDestinoId);
        notificacao.setNotTarefaId(tarefaId);
        notificacao.setNotTipo("ATRIBUICAO");
        notificacao.setNotMensagem("A tarefa " + nomeTarefa + " foi atribuída a você por " + nomeCriador + ".");
        return salvarSeValido(notificacao, usuarioOrigemId);
    }

    public NotificacaoModel criarNotificacaoComentario(String usuarioOrigemId, String usuarioDestinoId, String nomeComentador, String tarefaId) {
        NotificacaoModel notificacao = new NotificacaoModel();
        notificacao.setNotUsuarioId(usuarioDestinoId);
        notificacao.setNotTarefaId(tarefaId);
        notificacao.setNotTipo("COMENTARIO");
        notificacao.setNotMensagem(nomeComentador + " comentou em uma tarefa atribuída a você.");
        return salvarSeValido(notificacao, usuarioOrigemId);
    }

    public NotificacaoModel criarNotificacaoPrazo(String tarefaId, String tituloTarefa,  String usuarioDestinoId ) {
        NotificacaoModel notificacao = new NotificacaoModel();
        notificacao.setNotUsuarioId(usuarioDestinoId);
        notificacao.setNotTipo("PRAZO");
        notificacao.setNotMensagem("A tarefa '" + tituloTarefa + "' está próxima do prazo máximo!");
        notificacao.setNotDataCriacao(LocalDateTime.now());
        notificacao.setNotLida(false);
        return repository.save(notificacao);
    }

    public NotificacaoModel marcarComoLida(String id) {
        Optional<NotificacaoModel> notificacaoOpt = repository.findById(id);

        if (notificacaoOpt.isPresent()) {
            NotificacaoModel notificacao = notificacaoOpt.get();
            notificacao.setNotLida(true);
            return repository.save(notificacao);
        }

        return null;
    }

    public void deletarNotificacao(String id) {
        repository.deleteById(id);
    }
}
