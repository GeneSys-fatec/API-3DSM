package com.taskmanager.taskmngr_backend.model.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColunaDTO extends RepresentationModel<ColunaDTO>{
    private String Id;
    private String Titulo;
    private Integer Ordem;
}