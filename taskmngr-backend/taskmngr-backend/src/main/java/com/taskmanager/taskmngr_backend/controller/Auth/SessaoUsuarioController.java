package com.taskmanager.taskmngr_backend.controller.Auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.dto.usuario.UsuarioSessaoDTO;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SessaoUsuarioController {

    @GetMapping("/session")
    public ResponseEntity<UsuarioSessaoDTO> checkSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getPrincipal().equals("usuarioAnonimo")) 
        {
            UsuarioModel usuario = (UsuarioModel) authentication.getPrincipal();

            return ResponseEntity.ok(new UsuarioSessaoDTO(usuario.getUsuNome()));
        }

        return ResponseEntity.ok(new UsuarioSessaoDTO(null));
    }
}
