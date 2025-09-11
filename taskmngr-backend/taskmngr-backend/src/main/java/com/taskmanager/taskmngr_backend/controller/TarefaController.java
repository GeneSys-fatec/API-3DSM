package com.taskmanager.taskmngr_backend.controller;

import com.taskmanager.taskmngr_backend.model.TarefaModel;
import com.taskmanager.taskmngr_backend.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @GetMapping("/listar")
    public List<TarefaModel> listar() {
        return tarefaService.listarTodas();
    }

    @PostMapping("/cadastrar")
    public String cadastrarTarefa(@RequestBody TarefaModel tarefa) {
        tarefaService.salvar(tarefa);
        return "Tarefa cadastrada com sucesso!";
    }

    @PutMapping("/atualizar")
    public String atualizar(@RequestBody TarefaModel tarefa) {
        Optional<TarefaModel> tarefaExistente = tarefaService.buscarPorId(tarefa.getId());
        //busca no banco uma tarefa pelo id que vai ser opcional (pode ou nao existir)
        if (tarefaExistente.isPresent()) {
            TarefaModel t = tarefaExistente.get();
            t.setTitulo(tarefa.getTitulo());
            t.setDescricao(tarefa.getDescricao());
            t.setStatus(tarefa.getStatus());
            t.setPrioridade(tarefa.getPrioridade());
            t.setAnexo(tarefa.getAnexo());
            t.setDataCriacao(tarefa.getDataCriacao());
            t.setDataAtualizacao(tarefa.getDataAtualizacao());
            t.setUsuId(tarefa.getUsuId());
            t.setUsuNome(tarefa.getUsuNome());
            t.setProjId(tarefa.getProjId());
            t.setProjNome(tarefa.getProjNome());
            tarefaService.salvar(t);
            return "Tarefa atualizada com sucesso";
        } else {
            return "Tarefa não encontrada";
        }
    }

    @DeleteMapping("/apagar")
    public String apagarTarefa(@RequestBody TarefaModel tarefa) {
        tarefaService.deletar(tarefa.getId());
        return "Tarefa apagada com sucesso";
    }
}