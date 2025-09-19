package com.taskmanager.taskmngr_backend.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UsuarioModel implements UserDetails {
    @Id
    private String usu_id;
    private String usu_nome;
    private String usu_email;
    private String usu_caminhoFoto;
    private String usu_senha;
    private String usu_dataCriacao;
    private String usu_dataAtualizacao;
    // private List<ProjetoModel> projetos;
    // private List<EquipeModel> equipes;

    public String getUsu_id() {
        return usu_id;
    }

    public void setUsu_id(String usu_id) {
        this.usu_id = usu_id;
    }

    public String getUsu_nome() {
        return usu_nome;
    }

    public void setUsu_nome(String usu_nome) {
        this.usu_nome = usu_nome;
    }

    public String getUsu_email() {
        return usu_email;
    }

    public void setUsu_email(String usu_email) {
        this.usu_email = usu_email;
    }

    public String getUsu_caminhoFoto() {
        return usu_caminhoFoto;
    }

    public void setUsu_caminhoFoto(String usu_caminhoFoto) {
        this.usu_caminhoFoto = usu_caminhoFoto;
    }

    public String getUsu_senha() {
        return usu_senha;
    }

    public void setUsu_senha(String usu_senha) {
        this.usu_senha = usu_senha;
    }

    public String getUsu_dataCriacao() {
        return usu_dataCriacao;
    }

    public void setUsu_dataCriacao(String usu_dataCriacao) {
        this.usu_dataCriacao = usu_dataCriacao;
    }

    public String getUsu_dataAtualizacao() {
        return usu_dataAtualizacao;
    }

    public void setUsu_dataAtualizacao(String usu_dataAtualizacao) {
        this.usu_dataAtualizacao = usu_dataAtualizacao;
    }

    // public List<ProjetoModel> getProjetos() {
    //     return projetos;
    // }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return usu_senha;
    }

    @Override
    public String getUsername() {
        return usu_email;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
