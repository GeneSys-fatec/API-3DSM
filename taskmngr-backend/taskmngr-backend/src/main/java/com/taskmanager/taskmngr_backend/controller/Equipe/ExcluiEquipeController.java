package com.taskmanager.taskmngr_backend.controller.Equipe;

import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.Equipe.ExcluiEquipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipe")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class ExcluiEquipeController {
    @Autowired
    private ExcluiEquipeService excluiEquipeService;

    @DeleteMapping("/apagar/{equId}")
    public ResponseEntity<String> apagarEquipe(@PathVariable String equId, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        excluiEquipeService.excluir(equId, usuarioLogado);
        return ResponseEntity.ok("Equipe apagada com sucesso!");
    }
}