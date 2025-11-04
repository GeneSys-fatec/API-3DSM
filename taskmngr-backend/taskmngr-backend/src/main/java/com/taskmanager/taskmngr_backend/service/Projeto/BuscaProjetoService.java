package com.taskmanager.taskmngr_backend.service.Projeto;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;
import com.taskmanager.taskmngr_backend.repository.ProjetoRepository;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class BuscaProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<ProjetoModel> listarPorUsuario(UsuarioModel usuario) {
        if (usuario == null) {
            return Collections.emptyList();
        }

        List<EquipeModel> equipesDoUsuario = equipeRepository.findByUsuarioIds(usuario.getUsuId());

        if (equipesDoUsuario.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> idsDasEquipes = equipesDoUsuario.stream()
                .map(EquipeModel::getEquId)
                .collect(Collectors.toList());

        return projetoRepository.findByEquipeIdIn(idsDasEquipes);
    }

    public List<UsuarioModel> buscarMembrosDoProjeto(String projId) {
        ProjetoModel projeto = projetoRepository.findById(projId)
                .orElseThrow(() -> new ProjetoNaoEncontradoException(
                        "Projeto não encontrado",
                        "Não foi possível buscar membros pois o projeto com id " + projId + " não foi encontrado"));


        String equipeId = projeto.getEquipeId();
        if (equipeId == null) {
            return Collections.emptyList();
        }

        EquipeModel equipe = equipeRepository.findById(equipeId).orElse(null);

        if (equipe != null && equipe.getUsuarioIds() != null && !equipe.getUsuarioIds().isEmpty()) {
            return usuarioRepository.findAllById(equipe.getUsuarioIds());
        }

        return Collections.emptyList();
    }

    public List<ProjetoModel> listarTodas() {
        return projetoRepository.findAll();
    }

    public Optional<ProjetoModel> buscarPorId(String id) {
        return projetoRepository.findById(id);
    }
}