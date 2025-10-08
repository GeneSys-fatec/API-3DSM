package com.taskmanager.taskmngr_backend.controller.Equipe;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.converter.EquipeConverter;
import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.service.Equipe.CriaEquipeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/equipe")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class CriaEquipeController {

    @Autowired
    private EquipeConverter equipeConverter;

    @Autowired
    private CriaEquipeService criaEquipeService;

    @PostMapping("/cadastrar")
    public ResponseEntity<EquipeDTO> cadastrarEquipe(@Valid @RequestBody EquipeDTO dto) {

        EquipeModel equipeSalva = criaEquipeService.createEquipe(dto); 

        EquipeDTO dtoDeResposta = equipeConverter.modelParaDto(equipeSalva);

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoDeResposta);
    }
}
