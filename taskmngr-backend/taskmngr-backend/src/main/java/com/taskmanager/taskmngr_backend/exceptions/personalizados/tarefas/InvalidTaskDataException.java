package com.taskmanager.taskmngr_backend.exceptions.personalizados.tarefas;

public class InvalidTaskDataException extends RuntimeException {
    private String detalhes;

    public InvalidTaskDataException(String mensagem, String detalhes) {
        super(mensagem);
        this.detalhes = detalhes ;
    }

    public String getDetalhes() {
        return detalhes;
    }

}
