package com.taskmanager.taskmngr_backend.controller.Notificacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.taskmngr_backend.service.NotificacaoService;

@RestController
@RequestMapping("/notificacao")
@CrossOrigin(origins = "http://localhost:5173")
public class DeletaNotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarNotificacao(@PathVariable String id) {
        try {
            notificacaoService.deletarNotificacao(id);
            return ResponseEntity.noContent().build(); 
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); 
        }
    }
}
