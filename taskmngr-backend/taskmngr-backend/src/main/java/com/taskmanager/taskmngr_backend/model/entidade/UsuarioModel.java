package com.taskmanager.taskmngr_backend.model.entidade;

import java.util.Collection;
import java.util.Collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
@Document(collection = "Usuarios")
public class UsuarioModel implements UserDetails {
    @Id
    private String usuId;
    private String usuNome;
    private String usuEmail;
    private String usuCaminhoFoto;
    private String usuSenha;
    private String usuDataCriacao;
    private String usuDataAtualizacao;
    // private List<ProjetoModel> projetos;
    // private List<EquipeModel> equipes;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return usuSenha;
    }

    @Override
    public String getUsername() {
        return usuEmail;
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