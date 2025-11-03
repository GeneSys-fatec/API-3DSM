package com.taskmanager.taskmngr_backend.service.Projeto;

import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.repository.ProjetoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class EditaProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;
    
    public ProjetoModel salvar(ProjetoModel projeto) { 
        return projetoRepository.save(projeto); 
    }
}