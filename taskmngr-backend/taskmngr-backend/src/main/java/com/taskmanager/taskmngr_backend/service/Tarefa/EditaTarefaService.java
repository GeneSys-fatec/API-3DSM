package com.taskmanager.taskmngr_backend.service.Tarefa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.dto.TarefaDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ResponsavelTarefa;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.TarefaRepository;
import com.taskmanager.taskmngr_backend.service.Notificacao.CriaNotificacaoService;

@Service
public class EditaTarefaService {
    @Autowired
    private NotificacaoTarefaService notificacaoTarefaService;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private BuscaTarefaService buscaTarefaService;

    @Autowired
    private CriaTarefaService criaTarefaService;

    @Autowired
    private CriaNotificacaoService criaNotificacaoService;

    public TarefaModel atualizarTarefa(String tarId, TarefaDTO dto, UsuarioModel usuarioLogado) {
        TarefaModel tarefa = buscaTarefaService.buscarPorId(tarId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com id: " + tarId)); 

        tarefa.setTarTitulo(dto.getTarTitulo());
        tarefa.setTarDescricao(dto.getTarDescricao());
        tarefa.setTarStatus(dto.getTarStatus());
        tarefa.setTarPrioridade(dto.getTarPrioridade());
        tarefa.setTarPrazo(dto.getTarPrazo());
        tarefa.setTarDataAtualizacao(dto.getTarDataAtualizacao());
        tarefa.setProjId(dto.getProjId());
        tarefa.setProjNome(dto.getProjNome());

        if (dto.getResponsaveis() != null && !dto.getResponsaveis().isEmpty()) {
            List<ResponsavelTarefa> responsaveisVerificados = criaTarefaService.buildResponsaveisList(dto.getResponsaveis());
            tarefa.setResponsaveis(responsaveisVerificados);
        } else {
            tarefa.setResponsaveis(new ArrayList<>());
        }

        if ("Concluída".equalsIgnoreCase(dto.getTarStatus())) {
            String dataConclusao = java.time.LocalDate.now().toString();
            tarefa.setTarDataConclusao(dataConclusao);
            java.time.LocalDate prazo = java.time.LocalDate.parse(tarefa.getTarPrazo());
            java.time.LocalDate conclusao = java.time.LocalDate.parse(dataConclusao);
            tarefa.setConcluidaNoPrazo(!conclusao.isAfter(prazo));
        } else {
            tarefa.setTarDataConclusao(null);
            tarefa.setConcluidaNoPrazo(null);
        }

        TarefaModel tarefaAtualizada = notificacaoTarefaService.salvarSemNotificacao(tarefa);

        if (tarefaAtualizada.getResponsaveis() != null) {
            for (ResponsavelTarefa responsavel : tarefaAtualizada.getResponsaveis()) {
                String idResponsavel = responsavel.getUsuId();
                if (!idResponsavel.equals(usuarioLogado.getUsuId())) {
                    criaNotificacaoService.criarNotificacaoEdicaoTarefa(
                            usuarioLogado.getUsuId(),
                            idResponsavel,
                            tarefaAtualizada.getTarId(),
                            tarefaAtualizada.getTarTitulo(),
                            usuarioLogado.getUsuNome());
                }
            }
        }

        return tarefaAtualizada;
    }

    public TarefaModel atualizar(TarefaModel tarefa) {
        return tarefaRepository.save(tarefa);
    }
}
