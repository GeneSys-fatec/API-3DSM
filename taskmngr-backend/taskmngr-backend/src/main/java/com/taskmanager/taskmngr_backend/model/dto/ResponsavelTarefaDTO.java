package com.taskmanager.taskmngr_backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsavelTarefaDTO {
    private String usuId;
    private String usuNome;
}
