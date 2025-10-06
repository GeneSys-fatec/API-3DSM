package com.taskmanager.taskmngr_backend.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;
import lombok.Data;

@Data
public class EquipeDTO extends RepresentationModel<EquipeDTO> {

    private String equ_id;
    private String equ_nome;
    private String equ_descricao;
    private String equ_dataCriacao;
    private String equ_dataAtualizacao;
    private List<UsuarioDTO> equ_membros;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> usuarioIds;
}
