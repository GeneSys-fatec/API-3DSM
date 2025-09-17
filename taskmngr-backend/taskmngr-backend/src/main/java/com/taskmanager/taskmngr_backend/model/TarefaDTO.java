package com.taskmanager.taskmngr_backend.model;

import lombok.*; //precisa disso?
import org.springframework.data.annotation.Id;
import org.springframework.hateoas.RepresentationModel; //isso vai aqui?

@Data
public class TarefaDTO extends RepresentationModel<TarefaDTO>{
    @Id
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
