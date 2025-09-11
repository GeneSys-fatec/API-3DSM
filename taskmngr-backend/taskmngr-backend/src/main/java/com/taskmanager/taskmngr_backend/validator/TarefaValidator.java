
package com.taskmanager.taskmngr_backend.validator;

import com.taskmanager.taskmngr_backend.model.TarefaModel;

public class TarefaValidator {
	public static String validar(TarefaModel tarefa) {
		if (tarefa.getTitulo() == null || tarefa.getTitulo().trim().isEmpty()) {
			return "O título é obrigatório";
            //se for null ou "    " o trim remove os espacos e fica so ""
            //ai ele mostra a mensagem de erro
		}
		if (tarefa.getDescricao() == null || tarefa.getDescricao().trim().isEmpty()) {
			return "A descrição é obrigatória";
		}
		if (tarefa.getPrazo() == null || tarefa.getPrazo().trim().isEmpty()) {
			return "O prazo é obrigatório";
		}
		if (tarefa.getStatus() == null || tarefa.getStatus().trim().isEmpty()) {
			return "O status é obrigatório";
		}
		if (tarefa.getPrioridade() == null || tarefa.getPrioridade().trim().isEmpty()) {
			return "A prioridade é obrigatória";
		}
		if (tarefa.getDataCriacao() == null || tarefa.getDataCriacao().trim().isEmpty()) {
			return "A data de criação é obrigatória";
		}
		if (tarefa.getUsuId() == null || tarefa.getUsuId().trim().isEmpty()) {
			return "O ID do usuário é obrigatório";
		}
		if (tarefa.getUsuNome() == null || tarefa.getUsuNome().trim().isEmpty()) {
			return "O nome do usuário é obrigatório";
		}
		if (tarefa.getProjId() == null || tarefa.getProjId().trim().isEmpty()) {
			return "O ID do projeto é obrigatório";
		}
		if (tarefa.getProjNome() == null || tarefa.getProjNome().trim().isEmpty()) {
			return "O nome do projeto é obrigatório";
		}
		return null;
	}
}
