package com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes;
import lombok.Getter;

@Getter
public class AcessoNaoAutorizadoException extends RuntimeException {
    private String mensagem;
    public AcessoNaoAutorizadoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}