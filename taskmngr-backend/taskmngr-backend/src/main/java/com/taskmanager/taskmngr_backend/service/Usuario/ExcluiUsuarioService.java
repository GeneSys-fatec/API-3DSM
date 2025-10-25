package com.taskmanager.taskmngr_backend.service.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

@Service
public class ExcluiUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public void deletar(String id) {
        usuarioRepository.deleteById(id);
    }
}
