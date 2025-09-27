package com.taskmanager.taskmngr_backend.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // <-- IMPORT ADICIONADO
import org.springframework.web.bind.annotation.*;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.tarefas.InvalidTaskDataException;
import com.taskmanager.taskmngr_backend.model.AdicionadorLinkTarefa;
import com.taskmanager.taskmngr_backend.model.ProjetoModel; // <-- IMPORT ADICIONADO
import com.taskmanager.taskmngr_backend.model.TarefaModel;
import com.taskmanager.taskmngr_backend.model.UsuarioModel; // <-- IMPORT ADICIONADO
import com.taskmanager.taskmngr_backend.model.dto.TarefaDTO;
import com.taskmanager.taskmngr_backend.service.ProjetoService; // <-- IMPORT ADICIONADO
import com.taskmanager.taskmngr_backend.service.TarefaConverterService;
import com.taskmanager.taskmngr_backend.service.TarefaService;


@RestController
@RequestMapping("/tarefa")
@CrossOrigin(origins = "http://localhost:5173")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private AdicionadorLinkTarefa adicionadorLink;

    @Autowired
    private TarefaConverterService tarefaConverterService;

    @GetMapping("/listar-por-usuario")
    public ResponseEntity<List<TarefaDTO>> listarTarefasDoUsuario(@AuthenticationPrincipal UsuarioModel usuario) {
        List<ProjetoModel> projetosDoUsuario = projetoService.listarPorUsuario(usuario);

        List<TarefaModel> tarefas = tarefaService.listarPorProjetos(projetosDoUsuario);

        List<TarefaDTO> dtos = tarefas.stream()
                .map(tarefaConverterService::modelParaDto)
                .toList();
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{tar_id}")
    public ResponseEntity<TarefaDTO> buscarPorId(@PathVariable String tar_id) {
        Optional<TarefaModel> tarefaOpt = tarefaService.buscarPorId(tar_id);
        if (tarefaOpt.isPresent()) {
            TarefaDTO dto = tarefaConverterService.modelParaDto(tarefaOpt.get());
            adicionadorLink.adicionarLink(dto);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/por-projeto/{projId}")
    public ResponseEntity<List<TarefaDTO>> listarTarefasPorProjeto(@PathVariable String projId) {
        List<TarefaModel> tarefas = tarefaService.listarPorProjetoUnico(projId);
        List<TarefaDTO> dtos = tarefas.stream()
                .map(tarefaConverterService::modelParaDto)
                .toList();
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<TarefaDTO>> listarTarefa() {
        List<TarefaModel> tarefas = tarefaService.listarTodas();
        List<TarefaDTO> dtos = tarefas.stream()
                .map(tarefaConverterService::modelParaDto)
                .toList();
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarTarefa(@RequestBody TarefaDTO dto, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        if (dto.getTar_titulo() == null || dto.getTar_titulo().trim().isEmpty()) {
            throw new InvalidTaskDataException("Dados da tarefa inválidos", "Título é obrigatório");
        }
        if (dto.getTar_descricao() == null || dto.getTar_descricao().trim().isEmpty()) {
            throw new InvalidTaskDataException("Dados da tarefa inválidos", "Descrição é obrigatória");
        }
        if (dto.getTar_status() == null || dto.getTar_status().trim().isEmpty()) {
            throw new InvalidTaskDataException("Dados da tarefa inválidos", "Status é obrigatório");
        }
        if (dto.getTar_prioridade() == null || dto.getTar_prioridade().trim().isEmpty()) {
            throw new InvalidTaskDataException("Dados da tarefa inválidos", "Prioridade é obrigatória");
        }
        TarefaModel tarefa = tarefaConverterService.dtoParaModel(dto);
        if (usuarioLogado != null) {
            tarefa.setUsu_id(usuarioLogado.getUsu_id());
            tarefa.setUsu_nome(usuarioLogado.getUsu_nome());
        }
        tarefaService.salvar(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Tarefa cadastrada com sucesso!");
    }

    @PutMapping("/atualizar/{tar_id}")
    public ResponseEntity<String> atualizarTarefa(@PathVariable String tar_id, @RequestBody TarefaDTO dto, @AuthenticationPrincipal UsuarioModel usuarioLogado) { // Adicionado o usuarioLogado
        if (dto.getTar_titulo() == null || dto.getTar_titulo().trim().isEmpty()) {
            throw new InvalidTaskDataException("Dados da tarefa inválidos", "Título é obrigatório");
        }
        if (dto.getTar_descricao() == null || dto.getTar_descricao().trim().isEmpty()) {
            throw new InvalidTaskDataException("Dados da tarefa inválidos", "Descrição é obrigatória");
        }
        Optional<TarefaModel> tarefaExistente = tarefaService.buscarPorId(tar_id);
        if (tarefaExistente.isPresent()) {
            TarefaModel t = tarefaExistente.get();
            t.setTar_titulo(dto.getTar_titulo());
            t.setTar_descricao(dto.getTar_descricao());
            t.setTar_status(dto.getTar_status());
            t.setTar_prioridade(dto.getTar_prioridade());
            t.setTar_anexo(dto.getTar_anexo());
            t.setTar_prazo(dto.getTar_prazo());
            t.setTar_dataCriacao(dto.getTar_dataCriacao());
            t.setTar_dataAtualizacao(dto.getTar_dataAtualizacao());

            // AJUSTADO: Usar o usuário logado em vez do que vem do frontend
            t.setUsu_id(dto.getUsu_id());
            t.setUsu_nome(dto.getUsu_nome());

            t.setProj_id(dto.getProj_id());
            t.setProj_nome(dto.getProj_nome());
            tarefaService.salvar(t);
            return ResponseEntity.ok("Tarefa atualizada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tarefa não encontrada");
        }
    }

    @DeleteMapping("/apagar/{tar_id}")
    public ResponseEntity<String> apagarTarefa(@PathVariable String tar_id) {
        Optional<TarefaModel> tarefaExistente = tarefaService.buscarPorId(tar_id);
        if (tarefaExistente.isPresent()){
            tarefaService.deletar(tar_id);
            return ResponseEntity.ok("Tarefa apagada com sucesso");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tarefa não encontrada");
        }
    }
}