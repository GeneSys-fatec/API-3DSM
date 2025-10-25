package com.taskmanager.taskmngr_backend.service.Auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.utils.CryptoUtils;

@Service
public class CookieService {

    @Value("${crypto.secret}")
    private String secret;

    public String getSecret() {
        return secret;
    }

    public ResponseCookie createJWTCookie(String token) {
        String encryptedToken = CryptoUtils.encrypt(token, secret);

        return ResponseCookie.from("jwt-token", encryptedToken) 
            .httpOnly(true) 
            .secure(true) 
            .path("/")     
            .maxAge(2 * 60 * 60) 
            .sameSite("None")
            .build();
    }    

    public ResponseCookie createExpiredCookie() {
        return ResponseCookie.from("jwt-token", "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0) 
            .sameSite("None")
            .build();
    }

}
