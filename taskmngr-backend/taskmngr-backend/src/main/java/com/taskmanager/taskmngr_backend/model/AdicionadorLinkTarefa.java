// package com.taskmanager.taskmngr_backend.model;

// import java.util.List;

// import org.springframework.hateoas.Link;
// import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
// import org.springframework.stereotype.Component;

// import com.taskmanager.taskmngr_backend.controller.TarefaController;

// @Component
// public class AdicionadorLinkTarefa implements AdicionadorLink<TarefaModel> {

// 	@Override
// 	public void adicionarLink(List<TarefaModel> lista) {
// 		for (TarefaModel tarefa : lista) {
// 			String id = tarefa.getId();
//             Link selfLink = WebMvcLinkBuilder
//                     .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).obterTarefa(id))
//                     .withSelfRel();
//             Link allLink = WebMvcLinkBuilder
//                     .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).obterTarefas())
//                     .withRel("tarefas");
//             Link updateLink = WebMvcLinkBuilder
//                     .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).atualizarTarefa(tarefa))
//                     .withRel("atualizar");
//             Link deleteLink = WebMvcLinkBuilder
//                     .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).excluirTarefa(tarefa))
//                     .withRel("excluir");
//             tarefa.add(selfLink, allLink, updateLink, deleteLink);
//         }
//     }

//     @Override
//     public void adicionarLink(TarefaModel objeto) {
//         adicionarLink(List.of(objeto)); // reaproveita o método acima
//     }
// }