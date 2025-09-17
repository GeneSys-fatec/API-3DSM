package com.taskmanager.taskmngr_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection= "tarefas")
public class TarefaModel {
    @Id
    private String tar_id;
    private String tar_titulo;
    private String tar_descricao;
    private String tar_prazo;
    private String tar_status;
    private String tar_prioridade;
    private String tar_anexo;
    private String tar_dataCriacao;
    private String tar_dataAtualizacao;
    private String usu_id;
    private String usu_nome;
    private String proj_id;
    private String proj_nome;

    //getters e setters pra manipular os atributos
    public String getTar_id() { return tar_id; } //aq tinha opcao do get e do set gettar_id q ficava igual
    public void setTar_id(String tar_id) { this.tar_id = tar_id; } // ou getTar_id ai coloquei assim pq> cml cs

    public String getTar_titulo() { return tar_titulo; }
    public void setTar_titulo(String tar_titulo) { this.tar_titulo = tar_titulo; }

    public String getTar_descricao() { return tar_descricao; }
    public void setTar_descricao(String tar_descricao) { this.tar_descricao = tar_descricao; }

    public String getTar_prazo() { return tar_prazo; }
    public void setTar_prazo(String tar_prazo) { this.tar_prazo = tar_prazo; }

    public String getTar_status() { return tar_status; }
    public void setTar_status(String tar_status) { this.tar_status = tar_status; }

    public String getTar_prioridade() { return tar_prioridade; }
    public void setTar_prioridade(String tar_prioridade) { this.tar_prioridade = tar_prioridade; }

    public String getTar_anexo() { return tar_anexo; }
    public void setTar_anexo(String tar_anexo) { this.tar_anexo = tar_anexo; }

    public String getTar_dataCriacao() { return tar_dataCriacao; }
    public void setTar_dataCriacao(String tar_dataCriacao) { this.tar_dataCriacao = tar_dataCriacao; }

    public String getTar_dataAtualizacao() { return tar_dataAtualizacao; }
    public void setTar_dataAtualizacao(String tar_dataAtualizacao) { this.tar_dataAtualizacao = tar_dataAtualizacao; }

    public String getUsu_id() { return usu_id; }
    public void setUsu_id(String usu_id) { this.usu_id = usu_id; }

    public String getUsu_nome() { return usu_nome; }
    public void setUsu_nome(String usu_nome) { this.usu_nome = usu_nome; }

    public String getProj_id() { return proj_id; }
    public void setProj_id(String proj_id) { this.proj_id = proj_id; }

    public String getProj_nome() { return proj_nome; }
    public void setProj_nome(String proj_nome) { this.proj_nome = proj_nome; }


}