package com.taskmanager.taskmngr_backend.controller.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.dto.ColunaDTO;
import com.taskmanager.taskmngr_backend.service.ColunaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/colunas")
public class EditaColunaController {
    @Autowired
    private ColunaService colunaService;

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ColunaDTO> atualizarColuna(@PathVariable String id, @Valid @RequestBody ColunaDTO colunaDTO) {
        ColunaDTO colunaAtualizada = colunaService.atualizarColuna(id, colunaDTO);
        return ResponseEntity.ok(colunaAtualizada);
    }
}
