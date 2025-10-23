package com.taskmanager.taskmngr_backend.model.entidade;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "notificacoes")
public class NotificacaoModel {
    @Id
    private String notId;
    private String notMensagem;
    private String notUsuarioId;
    private boolean notLida;
    private String notTarefaId;
    private String notTipo;

    @Indexed(name = "dataExpiracaoIndex", expireAfterSeconds = 2592000)
    private LocalDateTime notDataCriacao;
    
    private Map<String, Object> notDadosAdicionais;
}
