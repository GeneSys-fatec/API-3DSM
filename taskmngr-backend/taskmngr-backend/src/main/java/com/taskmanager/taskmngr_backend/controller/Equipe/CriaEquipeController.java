package com.taskmanager.taskmngr_backend.controller.Equipe;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.EquipeSemInformacaoException;
import com.taskmanager.taskmngr_backend.model.converter.EquipeConverter;
import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.Equipe.CriaEquipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipe")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class CriaEquipeController {

    @Autowired
    private EquipeConverter equipeConverter;
    @Autowired
    private CriaEquipeService criaEquipeService;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarEquipe(@Valid @RequestBody EquipeDTO dto, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        if (dto.getEquNome() == null || dto.getEquNome().isBlank()) {
            throw new EquipeSemInformacaoException("Erro ao cadastrar equipe", "O nome da equipe é obrigatório.");
        }
        criaEquipeService.criar(dto, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body("Equipe cadastrada com sucesso!");
    }
}