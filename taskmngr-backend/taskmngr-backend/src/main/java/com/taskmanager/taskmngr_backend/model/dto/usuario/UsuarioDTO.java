package com.taskmanager.taskmngr_backend.model.dto.usuario;

import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO extends RepresentationModel<UsuarioDTO>{
    private String usuId;

    @NotBlank(message = "Nome é obrigatório!")
    @Size(min = 2, max = 150, message = "Nome deve ter entre 2 e 150 caracteres")
    private String usuNome;

    @NotBlank(message = "Email é obrigatório!")
    @Email(message = "Email inválido.")
    @Size(max = 254, message = "Email deve ter no máximo 254 caracteres")
    private String usuEmail;

    @Size(max = 255, message = "Caminho da foto deve ter no máximo 255 caracteres")
    private String usuCaminhoFoto;

    private String usuDataCriacao;

    private String usuDataAtualizacao;
}
