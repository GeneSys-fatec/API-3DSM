package com.taskmanager.taskmngr_backend.service.Equipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.NomeDeEquipeJaExisteException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

@Service
public class ValidaEquipeService {
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public void validarNomeUnico(String nome, String idAtual) {
        equipeRepository.findByEquNome(nome).ifPresent(equipeExistente -> {
            if (idAtual == null || !equipeExistente.getEquId().equals(idAtual)) {
                throw new NomeDeEquipeJaExisteException("Nome Duplicado", "Uma equipe com o nome '" + nome + "' já existe.");
            }
        });
    }

    public void verificarSeUsuarioPodeAlterarDescricao(EquipeModel equipe, UsuarioModel usuario) {
        if (!equipe.getCriadorId().equals(usuario.getUsuId())) {
            throw new AcessoNaoAutorizadoException("Acesso Não Autorizado", "Apenas o criador da equipe pode alterar a descrição.");
        }
    }

    public void verificarSeUsuarioPodeAlterarNome(EquipeModel equipe, UsuarioModel usuario) {
        if (!equipe.getCriadorId().equals(usuario.getUsuId())) {
            throw new AcessoNaoAutorizadoException("Acesso Não Autorizado", "Apenas o criador da equipe pode alterar o nome da equipe.");
        }
    }

    public void verificarSeUsuarioPodeAlterarMembros(EquipeModel equipe, UsuarioModel usuario) {
        if (!equipe.getCriadorId().equals(usuario.getUsuId())) {
            throw new AcessoNaoAutorizadoException("Acesso Não Autorizado", "Apenas o criador da equipe pode alterar a lista de membros.");
        }
    }

    public void verificarSeUsuarioPodeExcluirEquipe(EquipeModel equipe, UsuarioModel usuario) {
        if (!equipe.getCriadorId().equals(usuario.getUsuId())) {
            throw new AcessoNaoAutorizadoException( "Acesso Não Autorizado", "Apenas o criador da equipe pode excluir a equipe.");
        }
    }


    public List<UsuarioModel> buscarEValidarMembrosPorEmails(List<String> emails) {
        if (emails == null || emails.isEmpty()) {
            return new ArrayList<>();
        }
        List<UsuarioModel> usuariosEncontrados = usuarioRepository.findAllByEmails(emails);
        if (usuariosEncontrados.size() != new HashSet<>(emails).size()) {
            throw new UsuarioNaoEncontradoException("Usuário Não Encontrado", "Um ou mais emails fornecidos não correspondem a usuários existentes.");
        }
        return new ArrayList<>(usuariosEncontrados);
    }
}