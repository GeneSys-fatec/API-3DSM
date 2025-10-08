package com.taskmanager.taskmngr_backend.controller.Equipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.EquipeSemInformacaoException;
import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.service.EquipeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/equipe")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class EditaEquipeController {
    @Autowired
    private EquipeService equipeService;

    @PutMapping("/atualizar/{equ_id}")
    public ResponseEntity<String> atualizarEquipe(@PathVariable String equ_id, @Valid @RequestBody EquipeDTO dto) {
        if (dto.getEquNome() == null || dto.getEquNome().isBlank()) {
            throw new EquipeSemInformacaoException(
                    "Erro ao atualizar equipe",
                    "O nome da equipe não pode ser nulo ou vazio.");
        }

        equipeService.updateEquipe(equ_id, dto);
        return ResponseEntity.ok("Equipe atualizada com sucesso!");
    }
}
