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
      
        UsuarioModel usuarioModel = this.usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário Não Encontrado."));

        return User
                .builder()
                .username(usuarioModel.getUsername()) 
                .password(usuarioModel.getPassword()) 
                .roles("USER") 
                .build();
    }
}