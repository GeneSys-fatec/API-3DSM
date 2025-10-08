package com.taskmanager.taskmngr_backend.model.entidade;

import lombok.Data;

@Data
public class AnexoTarefaModel {
    private String arquivoNome;
    private String arquivoTipo;
    private long arquivoTamanho;
    private String arquivoCaminho;
}
