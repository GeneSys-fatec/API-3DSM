package com.taskmanager.taskmngr_backend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

public class UsuarioDetalhesService implements UserDetailsService {
    private UsuarioRepository usuarioRepository;

    public UsuarioDetalhesService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByUsuEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }

}
