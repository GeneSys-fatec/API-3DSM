package com.taskmanager.taskmngr_backend.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UsuarioCadastroDTO {

    @NotBlank(message = "Nome é obrigatório!")
    @Pattern(
    regexp = "^$|^[A-Za-zÀ-ÖØ-öø-ÿ]+(?: [A-Za-zÀ-ÖØ-öø-ÿ]+)*$",
    message = "Nome inválido: use apenas letras e espaços."
    )
    private String usuNome;

    @NotBlank(message = "Email é obrigatório!")
    @Pattern(
    regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
    message = "Formato de email inválido."
    )
    private String usuEmail;
    
    @NotBlank(message = "Senha é obrigatória!")
    @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$",
    message = "Senha fraca: deve ter mínimo 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais."
    )
    private String usuSenha;

    private String usuConfirmarSenha;
}