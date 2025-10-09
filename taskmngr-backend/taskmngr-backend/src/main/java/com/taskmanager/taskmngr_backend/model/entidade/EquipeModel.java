package com.taskmanager.taskmngr_backend.model.entidade;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "equipes")
public class EquipeModel {
    @Id
    private String equId;
    @Indexed(unique = true)
    private String equNome;
    private String equDescricao;
    private String criadorId;
    @CreatedDate
    private LocalDateTime equDataCriacao;
    @LastModifiedDate
    private LocalDateTime equDataAtualizacao;
    @DBRef
    private Set<UsuarioModel> usuarios = new HashSet<>();
    @DBRef
    private List<ProjetoModel> projetos = new ArrayList<>();
}