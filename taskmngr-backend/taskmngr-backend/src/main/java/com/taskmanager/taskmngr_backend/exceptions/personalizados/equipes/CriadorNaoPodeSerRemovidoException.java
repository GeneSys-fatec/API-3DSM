package com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes;

import lombok.Getter;

@Getter
public class CriadorNaoPodeSerRemovidoException extends RuntimeException {
    private String mensagem;
    public CriadorNaoPodeSerRemovidoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}