package com.taskmanager.taskmngr_backend.service.Equipe;

import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class CriaEquipeService {
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private ValidacaoEquipeService validacaoEquipeService;

    public EquipeModel criar(EquipeDTO dto, UsuarioModel criador) {
        validacaoEquipeService.validarNomeUnico(dto.getEquNome(), null);
        Set<UsuarioModel> membros = validacaoEquipeService.buscarEValidarMembrosPorEmails(dto.getMembrosEmails());

        EquipeModel equipe = new EquipeModel();
        equipe.setEquNome(dto.getEquNome());
        equipe.setEquDescricao(dto.getEquDescricao());
        equipe.setCriadorId(criador.getUsuId());
        equipe.setUsuarios(membros);

        return equipeRepository.save(equipe);
    }
}