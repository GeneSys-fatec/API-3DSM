package com.taskmanager.taskmngr_backend.controller.Auth;

import org.springframework.http.HttpHeaders;       
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.dto.ResponseDTO;
import com.taskmanager.taskmngr_backend.model.dto.usuario.UsuarioLoginDTO;
import com.taskmanager.taskmngr_backend.service.Auth.CookieService;
import com.taskmanager.taskmngr_backend.service.Auth.LoginUsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginUsuarioController {
    
    private final LoginUsuarioService loginUsuarioService;
    private final CookieService cookieService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UsuarioLoginDTO body) {
        String nomeUsuario = loginUsuarioService.loginUsuario(body);
        String token = loginUsuarioService.generateTokenForUser(body);
        ResponseDTO response = new ResponseDTO(nomeUsuario);
        ResponseCookie cookie = cookieService.createJWTCookie(token);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }
}
