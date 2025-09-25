package com.taskmanager.taskmngr_backend.model.dto;

import org.springframework.hateoas.RepresentationModel;
import lombok.Data;

@Data
public class ProjetoDTO extends RepresentationModel<ProjetoDTO>{
    private String proj_id;
    private String proj_nome;
    private String proj_descricao;
    private String proj_status;
    private String proj_dataCriacao;
    private String proj_dataAtualizacao;
    private String equ_id;
    private String equ_nome;

    

}
