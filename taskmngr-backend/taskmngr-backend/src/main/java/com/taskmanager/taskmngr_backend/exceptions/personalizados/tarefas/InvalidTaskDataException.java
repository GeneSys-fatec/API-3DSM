package com.taskmanager.taskmngr_backend.exceptions.personalizados.tarefas;

import lombok.Getter;

@Getter
public class InvalidTaskDataException extends RuntimeException {
    private String mensagem;

    public InvalidTaskDataException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
