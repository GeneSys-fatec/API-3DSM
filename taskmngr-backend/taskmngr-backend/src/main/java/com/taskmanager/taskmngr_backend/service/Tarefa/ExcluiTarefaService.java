package com.taskmanager.taskmngr_backend.service.Tarefa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taskmanager.taskmngr_backend.repository.TarefaRepository;

@Service
public class ExcluiTarefaService {
    @Autowired
    private TarefaRepository tarefaRepository;

    public void deletar(String id) {
        tarefaRepository.deleteById(id);
    }
}