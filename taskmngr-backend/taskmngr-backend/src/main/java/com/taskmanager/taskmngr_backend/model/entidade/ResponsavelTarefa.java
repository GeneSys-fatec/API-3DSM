package com.taskmanager.taskmngr_backend.model.entidade; // ou .embedded

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsavelTarefa {
    private String usuId;
    private String usuNome;
}