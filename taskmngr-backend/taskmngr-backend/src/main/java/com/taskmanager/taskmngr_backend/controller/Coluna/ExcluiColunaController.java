package com.taskmanager.taskmngr_backend.controller.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.service.Coluna.ExcluiColunaService;

@RestController
@RequestMapping("/colunas")
public class ExcluiColunaController {
    @Autowired
    private ExcluiColunaService excluiColunaService;

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarColuna(@PathVariable String id) {
        excluiColunaService.deletarColuna(id);
        return ResponseEntity.noContent().build();
    }
}