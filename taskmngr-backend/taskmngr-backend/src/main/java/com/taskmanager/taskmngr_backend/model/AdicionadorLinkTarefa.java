package com.taskmanager.taskmngr_backend.model;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import java.util.List;

import com.taskmanager.taskmngr_backend.controller.TarefaController;

@Component
public class AdicionadorLinkTarefa {

    public void adicionarLink(TarefaDTO dto) {
        String id = dto.getTar_id();

        Link selfLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).buscarPorId(id))
            .withSelfRel();

        Link cadastrarLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).cadastrarTarefa(dto))
            .withRel("cadastrar");

        Link allLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).listarTarefa())
            .withRel("listar");

        Link updateLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).atualizarTarefa(id, dto))
            .withRel("atualizar");

        Link deleteLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(TarefaController.class).apagarTarefa(id))
            .withRel("excluir");

        dto.add(selfLink, cadastrarLink, allLink, updateLink, deleteLink);
    }

    public void adicionarLink(List<TarefaDTO> lista) {
        for (TarefaDTO dto : lista) {
            adicionarLink(dto);
        }
    }
}