package com.taskmanager.taskmngr_backend.service.Token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class ValidaTokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("taskmngr-backend")
                .build()
                .verify(token)
                .getSubject();
    }
}