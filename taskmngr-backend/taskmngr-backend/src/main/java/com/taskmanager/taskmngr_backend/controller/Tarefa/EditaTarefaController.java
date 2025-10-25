package com.taskmanager.taskmngr_backend.controller.Tarefa;

// <<< MUDANÇA: Imports desnecessários removidos (Optional, Collectors, ResponsavelTarefa, TarefaModel)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.tarefas.InvalidTaskDataException;
import com.taskmanager.taskmngr_backend.model.dto.TarefaDTO;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.Tarefa.EditaTarefaService;

@RestController
@RequestMapping("/tarefa")
@CrossOrigin(origins = "http://localhost:5173")
public class EditaTarefaController {
    @Autowired
    private EditaTarefaService editaTarefaService;

    @PutMapping("/atualizar/{tarId}")
    public ResponseEntity<String> atualizarTarefa(@PathVariable String tarId, @RequestBody TarefaDTO dto,
                                                  @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        if (dto.getTarTitulo() == null || dto.getTarTitulo().isBlank() ||
                dto.getTarDescricao() == null || dto.getTarDescricao().isBlank() ||
                dto.getTarPrazo() == null || dto.getTarPrazo().isBlank()) {

            throw new InvalidTaskDataException("Erro ao cadastrar tarefa",
                    "Título, descrição e data são obrigatórios.");
        }
        try {
            editaTarefaService.atualizarTarefa(tarId, dto, usuarioLogado);
            return ResponseEntity.ok("Tarefa atualizada com sucesso!");

        } catch (RuntimeException e) { // <<< Idealmente, troque por uma exceção mais específica (ex: TarefaNotFoundException)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}