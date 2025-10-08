package com.taskmanager.taskmngr_backend.controller.Coluna;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.dto.ColunaDTO;
import com.taskmanager.taskmngr_backend.service.ColunaService;

@RestController
@RequestMapping("/colunas")
public class BuscaColunaController {
    @Autowired
    private ColunaService colunaService;

    @GetMapping("/por-projeto/{projId}")
    public ResponseEntity<List<ColunaDTO>> listarColunasPorProjeto(@PathVariable String projId) {
        return ResponseEntity.ok(colunaService.listarPorProjeto(projId));
    }
}
