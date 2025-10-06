package com.taskmanager.taskmngr_backend.service;

import com.taskmanager.taskmngr_backend.model.EquipeModel;
import com.taskmanager.taskmngr_backend.model.UsuarioModel;
import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.EquipeNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class EquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public EquipeModel createEquipe(EquipeDTO equipeDTO) {
        EquipeModel equipe = new EquipeModel();
        equipe.setNome(equipeDTO.getEqu_nome());
        equipe.setDescricao(equipeDTO.getEqu_descricao());
        if (equipeDTO.getUsuarioIds() != null && !equipeDTO.getUsuarioIds().isEmpty()) {
            List<UsuarioModel> usuarios = (List<UsuarioModel>) usuarioRepository.findAllById(equipeDTO.getUsuarioIds());
            equipe.setUsuarios(new HashSet<>(usuarios));
        }

        return equipeRepository.save(equipe);
    }

    public List<EquipeModel> getAllEquipes() {
        return equipeRepository.findAll();
    }

    public EquipeModel getEquipeById(String id) {
        return equipeRepository.findById(id)
                .orElseThrow(() -> new EquipeNaoEncontradaException(
                        "Equipe Não Encontrada",
                        "A equipe com o ID " + id + " não foi encontrada no sistema."
                ));
    }

    public EquipeModel updateEquipe(String id, EquipeDTO equipeDTO) {
        EquipeModel equipe = getEquipeById(id);
        equipe.setNome(equipeDTO.getEqu_nome());
        equipe.setDescricao(equipeDTO.getEqu_descricao());

        equipe.getUsuarios().clear();
        if (equipeDTO.getUsuarioIds() != null && !equipeDTO.getUsuarioIds().isEmpty()) {
            List<UsuarioModel> usuarios = (List<UsuarioModel>) usuarioRepository.findAllById(equipeDTO.getUsuarioIds());
            equipe.setUsuarios(new HashSet<>(usuarios));
        }

        return equipeRepository.save(equipe);
    }

    public void deleteEquipe(String id) {
        EquipeModel equipeParaDeletar = getEquipeById(id);

        equipeRepository.delete(equipeParaDeletar);
    }
}