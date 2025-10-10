package com.taskmanager.taskmngr_backend.model.dto;

import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO extends RepresentationModel<UsuarioDTO>{
    private String usuId;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 150, message = "Nome deve ter entre 2 e 150 caracteres")
    private String usuNome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Size(max = 254, message = "E-mail deve ter no máximo 254 caracteres")
    private String usuEmail;

    @Size(max = 255, message = "Caminho da foto deve ter no máximo 255 caracteres")
    private String usuCaminhoFoto;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, max = 64, message = "Senha deve ter entre 8 e 64 caracteres")
    private String usuSenha;

    private String usuDataCriacao;
    private String usuDataAtualizacao;
}
