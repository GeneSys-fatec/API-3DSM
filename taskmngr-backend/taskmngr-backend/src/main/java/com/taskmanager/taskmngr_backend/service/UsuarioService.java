package com.taskmanager.taskmngr_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    public List<UsuarioModel> listarTodas() {
        return usuarioRepository.findAll();
    }

    public Optional<UsuarioModel> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }

    public UsuarioModel salvar(UsuarioModel usuario) {
        usuario.setUsuSenha(encoder.encode(usuario.getUsuSenha()));
        return usuarioRepository.save(usuario);
    }

    public UsuarioModel atualizar(UsuarioModel usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deletar(String id) {
        usuarioRepository.deleteById(id);
    }
}
