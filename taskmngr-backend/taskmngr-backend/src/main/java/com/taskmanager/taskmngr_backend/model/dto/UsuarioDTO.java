package com.taskmanager.taskmngr_backend.model.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;

@Data
public class UsuarioDTO extends RepresentationModel<UsuarioDTO>{
    private String usu_id;
    private String usu_nome;
    private String usu_email;
    private String usu_caminhoFoto;
    private String usu_senha;
    private String usu_dataCriacao;
    private String usu_dataAtualizacao;
}
