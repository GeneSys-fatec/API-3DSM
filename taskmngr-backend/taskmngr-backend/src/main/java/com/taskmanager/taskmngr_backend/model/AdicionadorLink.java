package com.taskmanager.taskmngr_backend.model;

import java.util.List;

import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;

public interface AdicionadorLink<Tarefa> {
	public void adicionarLink(List<TarefaModel> lista);
	public void adicionarLink(TarefaModel objeto);
}