package com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes;
import lombok.Getter;

@Getter
public class NomeDeEquipeJaExisteException extends RuntimeException {
    private String mensagem;
    public NomeDeEquipeJaExisteException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}