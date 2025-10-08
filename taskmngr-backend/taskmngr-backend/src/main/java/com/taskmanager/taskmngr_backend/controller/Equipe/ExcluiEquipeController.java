package com.taskmanager.taskmngr_backend.controller.Equipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.service.Equipe.ExcluiEquipeService;

@RestController
@RequestMapping("/equipe")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class ExcluiEquipeController {
    @Autowired
    private ExcluiEquipeService excluiEquipeService;

    @DeleteMapping("/apagar/{equId}")
    public ResponseEntity<String> apagarEquipe(@PathVariable String equId) {
        excluiEquipeService.deleteEquipe(equId);
        return ResponseEntity.ok("Equipe apagada com sucesso!");
    }
}
