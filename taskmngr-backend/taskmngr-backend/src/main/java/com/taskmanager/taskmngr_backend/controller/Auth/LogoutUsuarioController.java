package com.taskmanager.taskmngr_backend.controller.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.service.CookieService;

@RestController
@RequestMapping("/auth")
public class LogoutUsuarioController {

   @Autowired
    private CookieService cookieService;
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        
        ResponseCookie cookieExpirado = cookieService.createExpiredCookie();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookieExpirado.toString()).body("Usuário deslogado com sucesso!");
    }
}
