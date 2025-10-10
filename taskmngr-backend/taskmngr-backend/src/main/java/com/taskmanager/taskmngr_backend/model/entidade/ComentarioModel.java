package com.taskmanager.taskmngr_backend.model.entidade;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "comentarios")
public class ComentarioModel {
    @Id
    private String comId;
    private String comMensagem;
    private Date comDataCriacao;
    private Date comDataAtualizacao;
    private String usuId;
    private String usuNome;
    private String tarId;
    private String comResposta;
}
