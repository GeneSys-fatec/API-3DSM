package com.taskmanager.taskmngr_backend.service.Usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

@Service
public class BuscaUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<UsuarioModel> listarTodas() {
        return usuarioRepository.findAll();
    }

    public Optional<UsuarioModel> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }

    public Optional<UsuarioModel> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<UsuarioModel> buscarPorEmails(List<String> emails) {
        return usuarioRepository.findAllByEmails(emails);
    }
}
