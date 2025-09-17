package com.taskmanager.taskmngr_backend.model;

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
    // private List<ProjetoModel> projetos;
    // private List<EquipeModel> equipes;

    public String getUsu_id() {
        return usu_id;
    }
    
    public String getUsu_nome() {
        return usu_nome;
    }

    public String getUsu_email() {
        return usu_email;
    }

    public String getUsu_caminhoFoto() {
        return usu_caminhoFoto;
    }

    public String getUsu_senha() {
        return usu_senha;
    }

    public String getUsu_dataCriacao() {
        return usu_dataCriacao;
    }

    public String getUsu_dataAtualizacao() {
        return usu_dataAtualizacao;
    }

    // public List<ProjetoModel> getProjetos() {
    //     return projetos; 
    // }

    
}
