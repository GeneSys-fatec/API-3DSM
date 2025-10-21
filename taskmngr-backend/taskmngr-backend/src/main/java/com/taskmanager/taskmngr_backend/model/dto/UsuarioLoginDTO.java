package com.taskmanager.taskmngr_backend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioLoginDTO {
    
    @NotBlank(message = "Email é obrigatório!")
    @Email(message = "Email inválido.")
    private String usuEmail;

    @NotBlank(message = "Senha é obrigatória!")
    private String usuSenha;
}
