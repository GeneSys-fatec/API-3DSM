package com.taskmanager.taskmngr_backend.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.autenticação.TokenCriacaoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.autenticação.TokenInvalidoException;
import com.taskmanager.taskmngr_backend.model.UsuarioModel;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    // Cria o Token quando o usuário faz login.
    public String generateToken(UsuarioModel usuarioModel) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); // Algoritmo de assinatura.

            String token = JWT.create()
                    .withIssuer("taskmngr-backend")
                    .withSubject(usuarioModel.getUsu_email())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new TokenCriacaoException("Erro ao gerar token.", exception.getMessage());
        }
    }

    // Valida Tokens recebidos.
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("taskmngr-backend")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new TokenInvalidoException("Token inválido.", "O token enviado não é válido ou expirou.");
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
