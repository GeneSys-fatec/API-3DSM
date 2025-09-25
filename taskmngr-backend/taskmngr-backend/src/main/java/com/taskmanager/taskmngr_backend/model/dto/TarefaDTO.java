package com.taskmanager.taskmngr_backend.model.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data
public class TarefaDTO extends RepresentationModel<TarefaDTO>{
    private String tar_id;
    private String tar_titulo;
    private String tar_descricao;
    private String tar_prazo;
    private String tar_status;
    private String tar_prioridade;
    private String tar_anexo;
    private String tar_dataCriacao;
    private String tar_dataAtualizacao;
    private String usu_id;
    private String usu_nome;
    private String proj_id;
    private String proj_nome;
}