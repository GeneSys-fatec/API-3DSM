package com.taskmanager.taskmngr_backend.controller.Notificacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.taskmanager.taskmngr_backend.service.Notificacao.MarcaComoLidaNotificacaoService;


@RestController
@RequestMapping("/notificacao")
@CrossOrigin(origins = "http://localhost:5173")
public class AtualizaNotificacaoController {

    @Autowired
    private MarcaComoLidaNotificacaoService marcaComoLidaNotificacaoService;

    @PutMapping("/marcar-lida/{id}")
    public ResponseEntity<Void> marcarComoLida(@PathVariable String id) {
        marcaComoLidaNotificacaoService.marcarComoLida(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/marcar-todas")
    public ResponseEntity<Void> marcarTodasComoLidas(@AuthenticationPrincipal UsuarioModel usuario) {
        marcaComoLidaNotificacaoService.marcarTodasComoLidas(usuario.getUsuId());
        return ResponseEntity.ok().build();
    }
}
