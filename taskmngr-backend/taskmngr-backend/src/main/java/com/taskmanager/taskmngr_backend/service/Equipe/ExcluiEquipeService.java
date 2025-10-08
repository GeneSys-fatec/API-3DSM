package com.taskmanager.taskmngr_backend.service.Equipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;

@Service
public class ExcluiEquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private BuscaEquipeService buscaEquipeService;

    public void deleteEquipe(String id) {
        EquipeModel equipeParaDeletar = buscaEquipeService.getEquipeById(id);
        equipeRepository.delete(equipeParaDeletar);
    }
}
