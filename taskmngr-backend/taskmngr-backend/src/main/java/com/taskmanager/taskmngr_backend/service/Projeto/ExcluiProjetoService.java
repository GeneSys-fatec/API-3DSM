package com.taskmanager.taskmngr_backend.service.Projeto;

import com.taskmanager.taskmngr_backend.repository.ProjetoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcluiProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    public void deletar(String id) { 
        projetoRepository.deleteById(id); 
    }
}