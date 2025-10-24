package com.taskmanager.taskmngr_backend.controller.Notificacao;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.service.TarefaService;

@RestController
@RequestMapping("/teste")
public class TesteNotificacaoController {

    private final TarefaService tarefaService;

    public TesteNotificacaoController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping("/notificar-prazos")
    public String testarNotificacoes() {
        tarefaService.notificarTarefasProximoVencimento();
        tarefaService.notificarTarefasVencidas();
        return "Notificações de prazo executadas com sucesso!";
    }
}