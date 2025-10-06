package com.taskmanager.taskmngr_backend.model;

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
    private String id;
    private String nome;
    private String descricao;
    @CreatedDate
    private LocalDateTime dataCriacao;
    @LastModifiedDate
    private LocalDateTime dataAtualizacao;
    @DBRef
    private Set<UsuarioModel> usuarios = new HashSet<>();
}