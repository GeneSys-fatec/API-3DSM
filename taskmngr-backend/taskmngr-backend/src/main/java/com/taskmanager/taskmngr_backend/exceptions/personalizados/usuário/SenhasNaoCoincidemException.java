package com.taskmanager.taskmngr_backend.exceptions.personalizados.usuário;

import lombok.Getter;

@Getter
public class SenhasNaoCoincidemException extends RuntimeException {
    private String mensagem;

    public SenhasNaoCoincidemException(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
