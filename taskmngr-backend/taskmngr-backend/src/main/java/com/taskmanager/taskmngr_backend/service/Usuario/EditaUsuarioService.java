package com.taskmanager.taskmngr_backend.service.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

@Service
public class EditaUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuarioModel salvar(UsuarioModel usuario) {
        usuario.setUsuSenha(encoder.encode(usuario.getUsuSenha()));
        return usuarioRepository.save(usuario);
    }

    public UsuarioModel atualizar(UsuarioModel usuario) {
        return usuarioRepository.save(usuario);
    }
}
