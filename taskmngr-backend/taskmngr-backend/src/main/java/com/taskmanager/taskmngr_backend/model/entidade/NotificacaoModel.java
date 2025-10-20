package com.taskmanager.taskmngr_backend.model.entidade;

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.data.annotation.Id;
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
    private LocalDateTime notDataCriacao;
    private Map<String, Object> notDadosAdicionais;
}
