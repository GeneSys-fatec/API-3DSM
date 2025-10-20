package com.taskmanager.taskmngr_backend.model.dto;

import lombok.*;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.taskmanager.taskmngr_backend.model.entidade.AnexoTarefaModel;

@Data
public class TarefaDTO extends RepresentationModel<TarefaDTO>{
    private String tarId;
    private String tarTitulo;
    private String tarDescricao;
    private String tarPrazo;
    private String tarStatus;
    private String tarPrioridade;
    private List<AnexoTarefaModel> tarAnexos;
    private String tarDataCriacao;
    private String tarDataAtualizacao;
    private String tarDataConclusao;
    private Boolean concluidaNoPrazo; // true = dentro do prazo, false = fora
    private String usuId;
    private String usuNome;
    private String projId;
    private String projNome;
}