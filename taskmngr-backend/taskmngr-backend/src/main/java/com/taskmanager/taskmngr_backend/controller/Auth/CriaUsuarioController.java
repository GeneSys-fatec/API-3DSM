package com.taskmanager.taskmngr_backend.controller.Auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.dto.ResponseDTO;
import com.taskmanager.taskmngr_backend.model.dto.UsuarioCadastroDTO;
import com.taskmanager.taskmngr_backend.service.Auth.CriaUsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class CriaUsuarioController {

    private final CriaUsuarioService criaUsuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity cadastrar(@RequestBody @Valid UsuarioCadastroDTO body) {
        ResponseDTO response = criaUsuarioService.cadastrarUsuario(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
