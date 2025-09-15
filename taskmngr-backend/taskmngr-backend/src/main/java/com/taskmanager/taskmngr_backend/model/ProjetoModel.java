package com.taskmanager.taskmngr_backend.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class ProjetoModel {
    @Id
    private String proj_id;
    private String proj_nome;
    private String proj_descricao;
    private String proj_status;
    private String proj_dataCriacao;
    private String proj_dataAtualizacao;
    private String equ_id;
    private String equ_nome;
    private List<UsuarioModel> usuarios;

    public String getProj_id() {
        return proj_id;
    }

    public String getProj_nome() {
        return proj_nome;
    }

    public String getProj_dataCriacao() {
        return proj_dataCriacao;
    }

    public String getProj_dataAtualizacao() {
        return proj_dataAtualizacao;
    }

    public String getProj_descricao() {
        return proj_descricao;
    }

    public String getProj_status() {
        return proj_status;
    }

    public String getEqu_id() {
        return equ_id;
    }

    public String getEqu_nome() {
        return equ_nome;
    }

    public List<UsuarioModel> getUsuarios() {
        return usuarios;
    }

    
}
