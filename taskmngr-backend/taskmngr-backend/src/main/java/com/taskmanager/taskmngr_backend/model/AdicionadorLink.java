package com.taskmanager.taskmngr_backend.model;

import java.util.List;

//pra rodar sem dar erro IMPORTANTEEE mvn spring-boot:run -DskipTests


public interface AdicionadorLink<Tarefa> {
	public void adicionarLink(List<TarefaModel> lista);
    //adiciona os links em todas as tarefas da lista
    //pra exibir os caminhos possiveis e ficar no padrao HATEOAS
	public void adicionarLink(TarefaModel objeto);
    //adiciona os links em uma tarefa especifica aproveitando o metodo que ta em cima
}