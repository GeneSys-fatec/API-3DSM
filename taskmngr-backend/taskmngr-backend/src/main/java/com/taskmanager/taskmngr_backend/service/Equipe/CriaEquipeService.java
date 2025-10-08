package com.taskmanager.taskmngr_backend.service.Equipe;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;

@Service
public class CriaEquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private ValidacaoEquipeService validacaoEquipeService;

    public EquipeModel createEquipe(EquipeDTO equipeDTO) {

        validacaoEquipeService.validarNomeDuplicado(equipeDTO.getEquNome());

        EquipeModel equipe = new EquipeModel();
        equipe.setEquNome(equipeDTO.getEquNome());
        equipe.setEquDescricao(equipeDTO.getEquDescricao());

        // Adiciona Membros na Equipe por E-mail 
        if (equipeDTO.getUsuarioEmails() != null && !equipeDTO.getUsuarioEmails().isEmpty()) {
            Set<UsuarioModel> membros = validacaoEquipeService.buscarEValidarMembrosPorEmails(equipeDTO.getUsuarioEmails());
            equipe.setUsuarios(membros);
        }

        return equipeRepository.save(equipe);
    }
}
