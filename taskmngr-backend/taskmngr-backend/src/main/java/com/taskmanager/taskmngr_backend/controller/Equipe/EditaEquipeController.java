package com.taskmanager.taskmngr_backend.controller.Equipe;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.EquipeSemInformacaoException;
import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.Equipe.EditaEquipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipe")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class EditaEquipeController {

    @Autowired
    private EditaEquipeService editaEquipeService;

    @PutMapping("/atualizar/{equId}")
    // CORRIGIDO: Adicionado @AuthenticationPrincipal
    public ResponseEntity<String> atualizarEquipe(@PathVariable String equId, @Valid @RequestBody EquipeDTO dto, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        if (dto.getEquNome() == null || dto.getEquNome().isBlank()) {
            throw new EquipeSemInformacaoException("Erro ao atualizar equipe", "O nome da equipe não pode ser nulo ou vazio.");
        }
        editaEquipeService.editar(equId, dto, usuarioLogado);
        return ResponseEntity.ok("Equipe atualizada com sucesso!");
    }
}