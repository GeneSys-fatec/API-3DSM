package com.taskmanager.taskmngr_backend.controller.Equipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.service.EquipeService;


@RestController
@RequestMapping("/equipe")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class ExcluiEquipeController {
    @Autowired
    private EquipeService equipeService;

    @DeleteMapping("/apagar/{equ_id}")
    public ResponseEntity<String> apagarEquipe(@PathVariable String equ_id) {
        equipeService.deleteEquipe(equ_id);
        return ResponseEntity.ok("Equipe apagada com sucesso!");
    }
}
