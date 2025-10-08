package com.taskmanager.taskmngr_backend.service.Equipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

@Service
public class ValidacaoEquipeService {
    
    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Validação de Nome Duplicado 
    public void validarNomeDuplicado(String equNome) {
        if (equipeRepository.existsByEquNome(equNome)) {
            throw new IllegalArgumentException("Já existe uma equipe com esse nome!"); // Trocar por Exception!
        }
    }

    // Validação de Usuário Existentes 
    public void validarUsuariosExistentes(List<UsuarioModel> usuarios, List<String> emails) {
        if (usuarios.size() != emails.size()) {
            throw new IllegalArgumentException("Alguns emails não correspondem a usuários existentes!"); // Trocar por Exception!
        }
    }

    // Buscar Usuários pelo Email e Adicioná-los à Equipe (mudar verificação para UsuárioService)
    public Set<UsuarioModel> buscarEValidarMembrosPorEmails(List<String> emails) {
        if (emails == null || emails.isEmpty()) {
            return new HashSet<>();
        }

        List<UsuarioModel> usuarios = usuarioRepository.findAllByEmails(emails);

        validarUsuariosExistentes(usuarios, emails);

        return new HashSet<>(usuarios);
    }
}
