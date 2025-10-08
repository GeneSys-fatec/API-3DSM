package com.taskmanager.taskmngr_backend.model.entidade;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "equipes")
public class EquipeModel {
    @Id
    private String equId;
    private String equNome;
    private String equDescricao;
    @CreatedDate
    private LocalDateTime equDataCriacao;
    @LastModifiedDate
    private LocalDateTime equDataAtualizacao;
    @DBRef
    private Set<UsuarioModel> usuarios = new HashSet<>();
}