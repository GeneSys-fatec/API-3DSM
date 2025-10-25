package com.taskmanager.taskmngr_backend.service.Equipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;

@Service
public class ExcluiEquipeService {
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private BuscaEquipeService buscaEquipeService;
    @Autowired
    private ValidaEquipeService validacaoEquipeService;

    public void excluir(String id, UsuarioModel usuarioLogado) {
        EquipeModel equipe = buscaEquipeService.getEquipeById(id);
        validacaoEquipeService.verificarSeUsuarioPodeExcluirEquipe(equipe, usuarioLogado);
        equipeRepository.delete(equipe);
    }
}