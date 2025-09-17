package com.taskmanager.taskmngr_backend.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // pra retornar erro/sucesso
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*; //importa todas as annotations do spring de 1 vez

import com.taskmanager.taskmngr_backend.model.AdicionadorLinkTarefa;
import com.taskmanager.taskmngr_backend.model.TarefaModel;
import com.taskmanager.taskmngr_backend.service.TarefaService;


@RestController
@RequestMapping("/tarefa")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;
    @Autowired
    private AdicionadorLinkTarefa adicionadorLink;


    @GetMapping("/{tar_id}")
    public ResponseEntity<TarefaModel> buscarPorId(@PathVariable String tar_id) {
        Optional<TarefaModel> tarefaOpt = tarefaService.buscarPorId(tar_id);
        if (tarefaOpt.isPresent()) {
            TarefaModel tarefa = tarefaOpt.get();
            adicionadorLink.adicionarLink(tarefa);
            return ResponseEntity.ok(tarefa);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    } // nao sei se vai precisar pegar por id mas caso precise ja fiz o get

    @GetMapping("/listar")
    public ResponseEntity<List<TarefaModel>> listarTarefa() { //responseEntity aq (importante)
        List<TarefaModel> tarefas = tarefaService.listarTodas();
        adicionadorLink.adicionarLink(tarefas);
        return ResponseEntity.ok(tarefas);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarTarefa(@RequestBody TarefaModel tarefa) { //aqui tbm request body pede os atributos acgo
        tarefaService.salvar(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Tarefa cadastrada com sucesso!");
    }


    @PutMapping("/atualizar/{tar_id}")
    public ResponseEntity<String> atualizarTarefa(@PathVariable String tar_id, @RequestBody TarefaModel tarefa) {
        Optional<TarefaModel> tarefaExistente = tarefaService.buscarPorId(tar_id);
        //busca no banco uma tarefa pelo id que vai ser opcional (pode ou nao existir)
        if (tarefaExistente.isPresent()) {
            TarefaModel t = tarefaExistente.get();
            t.setTar_titulo(tarefa.getTar_titulo());
            t.setTar_descricao(tarefa.getTar_descricao());
            t.setTar_status(tarefa.getTar_status());
            t.setTar_prioridade(tarefa.getTar_prioridade());
            t.setTar_anexo(tarefa.getTar_anexo());
            t.setTar_dataCriacao(tarefa.getTar_dataCriacao());
            t.setTar_dataAtualizacao(tarefa.getTar_dataAtualizacao());
            t.setUsu_id(tarefa.getUsu_id());
            t.setUsu_nome(tarefa.getUsu_nome());
            t.setProj_id(tarefa.getProj_id());
            t.setProj_nome(tarefa.getProj_nome());
            tarefaService.salvar(t);
            return ResponseEntity.ok("Tarefa cadastrada com sucesso!");
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
}