package com.taskmanager.taskmngr_backend.model;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.controller.Tarefa.BuscaTarefaController;
import com.taskmanager.taskmngr_backend.controller.Tarefa.CriaTarefaController;
import com.taskmanager.taskmngr_backend.controller.Tarefa.EditaTarefaController;
import com.taskmanager.taskmngr_backend.controller.Tarefa.ExcluiTarefaController;
import com.taskmanager.taskmngr_backend.model.dto.TarefaDTO;

@Component
public class AdicionadorLinkTarefa {

    public void adicionarLink(TarefaDTO dto) {
        String id = dto.getTarId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(BuscaTarefaController.class).buscarPorId(id))
                .withSelfRel();

        Link cadastrarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(CriaTarefaController.class).cadastrarTarefa(dto, null))
                .withRel("cadastrar");

        Link allLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(BuscaTarefaController.class).listarTarefa())
                .withRel("listar");

        Link updateLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EditaTarefaController.class).atualizarTarefa(id, dto, null))
                .withRel("atualizar");

        Link deleteLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ExcluiTarefaController.class).apagarTarefa(id))
                .withRel("excluir");

        dto.add(selfLink, cadastrarLink, allLink, updateLink, deleteLink);
    }

    public void adicionarLink(List<TarefaDTO> lista) {
        for (TarefaDTO dto : lista) {
            adicionarLink(dto);
        }
    }
}