package com.taskmanager.taskmngr_backend.service.Equipe;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.EquipeNaoEncontradaException;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;

@Service
public class BuscaEquipeService {
    
    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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

    //logica para a rota de listar as equipes de um usuario especifico
    //a rota só vai ser usada para adjuar no desenvolvimento
    public List<EquipeModel> getEquipesPorIdUsuario(String usuarioId) {
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado", "Nenhum usuário encontrado com o ID: " + usuarioId));

        return equipeRepository.findByUsuariosContaining(usuario);
    }

    public List<EquipeModel> getEquipesPorUsuario(UsuarioModel usuario) {
        if (usuario == null) {
            return Collections.emptyList();
        }
        return equipeRepository.findByUsuariosContaining(usuario);
    }
}
