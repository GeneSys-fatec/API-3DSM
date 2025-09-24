package com.taskmanager.taskmngr_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.autenticação.CredenciaisInvalidasException;
import com.taskmanager.taskmngr_backend.model.dto.ErroRespostaDTO;

@ControllerAdvice
public class ManipuladorGlobal {
    
    // CredenciaisInvalidasException
    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErroRespostaDTO> manipularCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        ErroRespostaDTO erro = new ErroRespostaDTO(ex.getMessage(), "Credenciais Inválidas.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    
}
