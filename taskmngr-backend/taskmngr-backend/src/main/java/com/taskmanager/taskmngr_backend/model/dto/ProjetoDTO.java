package com.taskmanager.taskmngr_backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //recebe os dados de equipe
    private String equipeId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY) //envia os dados de equipe
    private String equId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String equNome;

    

}
