package com.taskmanager.taskmngr_backend.model;

import org.springframework.data.annotation.Id;

//isso aq seria pro hateoas mas n ta funcionando import org.springframework.hateoas.RepresentationModel;
public class TarefaModel {
    @Id
    private String id;
    private String titulo;
    private String descricao;
    private String prazo;
    private String status;
    private String prioridade;
    private String anexo;
    private String dataCriacao;
    private String dataAtualizacao;
    private String usuId;
    private String usuNome;
    private String projId;
    private String projNome;

    //getters e setters pra manipular os atributos
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getPrazo() { return prazo; }
    public void setPrazo(String prazo) { this.prazo = prazo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPrioridade() { return prioridade; }
    public void setPrioridade(String prioridade) { this.prioridade = prioridade; }

    public String getAnexo() { return anexo; }
    public void setAnexo(String anexo) { this.anexo = anexo; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(String dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public String getUsuId() { return usuId; }
    public void setUsuId(String usuId) { this.usuId = usuId; }

    public String getUsuNome() { return usuNome; }
    public void setUsuNome(String usuNome) { this.usuNome = usuNome; }

    public String getProjId() { return projId; }
    public void setProjId(String projId) { this.projId = projId; }

    public String getProjNome() { return projNome; }
    public void setProjNome(String projNome) { this.projNome = projNome; }


}