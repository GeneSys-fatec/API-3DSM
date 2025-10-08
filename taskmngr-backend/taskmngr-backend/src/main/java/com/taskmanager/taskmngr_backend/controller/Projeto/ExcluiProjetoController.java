package com.taskmanager.taskmngr_backend.controller.Projeto;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.service.ProjetoService;

@RestController
@RequestMapping("/projeto")
@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*")
public class ExcluiProjetoController {
    @Autowired
    private ProjetoService projetoService;

    @DeleteMapping("/apagar/{projId}")
    public ResponseEntity<String> apagarProjeto(@PathVariable String projId) {
        Optional<ProjetoModel> projetoExistente = projetoService.buscarPorId(projId);
        if (projetoExistente.isEmpty()) {
            throw new ProjetoNaoEncontradoException("Projeto não encontrado",
                    "Não foi possivel deletar o projeto com id " + projId + ", não foi encontrado");
        }
        projetoService.deletar(projId);
        return ResponseEntity.ok("Projeto apagado com sucesso");
    }

    @ExceptionHandler(ProjetoNaoEncontradoException.class)
    public ResponseEntity<String> handleProjetoNaoEncontrado(ProjetoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMensagem());
    }
}
