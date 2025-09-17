package com.taskmanager.taskmngr_backend.model;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.controller.TarefaController;

@Component
public class AdicionadorLinkTarefa implements AdicionadorLink<TarefaModel> {

    @Override
    public void adicionarLink(List<TarefaModel> lista) {
        for (TarefaModel tarefa : lista) {
            String id = tarefa.getTar_id();

            Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).buscarPorId(id))
                .withSelfRel();

            Link cadastrarLink = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).cadastrarTarefa(tarefa))
                    .withRel("cadastrar");

            Link allLink = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).listarTarefa())
                    .withRel("listar");

            Link updateLink = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).atualizarTarefa(id, tarefa))
                    .withRel("atualizar");

            Link deleteLink = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).apagarTarefa(id))
                    .withRel("excluir");

            tarefa.add(selfLink, cadastrarLink, allLink, updateLink, deleteLink);
        }
    }

    @Override
    public void adicionarLink(TarefaModel objeto) {
        adicionarLink(List.of(objeto));
    }
}