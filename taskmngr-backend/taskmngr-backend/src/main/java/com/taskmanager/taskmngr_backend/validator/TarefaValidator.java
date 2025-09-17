
package com.taskmanager.taskmngr_backend.validator;

import com.taskmanager.taskmngr_backend.model.TarefaModel;

public class TarefaValidator {
	public static String validar(TarefaModel tarefa) {
		if (tarefa.getTar_titulo() == null || tarefa.getTar_titulo().trim().isEmpty()) {
			return "O título é obrigatório";
            //se for null ou "    " o trim remove os espacos e fica so ""
            //ai ele mostra a mensagem de erro
		}
		if (tarefa.getTar_descricao() == null || tarefa.getTar_descricao().trim().isEmpty()) {
			return "A descrição é obrigatória";
		}
		if (tarefa.getTar_prazo() == null || tarefa.getTar_prazo().trim().isEmpty()) {
			return "O prazo é obrigatório";
		}
		if (tarefa.getTar_status() == null || tarefa.getTar_status().trim().isEmpty()) {
			return "O status é obrigatório";
		}
		if (tarefa.getTar_prioridade() == null || tarefa.getTar_prioridade().trim().isEmpty()) {
			return "A prioridade é obrigatória";
		}
		if (tarefa.getTar_dataCriacao() == null || tarefa.getTar_dataCriacao().trim().isEmpty()) {
			return "A data de criação é obrigatória";
		}
		// if (tarefa.getUsu_id() == null || tarefa.getUsu_id().trim().isEmpty()) {
		// 	return "O ID do usuário é obrigatório";
		// } isso aq nao vai ter obrigatoriedade ja q vai vir da entidade usuario? 
		// mas e se 2 pessoas tiverem o mesmo nome...?
		if (tarefa.getUsu_nome() == null || tarefa.getUsu_nome().trim().isEmpty()) {
			return "O nome do usuário é obrigatório";
		}
		if (tarefa.getProj_id() == null || tarefa.getProj_id().trim().isEmpty()) {
			return "O ID do projeto é obrigatório";
		}
		if (tarefa.getProj_nome() == null || tarefa.getProj_nome().trim().isEmpty()) {
			return "O nome do projeto é obrigatório";
		}
		return null;
	}
}
