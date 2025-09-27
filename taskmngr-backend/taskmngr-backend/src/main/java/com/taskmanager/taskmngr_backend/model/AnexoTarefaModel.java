package com.taskmanager.taskmngr_backend.model;

public class AnexoTarefaModel {
    private String arquivoNome;
    private String arquivoTipo;
    private long arquivoTamanho;
    private String arquivoCaminho;

    public String getArquivoNome() { return arquivoNome; }
    public void setArquivoNome(String arquivoNome) { this.arquivoNome = arquivoNome; }

    public String getArquivoTipo() { return arquivoTipo; }
    public void setArquivoTipo(String arquivoTipo) { this.arquivoTipo = arquivoTipo; }

    public long getArquivoTamanho() { return arquivoTamanho; }
    public void setArquivoTamanho(long arquivoTamanho) { this.arquivoTamanho = arquivoTamanho; }

    public String getArquivoCaminho() { return arquivoCaminho; }
    public void setArquivoCaminho(String arquivoCaminho) { this.arquivoCaminho = arquivoCaminho; }
}
