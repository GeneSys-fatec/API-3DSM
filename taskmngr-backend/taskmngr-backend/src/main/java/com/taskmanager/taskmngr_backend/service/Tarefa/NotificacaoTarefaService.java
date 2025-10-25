package com.taskmanager.taskmngr_backend.service.Tarefa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.entidade.ResponsavelTarefa;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.TarefaRepository;
import com.taskmanager.taskmngr_backend.service.Notificacao.CriaNotificacaoService;

@Service
public class NotificacaoTarefaService {
    @Autowired
    private CriaNotificacaoService criaNotificacaoService;

    @Autowired
    private TarefaRepository tarefaRepository;

    public TarefaModel salvarSemNotificacao(TarefaModel tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public TarefaModel salvarEEnviarNotificacao(TarefaModel tarefa, UsuarioModel usuarioLogado) {
        TarefaModel tarefaSalva = tarefaRepository.save(tarefa);

        if (tarefaSalva.getResponsaveis() != null) {
            for (ResponsavelTarefa responsavel : tarefaSalva.getResponsaveis()) {
                criaNotificacaoService.criarNotificacaoAtribuicao(
                        usuarioLogado.getUsuId(),
                        responsavel.getUsuId(),
                        usuarioLogado.getUsuNome(),
                        tarefaSalva.getTarId(),
                        tarefaSalva.getTarTitulo());
            }
        }
        return tarefaSalva;
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void notificarTarefasProximoVencimento() {
        LocalDate hoje = LocalDate.now();
        LocalDate prazoProximo = hoje.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<TarefaModel> tarefas = tarefaRepository.findAll();

        for (TarefaModel tarefa : tarefas) {
            try {
                if (tarefa.getTarPrazo() == null || tarefa.getTarPrazo().isBlank())
                    continue;

                LocalDate prazo = LocalDate.parse(tarefa.getTarPrazo(), formatter);

                if (prazo.equals(prazoProximo)) {
                    if (tarefa.getResponsaveis() != null) {
                        for (ResponsavelTarefa responsavel : tarefa.getResponsaveis()) {
                            criaNotificacaoService.criarNotificacaoPrazo(
                                    tarefa.getTarId(),
                                    tarefa.getTarTitulo(),
                                    responsavel.getUsuId());
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro ao converter data da tarefa " + tarefa.getTarId() + ": " + e.getMessage());
            }
        }
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void notificarTarefasVencidas() {
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<TarefaModel> tarefas = tarefaRepository.findAll();

        for (TarefaModel tarefa : tarefas) {
            try {
                if (tarefa.getTarPrazo() == null || tarefa.getTarPrazo().isBlank())
                    continue;
                if ("Concluída".equalsIgnoreCase(tarefa.getTarStatus()))
                    continue;

                LocalDate prazo = LocalDate.parse(tarefa.getTarPrazo(), formatter);

                if (prazo.isBefore(hoje)) {
                    if (tarefa.getResponsaveis() != null) {
                        for (ResponsavelTarefa responsavel : tarefa.getResponsaveis()) {
                            criaNotificacaoService.criarNotificacaoPrazoExpirado(
                                    tarefa.getTarId(),
                                    tarefa.getTarTitulo(),
                                    responsavel.getUsuId());
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro ao converter data da tarefa " + tarefa.getTarId() + ": " + e.getMessage());
            }
        }
    }
}