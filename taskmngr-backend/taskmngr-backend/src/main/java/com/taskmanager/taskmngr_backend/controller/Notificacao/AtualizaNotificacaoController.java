package com.taskmanager.taskmngr_backend.controller.Notificacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.NotificacaoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


@RestController
@RequestMapping("/notificacao")
@CrossOrigin(origins = "http://localhost:5173")
public class AtualizaNotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    @PutMapping("/marcar-lida/{id}")
    public ResponseEntity<Void> marcarComoLida(@PathVariable String id) {
        notificacaoService.marcarComoLida(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/marcar-todas")
    public ResponseEntity<Void> marcarTodasComoLidas(@AuthenticationPrincipal UsuarioModel usuario) {
        notificacaoService.marcarTodasComoLidas(usuario.getUsuId());
        return ResponseEntity.ok().build();
    }
}
