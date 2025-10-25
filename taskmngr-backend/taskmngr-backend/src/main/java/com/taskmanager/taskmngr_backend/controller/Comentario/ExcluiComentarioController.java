package com.taskmanager.taskmngr_backend.controller.Comentario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.taskmngr_backend.service.Comentario.ExcluiComentarioService;

@RestController
@RequestMapping("/comentario")
@CrossOrigin(origins = "http://localhost:5173")
public class ExcluiComentarioController {
    @Autowired
    private ExcluiComentarioService excluiComentarioService;

    @DeleteMapping("/apagar/{comId}")
    public ResponseEntity<String> apagarComentario(@PathVariable String comId) {
        excluiComentarioService.deletarRespostaComentario(comId);
        return ResponseEntity.ok("Comentário apagado com sucesso!");
    }
}
