package com.taskmanager.taskmngr_backend.exceptions.personalizados.tarefas;

import lombok.Getter;

@Getter
public class AnexoTamanhoExcedente extends RuntimeException {
    private String mensagem;

    public AnexoTamanhoExcedente(String titulo, String mensagem) {
        super(titulo);
        this.mensagem = mensagem;
    }
}
