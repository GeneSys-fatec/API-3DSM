package com.taskmanager.taskmngr_backend.model.entidade;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Tarefas")
public class TarefaModel {
    @Id

    private String tarId;
    private String tarTitulo;
    private String tarDescricao;
    private String tarPrazo;
    private String tarStatus;
    private String tarPrioridade;
    private List<AnexoTarefaModel> tarAnexos;
    private String tarDataCriacao;
    private String tarDataAtualizacao;
    private String usuId;
    private String usuNome;
    private String projId;
    private String projNome;
}