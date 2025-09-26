package com.taskmanager.taskmngr_backend.service;

import com.taskmanager.taskmngr_backend.model.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.TarefaModel;
import com.taskmanager.taskmngr_backend.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    public List<TarefaModel> listarTodas() {
        return tarefaRepository.findAll();
    }

    public List<TarefaModel> listarPorProjetos(List<ProjetoModel> projetos) {
        if (projetos == null || projetos.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> projetoIds = projetos.stream()
                .map(ProjetoModel::getProj_id)
                .collect(Collectors.toList());
        return tarefaRepository.findByProjIdIn(projetoIds);
    }

    // ... dentro da classe TarefaService
    public List<TarefaModel> listarPorProjetoUnico(String projId) {
        if (projId == null || projId.isEmpty()) {
            return Collections.emptyList();
        }
        return tarefaRepository.findByProjId(projId);
    }

    public Optional<TarefaModel> buscarPorId(String id) {
        return tarefaRepository.findById(id);
    }

    public TarefaModel salvar(TarefaModel tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public TarefaModel atualizar(TarefaModel tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public void deletar(String id) {
        tarefaRepository.deleteById(id);
    }
}