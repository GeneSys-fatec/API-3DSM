package com.taskmanager.taskmngr_backend.service;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    public ResponseCookie createJWTCookie(String token) {
        return ResponseCookie.from("jwt-token", token) 
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
