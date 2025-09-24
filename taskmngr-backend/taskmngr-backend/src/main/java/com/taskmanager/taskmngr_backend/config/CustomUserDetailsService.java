package com.taskmanager.taskmngr_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.model.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // ---- AQUI ESTÁ A CORREÇÃO ----
        // DE: this.usuarioRepository.findBy(username)
        // PARA: this.usuarioRepository.findByUsuEmail(username)
        UsuarioModel usuarioModel = this.usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário Não Encontrado."));

        return User
                .builder()
                .username(usuarioModel.getUsername()) // getUsername() é o padrão do UserDetails
                .password(usuarioModel.getPassword()) // getPassword() é o padrão do UserDetails
                .roles("USER") // Você pode adicionar lógicas para roles diferentes aqui
                .build();
    }
}