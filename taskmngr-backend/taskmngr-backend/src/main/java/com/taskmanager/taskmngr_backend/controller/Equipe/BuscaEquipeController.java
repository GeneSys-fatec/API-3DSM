package com.taskmanager.taskmngr_backend.controller.Equipe;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.AdicionadorLinkEquipe;
import com.taskmanager.taskmngr_backend.model.converter.EquipeConverter;
import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.service.EquipeService;


@RestController
@RequestMapping("/equipe")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class BuscaEquipeController {
    @Autowired
    private EquipeService equipeService;

    @Autowired
    private EquipeConverter equipeConverterService;

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
}
