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

    public String getProj_id() {return proj_id;}
    public void setProj_id(String proj_id) {this.proj_id = proj_id;}

    public String getProj_nome() {return proj_nome;}
    public void setProj_nome(String proj_nome) {this.proj_nome = proj_nome;}
    
    public String getProj_dataCriacao() {return proj_dataCriacao;}
    public void setProj_dataCriacao(String proj_dataCriacao) {this.proj_dataCriacao = proj_dataCriacao;}
    
    public String getProj_dataAtualizacao() {return proj_dataAtualizacao;}
    public void setProj_dataAtualizacao(String proj_dataAtualizacao) {this.proj_dataAtualizacao = proj_dataAtualizacao;}
    
    public String getProj_descricao() {return proj_descricao;}
    public void setProj_descricao(String proj_descricao) {this.proj_descricao = proj_descricao;}
    
    public String getProj_status() {return proj_status;}
    public void setProj_status(String proj_status) {this.proj_status = proj_status;}

    public String getEqu_id() {return equ_id;}
    public void setEqu_id(String equ_id) {this.equ_id = equ_id;}

    public String getEqu_nome() {return equ_nome;}
    public void setEqu_nome(String equ_nome) {this.equ_nome = equ_nome;}

    public List<UsuarioModel> getUsuarios() {return usuarios;}
    public void setUsuarios(List<UsuarioModel> usuarios) {this.usuarios = usuarios;}

}
