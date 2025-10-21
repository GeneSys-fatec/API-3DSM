package com.taskmanager.taskmngr_backend.service.Auth;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário.EmailJaCadastradoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário.SenhasNaoCoincidemException;
import com.taskmanager.taskmngr_backend.model.dto.ResponseDTO;
import com.taskmanager.taskmngr_backend.model.dto.UsuarioCadastroDTO;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;
import com.taskmanager.taskmngr_backend.service.TokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CriaUsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public ResponseDTO cadastrarUsuario(UsuarioCadastroDTO body) {
        Optional<UsuarioModel> usuarioOpt = this.usuarioRepository.findByEmail(body.getUsuEmail().toLowerCase());

        if (usuarioOpt.isPresent()) {
            throw new EmailJaCadastradoException("Email já cadastrado.", "Este email já está sendo usado em outra conta.");
        }

        if (!body.getUsuSenha().equals(body.getUsuConfirmarSenha())) {
            throw new SenhasNaoCoincidemException("Confirmação de senha", "As senhas não coincidem.");
        }

        UsuarioModel novoUsuario = new UsuarioModel();
        novoUsuario.setUsuSenha(passwordEncoder.encode(body.getUsuSenha()));
        novoUsuario.setUsuEmail(body.getUsuEmail().toLowerCase());
        novoUsuario.setUsuNome(body.getUsuNome());
        this.usuarioRepository.save(novoUsuario);

        String token = this.tokenService.generateToken(novoUsuario);

        return new ResponseDTO(novoUsuario.getUsuNome(), token);
    }
}
