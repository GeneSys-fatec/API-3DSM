package com.taskmanager.taskmngr_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class ColunaModel {
    @Id
    private String colId;
    private String colTitulo;
    private Integer colOrdem;
}
