package com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes;

import lombok.Getter;

@Getter
public class UsuarioNaoEMembroException extends RuntimeException {
    private String mensagem;
    public UsuarioNaoEMembroException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}