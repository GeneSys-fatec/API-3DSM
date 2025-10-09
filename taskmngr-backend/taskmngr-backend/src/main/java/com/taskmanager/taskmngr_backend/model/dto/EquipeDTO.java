package com.taskmanager.taskmngr_backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import java.util.List;

@Data
public class EquipeDTO extends RepresentationModel<EquipeDTO> {

    private String equId;
    private String equNome;
    private String equDescricao;
    private String equDataCriacao;
    private String equDataAtualizacao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<UsuarioDTO> equMembros;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> membrosEmails;
}