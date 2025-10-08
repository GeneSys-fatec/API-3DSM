package com.taskmanager.taskmngr_backend.model.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;

@Data
public class UsuarioDTO extends RepresentationModel<UsuarioDTO>{
    private String usuId;
    private String usuNome;
    private String usuEmail;
    private String usuCaminhoFoto;
    private String usuSenha;
    private String usuDataCriacao;
    private String usuDataAtualizacao;
}
