package com.taskmanager.taskmngr_backend.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.config.TokenService;
import com.taskmanager.taskmngr_backend.model.UsuarioModel;
import com.taskmanager.taskmngr_backend.model.dto.ResponseDTO;
import com.taskmanager.taskmngr_backend.model.dto.UsuarioDTO;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UsuarioDTO body) {
        
        Optional<UsuarioModel> usuarioOpt = this.usuarioRepository.findByEmail(body.getUsu_email());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        UsuarioModel usuario = usuarioOpt.get();
        if (passwordEncoder.matches(body.getUsu_senha(), usuario.getPassword())) {
            String token = this.tokenService.generateToken(usuario);
            return ResponseEntity.ok(new ResponseDTO(usuario.getUsu_nome(), token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha inválida.");
    }

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrar(@RequestBody UsuarioDTO body) {
        
        Optional<UsuarioModel> usuarioOpt = this.usuarioRepository.findByEmail(body.getUsu_email());

        if (usuarioOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Este e-mail já está em uso.");
        }

        UsuarioModel novoUsuario = new UsuarioModel();
        novoUsuario.setUsu_senha(passwordEncoder.encode(body.getUsu_senha()));
        novoUsuario.setUsu_email(body.getUsu_email());
        novoUsuario.setUsu_nome(body.getUsu_nome());
        this.usuarioRepository.save(novoUsuario);

        String token = this.tokenService.generateToken(novoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(novoUsuario.getUsu_nome(), token));
    }
}