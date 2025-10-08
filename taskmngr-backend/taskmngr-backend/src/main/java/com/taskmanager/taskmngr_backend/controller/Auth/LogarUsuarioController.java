package com.taskmanager.taskmngr_backend.controller.Auth;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.autenticação.CredenciaisInvalidasException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.dto.ResponseDTO;
import com.taskmanager.taskmngr_backend.model.dto.UsuarioDTO;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;
import com.taskmanager.taskmngr_backend.service.TokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LogarUsuarioController {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UsuarioDTO body) {
        
        Optional<UsuarioModel> usuarioOpt = this.usuarioRepository.findByEmail(body.getUsuEmail());

        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.", "Email não encontrado.");
        }

        UsuarioModel usuario = usuarioOpt.get();
        if (passwordEncoder.matches(body.getUsuSenha(), usuario.getPassword())) {
            String token = this.tokenService.generateToken(usuario);
            return ResponseEntity.ok(new ResponseDTO(usuario.getUsuNome(), token));
        }

        throw new CredenciaisInvalidasException("Credenciais inválidas.", "Senha incorreta.");
    }
}
