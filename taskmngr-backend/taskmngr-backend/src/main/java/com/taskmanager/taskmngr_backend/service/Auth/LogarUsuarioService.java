package com.taskmanager.taskmngr_backend.service.Auth;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.autenticação.CredenciaisInvalidasException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário.UsuarioNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.dto.ResponseDTO;
import com.taskmanager.taskmngr_backend.model.dto.UsuarioLoginDTO;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;
import com.taskmanager.taskmngr_backend.service.TokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogarUsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public ResponseDTO loginUsuario(UsuarioLoginDTO body) {
        Optional<UsuarioModel> usuarioOpt = this.usuarioRepository.findByEmail(body.getUsuEmail().toLowerCase());

        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.", "Email não encontrado.");
        }

        UsuarioModel usuario = usuarioOpt.get();
            
        if (!passwordEncoder.matches(body.getUsuSenha(), usuario.getPassword())) {
            throw new CredenciaisInvalidasException("Credenciais inválidas.", "Senha incorreta.");
        }

        String token = this.tokenService.generateToken(usuario);

        return new ResponseDTO(usuario.getUsuNome(), token);
       
    }
}
