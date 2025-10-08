package com.taskmanager.taskmngr_backend.service.Equipe;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

@Service
public class EditaEquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BuscaEquipeService buscaEquipeService;

    public EquipeModel updateEquipe(String id, EquipeDTO equipeDTO) {
        
        EquipeModel equipe = buscaEquipeService.getEquipeById(id);
        equipe.setEquNome(equipeDTO.getEquNome());
        equipe.setEquDescricao(equipeDTO.getEquDescricao());

        equipe.getUsuarios().clear();

        // Adiciona Membros na Equipe por E-mail 
        if (equipeDTO.getUsuarioEmails() != null && !equipeDTO.getUsuarioEmails().isEmpty()) {
            List<UsuarioModel> usuarios = (List<UsuarioModel>) usuarioRepository.findAllByEmails(equipeDTO.getUsuarioEmails());
            equipe.setUsuarios(new HashSet<>(usuarios));
        }

        return equipeRepository.save(equipe);
    }
}
