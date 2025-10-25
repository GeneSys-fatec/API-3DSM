package com.taskmanager.taskmngr_backend.controller.Usuario;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.Usuario.BuscaUsuarioService;
import com.taskmanager.taskmngr_backend.service.Usuario.ExcluiUsuarioService;
@RestController
@RequestMapping("/usuario")
public class ExcluiUsuarioController {
    @Autowired
    private ExcluiUsuarioService excluiUsuarioService;
    @Autowired
    private BuscaUsuarioService buscaUsuarioService;

    @DeleteMapping("/apagar/{usuId}")
    public ResponseEntity<String> apagarUsuario(@PathVariable String usuId) { 
        Optional<UsuarioModel> usuarioExistente = buscaUsuarioService.buscarPorId(usuId);
        if (usuarioExistente.isPresent()){
            excluiUsuarioService.deletar(usuId);
            return ResponseEntity.ok("Usuário apagado com sucesso!");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Usuário não encontrado");
        }
    }  
}
