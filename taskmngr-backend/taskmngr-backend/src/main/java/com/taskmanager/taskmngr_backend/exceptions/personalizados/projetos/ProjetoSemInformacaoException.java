package com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos;

import lombok.Getter;

@Getter
public class ProjetoSemInformacaoException extends RuntimeException {
    private String mensagem;

    public ProjetoSemInformacaoException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}