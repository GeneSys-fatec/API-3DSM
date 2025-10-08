package com.taskmanager.taskmngr_backend.model.dto;

import org.springframework.hateoas.RepresentationModel;
import lombok.Data;

@Data
public class ProjetoDTO extends RepresentationModel<ProjetoDTO>{
    private String projId;
    private String projNome;
    private String projDescricao;
    private String projStatus;
    private String projDataCriacao;
    private String projDataAtualizacao;
    private String equ_id;
    private String equ_nome;

    

}
