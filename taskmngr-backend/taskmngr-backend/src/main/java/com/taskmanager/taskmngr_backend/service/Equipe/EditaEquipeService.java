package com.taskmanager.taskmngr_backend.service.Equipe;

import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

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
        validacaoEquipeService.validarNomeUnico(dto.getEquNome(), id);

        if (dto.getMembrosEmails() != null) {
            validacaoEquipeService.verificarSeUsuarioECriador(equipe, usuarioLogado, "alterar a lista de membros");

            Set<UsuarioModel> novosMembros = validacaoEquipeService.buscarEValidarMembrosPorEmails(dto.getMembrosEmails());
            equipe.setUsuarios(novosMembros);
        }

        equipe.setEquNome(dto.getEquNome());
        equipe.setEquDescricao(dto.getEquDescricao());

        return equipeRepository.save(equipe);
    }
}