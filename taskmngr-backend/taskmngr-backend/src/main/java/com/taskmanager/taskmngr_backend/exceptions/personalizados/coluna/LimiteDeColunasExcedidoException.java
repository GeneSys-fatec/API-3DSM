package com.taskmanager.taskmngr_backend.exceptions.personalizados.coluna;

public class LimiteDeColunasExcedidoException extends RuntimeException {
    private final String mensagem;

    public LimiteDeColunasExcedidoException(String message, String mensagem) {
        super(message);
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}