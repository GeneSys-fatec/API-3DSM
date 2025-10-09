package com.taskmanager.taskmngr_backend.service.Equipe;

import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcluiEquipeService {
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private BuscaEquipeService buscaEquipeService;
    @Autowired
    private ValidacaoEquipeService validacaoEquipeService;

    // CORRIGIDO: Assinatura correta
    public void excluir(String id, UsuarioModel usuarioLogado) {
        EquipeModel equipe = buscaEquipeService.getEquipeById(id);
        validacaoEquipeService.verificarSeUsuarioECriador(equipe, usuarioLogado, "excluir a equipe");
        equipeRepository.delete(equipe);
    }
}