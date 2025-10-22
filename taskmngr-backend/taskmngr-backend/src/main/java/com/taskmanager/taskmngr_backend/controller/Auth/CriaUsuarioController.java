package com.taskmanager.taskmngr_backend.controller.Auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.dto.ResponseDTO;
import com.taskmanager.taskmngr_backend.model.dto.usuario.UsuarioCadastroDTO;
import com.taskmanager.taskmngr_backend.service.Auth.CriaUsuarioService;
import com.taskmanager.taskmngr_backend.service.CookieService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class CriaUsuarioController {

    private final CriaUsuarioService criaUsuarioService;
    private final CookieService cookieService;

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrar(@RequestBody @Valid UsuarioCadastroDTO body) {
        String token = criaUsuarioService.cadastrarUsuario(body);
        ResponseDTO response = new ResponseDTO(body.getUsuNome());
        ResponseCookie cookie = cookieService.createJWTCookie(token);
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }
}
