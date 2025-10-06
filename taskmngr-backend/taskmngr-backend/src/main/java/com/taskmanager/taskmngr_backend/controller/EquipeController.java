package com.taskmanager.taskmngr_backend.controller;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.EquipeSemInformacaoException;
import com.taskmanager.taskmngr_backend.model.AdicionadorLinkEquipe;
import com.taskmanager.taskmngr_backend.model.EquipeModel;
import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.service.EquipeConverterService;
import com.taskmanager.taskmngr_backend.service.EquipeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/equipe")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private EquipeConverterService equipeConverterService;

    @Autowired
    private AdicionadorLinkEquipe adicionadorLink;

    @GetMapping("/listar")
    public ResponseEntity<List<EquipeDTO>> listarEquipes() {
        List<EquipeModel> equipes = equipeService.getAllEquipes();
        List<EquipeDTO> dtos = equipes.stream()
                .map(equipeConverterService::modelParaDto)
                .collect(Collectors.toList());
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{equ_id}")
    public ResponseEntity<EquipeDTO> getEquipeById(@PathVariable String equ_id) {
        EquipeModel equipe = equipeService.getEquipeById(equ_id);
        EquipeDTO dto = equipeConverterService.modelParaDto(equipe);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarEquipe(@Valid @RequestBody EquipeDTO dto) {

        if (dto.getEqu_nome() == null || dto.getEqu_nome().isBlank()) {
            throw new EquipeSemInformacaoException(
                    "Erro ao cadastrar equipe",
                    "O nome da equipe é obrigatório.");
        }

        equipeService.createEquipe(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Equipe cadastrada com sucesso!");
    }

    @PutMapping("/atualizar/{equ_id}")
    public ResponseEntity<String> atualizarEquipe(@PathVariable String equ_id, @Valid @RequestBody EquipeDTO dto) {
        if (dto.getEqu_nome() == null || dto.getEqu_nome().isBlank()) {
            throw new EquipeSemInformacaoException(
                    "Erro ao atualizar equipe",
                    "O nome da equipe não pode ser nulo ou vazio.");
        }

        equipeService.updateEquipe(equ_id, dto);
        return ResponseEntity.ok("Equipe atualizada com sucesso!");
    }

    @DeleteMapping("/apagar/{equ_id}")
    public ResponseEntity<String> apagarEquipe(@PathVariable String equ_id) {
        equipeService.deleteEquipe(equ_id);
        return ResponseEntity.ok("Equipe apagada com sucesso!");
    }
}