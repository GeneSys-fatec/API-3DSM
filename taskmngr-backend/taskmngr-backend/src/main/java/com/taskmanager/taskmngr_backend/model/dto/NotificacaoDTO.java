package com.taskmanager.taskmngr_backend.model.dto;

import java.time.LocalDateTime;
import org.springframework.hateoas.RepresentationModel;
import lombok.Data;

@Data
public class NotificacaoDTO extends RepresentationModel<NotificacaoDTO> {
    private String notId;
    private String notMensagem;
    private String notTipo;
    private boolean notLida;
    private LocalDateTime notDataCriacao;
}
