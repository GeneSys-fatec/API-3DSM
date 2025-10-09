package com.taskmanager.taskmngr_backend.exceptions.personalizados.comentário;

import lombok.Getter;

@Getter
public class ComentarioEmBrancoException extends RuntimeException {
    private String mensagem;
    public ComentarioEmBrancoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
