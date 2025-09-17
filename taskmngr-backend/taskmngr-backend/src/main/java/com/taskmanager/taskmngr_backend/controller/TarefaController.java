package com.taskmanager.taskmngr_backend.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // pra retornar erro/sucesso
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*; //importa todas as annotations do spring de 1 vez

import com.taskmanager.taskmngr_backend.model.AdicionadorLinkTarefa;
import com.taskmanager.taskmngr_backend.model.TarefaModel;
import com.taskmanager.taskmngr_backend.model.dto.TarefaDTO;
import com.taskmanager.taskmngr_backend.service.TarefaService;


@RestController
@RequestMapping("/tarefa")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;
    @Autowired
    private AdicionadorLinkTarefa adicionadorLink;


    @GetMapping("/{tar_id}")
    public ResponseEntity<TarefaDTO> buscarPorId(@PathVariable String tar_id) {
        Optional<TarefaModel> tarefaOpt = tarefaService.buscarPorId(tar_id);
        if (tarefaOpt.isPresent()) {
            TarefaDTO dto = converterModelParaDto(tarefaOpt.get());
            adicionadorLink.adicionarLink(dto);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    } //caso precise buscar por id

    @GetMapping("/listar")
    public ResponseEntity<List<TarefaDTO>> listarTarefa() {
        List<TarefaModel> tarefas = tarefaService.listarTodas();
        List<TarefaDTO> dtos = tarefas.stream()
            .map(this::converterModelParaDto)
            .toList();
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarTarefa(@RequestBody TarefaDTO dto) { //aqui tbm request body pede os atributos
        TarefaModel tarefa = converterDtoParaModel(dto);
        tarefaService.salvar(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Tarefa cadastrada com sucesso!");
    }


    @PutMapping("/atualizar/{tar_id}")
    public ResponseEntity<String> atualizarTarefa(@PathVariable String tar_id, @RequestBody TarefaDTO dto) {
        Optional<TarefaModel> tarefaExistente = tarefaService.buscarPorId(tar_id);
        //busca no banco uma tarefa pelo id que vai ser opcional (pode ou nao existir)
        if (tarefaExistente.isPresent()) {
            TarefaModel t = tarefaExistente.get();
            t.setTar_titulo(dto.getTar_titulo());
            t.setTar_descricao(dto.getTar_descricao());
            t.setTar_status(dto.getTar_status());
            t.setTar_prioridade(dto.getTar_prioridade());
            t.setTar_anexo(dto.getTar_anexo());
            t.setTar_dataCriacao(dto.getTar_dataCriacao());
            t.setTar_dataAtualizacao(dto.getTar_dataAtualizacao());
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
    public ResponseEntity<String> apagarTarefa(@PathVariable String tar_id) { //enqnt o path so o id ? 
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

    
    private TarefaModel converterDtoParaModel(TarefaDTO dto) {
        TarefaModel model = new TarefaModel();
        model.setTar_titulo(dto.getTar_titulo());
        model.setTar_descricao(dto.getTar_descricao());
        model.setTar_status(dto.getTar_status());
        model.setTar_prioridade(dto.getTar_prioridade());
        model.setTar_anexo(dto.getTar_anexo());
        model.setTar_dataCriacao(dto.getTar_dataCriacao());
        model.setTar_dataAtualizacao(dto.getTar_dataAtualizacao());
        model.setUsu_id(dto.getUsu_id());
        model.setUsu_nome(dto.getUsu_nome());
        model.setProj_id(dto.getProj_id());
        model.setProj_nome(dto.getProj_nome());
        return model;
    }

    private TarefaDTO converterModelParaDto(TarefaModel model) {
        TarefaDTO dto = new TarefaDTO();
        dto.setTar_id(model.getTar_id());
        dto.setTar_titulo(model.getTar_titulo());
        dto.setTar_descricao(model.getTar_descricao());
        dto.setTar_prazo(model.getTar_prazo());
        dto.setTar_status(model.getTar_status());
        dto.setTar_prioridade(model.getTar_prioridade());
        dto.setTar_anexo(model.getTar_anexo());
        dto.setTar_dataCriacao(model.getTar_dataCriacao());
        dto.setTar_dataAtualizacao(model.getTar_dataAtualizacao());
        dto.setUsu_id(model.getUsu_id());
        dto.setUsu_nome(model.getUsu_nome());
        dto.setProj_id(model.getProj_id());
        dto.setProj_nome(model.getProj_nome());
        return dto;
    }
}