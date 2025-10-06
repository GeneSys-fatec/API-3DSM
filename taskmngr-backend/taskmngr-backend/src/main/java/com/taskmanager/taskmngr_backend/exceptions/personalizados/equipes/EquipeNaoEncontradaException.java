package com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes;

import lombok.Getter;

@Getter
public class EquipeNaoEncontradaException extends RuntimeException {
    private String mensagem;

    public EquipeNaoEncontradaException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}