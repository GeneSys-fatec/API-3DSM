package com.taskmanager.taskmngr_backend.service.Auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.autenticação.CredenciaisInvalidasException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.dto.usuario.UsuarioLoginDTO;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;
import com.taskmanager.taskmngr_backend.service.TokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginUsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    private UsuarioModel validarCredenciais(UsuarioLoginDTO body) {
        UsuarioModel usuario = this.usuarioRepository.findByEmail(body.getUsuEmail().toLowerCase())
        .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado.", "Email não encontrado."));

        if (!passwordEncoder.matches(body.getUsuSenha(), usuario.getPassword())) {
            throw new CredenciaisInvalidasException("Credenciais inválidas.", "Senha incorreta.");
        }
        
        return usuario;
    }

    public String loginUsuario(UsuarioLoginDTO body) {
        UsuarioModel usuario = this.validarCredenciais(body);
        return usuario.getUsuNome();
    }

    public String generateTokenForUser(UsuarioLoginDTO body) {
        UsuarioModel usuario = this.validarCredenciais(body);
        return this.tokenService.generateToken(usuario);
    }
}
