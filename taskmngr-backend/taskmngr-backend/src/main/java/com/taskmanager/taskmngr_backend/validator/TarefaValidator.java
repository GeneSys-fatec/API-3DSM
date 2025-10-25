package com.taskmanager.taskmngr_backend.validator;

import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;

public class TarefaValidator {
    public static String validar(TarefaModel Tarefa) {
        if (Tarefa.getTarTitulo() == null || Tarefa.getTarTitulo().trim().isEmpty()) {
            return "O título é obrigatório";
        }
        if (Tarefa.getTarDescricao() == null || Tarefa.getTarDescricao().trim().isEmpty()) {
            return "A descrição é obrigatória";
        }
        if (Tarefa.getTarPrazo() == null || Tarefa.getTarPrazo().trim().isEmpty()) {
            return "O prazo é obrigatório";
        }
        if (Tarefa.getTarStatus() == null || Tarefa.getTarStatus().trim().isEmpty()) {
            return "O status é obrigatório";
        }
        if (Tarefa.getTarPrioridade() == null || Tarefa.getTarPrioridade().trim().isEmpty()) {
            return "A prioridade é obrigatória";
        }
        if (Tarefa.getTarDataCriacao() == null || Tarefa.getTarDataCriacao().trim().isEmpty()) {
            return "A data de criação é obrigatória";
        }
        if (Tarefa.getResponsaveis() == null || Tarefa.getResponsaveis().isEmpty()) {
            return "Pelo menos um responsável é obrigatório";
        }

        if (Tarefa.getProjId() == null || Tarefa.getProjId().trim().isEmpty()) {
            return "O ID do projeto é obrigatório";
        }
        if (Tarefa.getProjNome() == null || Tarefa.getProjNome().trim().isEmpty()) {
            return "O nome do projeto é obrigatório";
        }
        return null;
    }
}