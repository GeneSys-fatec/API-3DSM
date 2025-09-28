package com.taskmanager.taskmngr_backend.model;

import java.util.List;

public interface AdicionadorLink<Tarefa> {
	public void adicionarLink(List<TarefaModel> lista);
	public void adicionarLink(TarefaModel objeto);
}