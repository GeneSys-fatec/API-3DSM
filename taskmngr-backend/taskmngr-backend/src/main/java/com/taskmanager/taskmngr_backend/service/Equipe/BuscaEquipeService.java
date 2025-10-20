package com.taskmanager.taskmngr_backend.service.Equipe;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.EquipeNaoEncontradaException;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;

@Service
public class BuscaEquipeService {

    @Autowired
    private EquipeRepository equipeRepository;

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

    public List<EquipeModel> getEquipesPorIdUsuario(String usuarioId) {
        return equipeRepository.findByUsuariosUsuId(usuarioId);
    }

    public List<EquipeModel> getEquipesPorUsuario(UsuarioModel usuario) {
        if (usuario == null) {
            return Collections.emptyList();
        }
        return equipeRepository.findByUsuariosUsuId(usuario.getUsuId());
    }
}
