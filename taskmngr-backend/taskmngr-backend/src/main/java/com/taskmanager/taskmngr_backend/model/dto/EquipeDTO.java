package com.taskmanager.taskmngr_backend.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;
import lombok.Data;

@Data
public class EquipeDTO extends RepresentationModel<EquipeDTO> {

    private String equId;
    private String equNome;
    private String equDescricao;
    private String equDataCriacao;
    private String equDataAtualizacao;
    private List<UsuarioDTO> equMembros;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> usuarioIds;
    private List<String> usuarioEmails;
}
