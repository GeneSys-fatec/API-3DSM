package com.taskmanager.taskmngr_backend.service.Token;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;

@Service
public class CriaTokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UsuarioModel usuarioModel) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("taskmngr-backend")
                .withSubject(usuarioModel.getUsuEmail())
                .withExpiresAt(this.generateExpirationDate())
                .sign(algorithm);
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}