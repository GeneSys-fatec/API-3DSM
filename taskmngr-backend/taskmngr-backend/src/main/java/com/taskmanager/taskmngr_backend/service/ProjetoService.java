package com.taskmanager.taskmngr_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taskmanager.taskmngr_backend.model.ProjetoModel;
import com.taskmanager.taskmngr_backend.repository.ProjetoRepository;

@Service
public class ProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    public List<ProjetoModel> listarTodas() {
        return projetoRepository.findAll();
    }

    public Optional<ProjetoModel> buscarPorId(String id) {
        return projetoRepository.findById(id);
    }

    public ProjetoModel salvar(ProjetoModel tarefa) {
        return projetoRepository.save(tarefa);
    }

    public void deletar(String id) {
        projetoRepository.deleteById(id);
    }
}
