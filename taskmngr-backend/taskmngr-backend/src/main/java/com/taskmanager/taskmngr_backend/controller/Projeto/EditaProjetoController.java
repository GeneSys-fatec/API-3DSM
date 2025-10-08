package com.taskmanager.taskmngr_backend.controller.Projeto;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.service.ProjetoService;

@RestController
@RequestMapping("/projeto")
@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*")
public class EditaProjetoController {
    @Autowired
    private ProjetoService projetoService;

    @PutMapping("/atualizar/{projId}")
    public ResponseEntity<String> atualizar(@PathVariable String projId, @RequestBody ProjetoDTO dto) {
        Optional<ProjetoModel> projetoExistente = projetoService.buscarPorId(projId);
        if (projetoExistente.isEmpty()) {
            throw new ProjetoNaoEncontradoException(
                    "Projeto não encontrado",
                    "Projeto com id " + projId + " não foi encontrado");
        }
        ProjetoModel p = projetoExistente.get();
        p.setProjNome(dto.getProjNome());
        p.setProjDescricao(dto.getProjDescricao());
        p.setProjDataCriacao(dto.getProjDataCriacao());
        p.setProjDataAtualizacao(dto.getProjDataAtualizacao());
        projetoService.salvar(p);
        return ResponseEntity.ok("Projeto atualizado com sucesso");
    }

    @ExceptionHandler(ProjetoNaoEncontradoException.class)
    public ResponseEntity<String> handleProjetoNaoEncontrado(ProjetoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMensagem());
    }
}
