package com.taskmanager.taskmngr_backend.controller.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.service.ColunaService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/colunas")
public class AtualizaOrdemColunaController {

    @Autowired
    private ColunaService colunaService;

    // Este é o endpoint que o Home.tsx chama
    @PutMapping("/reordenar")
    public ResponseEntity<Void> reordenarColunas(@RequestBody List<Map<String, Object>> colunasOrdem) {
        colunaService.atualizarOrdemColunas(colunasOrdem);
        return ResponseEntity.ok().build();
    }
}