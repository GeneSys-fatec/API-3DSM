package com.taskmanager.taskmngr_backend.model.dto;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ComentarioDTO extends RepresentationModel<ComentarioDTO> {
    private String comId;
    @NotBlank(message = "A mensagem não pode estar em branco!")
    private String comMensagem;
    private Date comDataCriacao;
    private Date comDataAtualizacao;
    private String usuId;
    private String usuNome;
    private String tarId;
    private String comResposta;
}
