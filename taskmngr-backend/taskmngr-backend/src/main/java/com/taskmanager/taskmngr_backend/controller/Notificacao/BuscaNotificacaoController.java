package com.taskmanager.taskmngr_backend.controller.Notificacao;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.taskmngr_backend.model.converter.NotificacaoConverter;
import com.taskmanager.taskmngr_backend.model.dto.NotificacaoDTO;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.Notificacao.BuscaNotificacaoService;

@RestController
@RequestMapping("/notificacao")
@CrossOrigin(origins = "http://localhost:5173")
public class BuscaNotificacaoController {

    @Autowired
    private BuscaNotificacaoService buscaNotificacaoService;

    @Autowired
    private NotificacaoConverter notificacaoConverter;

    @GetMapping("/listar")
    public ResponseEntity<List<NotificacaoDTO>> listarPorUsuario(
            @AuthenticationPrincipal UsuarioModel usuarioLogado) {

        List<NotificacaoDTO> notificacoes = buscaNotificacaoService
                .listarPorUsuario(usuarioLogado.getUsuId())
                .stream()
                .map(notificacao -> notificacaoConverter.modelParaDto(notificacao))
                .collect(Collectors.toList());

        return ResponseEntity.ok(notificacoes);
    }
}
