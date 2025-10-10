package com.taskmanager.taskmngr_backend.service.Equipe;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.NomeDeEquipeJaExisteException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ValidacaoEquipeService {
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

    public void verificarSeUsuarioECriador(EquipeModel equipe, UsuarioModel usuario, String acao) {
        if (!equipe.getCriadorId().equals(usuario.getUsuId())) {
            throw new AcessoNaoAutorizadoException("Acesso Não Autorizado", "Apenas o criador da equipe pode " + acao + ".");
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