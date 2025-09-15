package com.taskmanager.taskmngr_backend.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class UsuarioModel {
    @Id
    private String usu_id;
    private String usu_nome;
    private String usu_email;
    private String usu_caminhoFoto;
    private String usu_senha;
    private String usu_dataCriacao; 
    private String usu_dataAtualizacao;
    private List<ProjetoModel> projetos;

    
}
