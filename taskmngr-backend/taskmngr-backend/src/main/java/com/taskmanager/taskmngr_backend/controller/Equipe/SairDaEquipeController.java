package com.taskmanager.taskmngr_backend.controller.Equipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.Equipe.SairDaEquipeService;

@RestController
@RequestMapping("/equipe")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class SairDaEquipeController {

    @Autowired
    private SairDaEquipeService sairDaEquipeService;

    @PostMapping("/{equipeId}/sair")
    public ResponseEntity<String> sairDaEquipe(
            @PathVariable String equipeId,
            @AuthenticationPrincipal UsuarioModel usuarioLogado) {

        sairDaEquipeService.sair(equipeId, usuarioLogado);

        return ResponseEntity.ok("Você saiu da equipe com sucesso.");
    }
}