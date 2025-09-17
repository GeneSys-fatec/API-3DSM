package com.taskmanager.taskmngr_backend.model;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import java.util.List;

import com.taskmanager.taskmngr_backend.controller.ProjetoController;
import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;

@Component
public class AdicionadorLinkProjetos{

    public void adicionarLink(ProjetoDTO dto) {
        String id = dto.getProj_nome();
        Link selfLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(ProjetoController.class).buscarPorId(id))
            .withSelfRel();

        Link cadastrarLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(ProjetoController.class).cadastrarProjeto(dto))
            .withRel("cadastrar");

        Link allLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(ProjetoController.class).listarTodas())
            .withRel("listar");

        Link updateLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(ProjetoController.class).atualizar(id, dto))
            .withRel("atualizar");

        Link deleteLink = WebMvcLinkBuilder
            .linkTo(WebMvcLinkBuilder.methodOn(ProjetoController.class).apagarProjeto(id))
            .withRel("excluir");

        dto.add(selfLink, cadastrarLink, allLink, updateLink, deleteLink);
    }

    public void adicionarLink(List<ProjetoDTO> lista) {
        for (ProjetoDTO dto : lista) {
            adicionarLink(dto);
        }
    }
}