package com.taskmanager.taskmngr_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.dto.ColunaDTO;
import com.taskmanager.taskmngr_backend.service.ColunaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/colunas")
public class ColunaController {
    
    @Autowired
    private ColunaService colunaService;

    @GetMapping
     public ResponseEntity<List<ColunaDTO>> listarColunas() {
        return ResponseEntity.ok(colunaService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<ColunaDTO> criarColuna(@Valid @RequestBody ColunaDTO colunaDTO) {
        ColunaDTO novaColuna = colunaService.criarColuna(colunaDTO);
        return new ResponseEntity<>(novaColuna, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColunaDTO> atualizarColuna(@PathVariable String id, @Valid @RequestBody ColunaDTO colunaDTO) {
        ColunaDTO colunaAtualizada = colunaService.atualizarColuna(id, colunaDTO);
        return ResponseEntity.ok(colunaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarColuna(@PathVariable String id) { // id é String
        colunaService.deletarColuna(id);
        return ResponseEntity.noContent().build();
    }
}