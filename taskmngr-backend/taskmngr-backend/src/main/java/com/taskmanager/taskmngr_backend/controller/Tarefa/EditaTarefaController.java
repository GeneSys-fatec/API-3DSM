package com.taskmanager.taskmngr_backend.controller.Tarefa;

import java.util.Optional;

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
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.TarefaService;

@RestController
@RequestMapping("/tarefa")
@CrossOrigin(origins = "http://localhost:5173")
public class EditaTarefaController {
    @Autowired
    private TarefaService tarefaService;

    @PutMapping("/atualizar/{tarId}")
    public ResponseEntity<String> atualizarTarefa(@PathVariable String tarId, @RequestBody TarefaDTO dto,
            @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        if (dto.getTarTitulo() == null || dto.getTarTitulo().isBlank() ||
                dto.getTarDescricao() == null || dto.getTarDescricao().isBlank() ||
                dto.getTarPrazo() == null || dto.getTarPrazo().isBlank()) {

            throw new InvalidTaskDataException("Erro ao cadastrar tarefa",
                    "Título, descrição e data são obrigatórios.");
        }
        Optional<TarefaModel> tarefaExistente = tarefaService.buscarPorId(tarId);
        if (tarefaExistente.isPresent()) {
            TarefaModel t = tarefaExistente.get();
            t.setTarTitulo(dto.getTarTitulo());
            t.setTarDescricao(dto.getTarDescricao());
            t.setTarStatus(dto.getTarStatus());
            t.setTarPrioridade(dto.getTarPrioridade());
            t.setTarPrazo(dto.getTarPrazo());
            t.setTarDataCriacao(dto.getTarDataCriacao());
            t.setTarDataAtualizacao(dto.getTarDataAtualizacao());

            t.setUsuId(dto.getUsuId());
            t.setUsuNome(dto.getUsuNome());

            t.setProjId(dto.getProjId());
            t.setProjNome(dto.getProjNome());
            tarefaService.salvar(t);
            return ResponseEntity.ok("Tarefa atualizada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tarefa não encontrada");
        }
    }
}
