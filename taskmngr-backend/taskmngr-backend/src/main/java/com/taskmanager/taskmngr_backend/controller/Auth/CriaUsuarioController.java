package com.taskmanager.taskmngr_backend.controller.Auth;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário.EmailJaCadastradoException;
import com.taskmanager.taskmngr_backend.model.dto.ResponseDTO;
import com.taskmanager.taskmngr_backend.model.dto.UsuarioCadastroDTO;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;
import com.taskmanager.taskmngr_backend.service.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class CriaUsuarioController {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrar(@RequestBody @Valid UsuarioCadastroDTO body) {
        
        Optional<UsuarioModel> usuarioOpt = this.usuarioRepository.findByEmail(body.getUsuEmail());

        if (usuarioOpt.isPresent()) {
            throw new EmailJaCadastradoException("Email já cadastrado.", "Este email já está sendo usado em outra conta.");
        }

        UsuarioModel novoUsuario = new UsuarioModel();
        novoUsuario.setUsuSenha(passwordEncoder.encode(body.getUsuSenha()));
        novoUsuario.setUsuEmail(body.getUsuEmail());
        novoUsuario.setUsuNome(body.getUsuNome());
        this.usuarioRepository.save(novoUsuario);

        String token = this.tokenService.generateToken(novoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(novoUsuario.getUsuNome(), token));
    
    }
}
