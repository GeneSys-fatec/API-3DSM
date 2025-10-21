package com.taskmanager.taskmngr_backend.controller.Auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.dto.ResponseDTO;
import com.taskmanager.taskmngr_backend.model.dto.UsuarioLoginDTO;
import com.taskmanager.taskmngr_backend.service.Auth.LogarUsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LogarUsuarioController {
    
    private final LogarUsuarioService logarUsuarioService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UsuarioLoginDTO body) {
        ResponseDTO response = logarUsuarioService.loginUsuario(body);
        return ResponseEntity.ok(response);
    }
}
