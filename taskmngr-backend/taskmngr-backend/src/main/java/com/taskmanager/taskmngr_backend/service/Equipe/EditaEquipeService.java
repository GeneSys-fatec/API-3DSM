package com.taskmanager.taskmngr_backend.service.Equipe;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;

@Service
public class EditaEquipeService {
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private BuscaEquipeService buscaEquipeService;
    @Autowired
    private ValidacaoEquipeService validacaoEquipeService;

    public EquipeModel editar(String id, EquipeDTO dto, UsuarioModel usuarioLogado) {
        EquipeModel equipe = buscaEquipeService.getEquipeById(id);

        if (dto.getEquNome() != null && !dto.getEquNome().equals(equipe.getEquNome())) {
            validacaoEquipeService.verificarSeUsuarioPodeAlterarNome(equipe, usuarioLogado);
            validacaoEquipeService.validarNomeUnico(dto.getEquNome(), id);
            equipe.setEquNome(dto.getEquNome());
        }

        if (dto.getEquDescricao() != null && !dto.getEquDescricao().equals(equipe.getEquDescricao())) {
            validacaoEquipeService.verificarSeUsuarioPodeAlterarDescricao(equipe, usuarioLogado);
            equipe.setEquDescricao(dto.getEquDescricao());
        }

        if (dto.getMembrosEmails() != null) {
            validacaoEquipeService.verificarSeUsuarioPodeAlterarMembros(equipe, usuarioLogado);
            List<UsuarioModel> novosMembros = validacaoEquipeService.buscarEValidarMembrosPorEmails(dto.getMembrosEmails());
            equipe.setUsuarios(novosMembros);
        }

        return equipeRepository.save(equipe);
    }
}