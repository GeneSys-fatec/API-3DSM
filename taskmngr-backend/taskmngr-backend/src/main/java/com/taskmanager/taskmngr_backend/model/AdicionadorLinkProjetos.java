package com.taskmanager.taskmngr_backend.model;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.controller.Projeto.BuscaProjetoController;
import com.taskmanager.taskmngr_backend.controller.Projeto.CriaProjetoController;
import com.taskmanager.taskmngr_backend.controller.Projeto.EditaProjetoController;
import com.taskmanager.taskmngr_backend.controller.Projeto.ExcluiProjetoController;
import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;

@Component
public class AdicionadorLinkProjetos {

    public void adicionarLink(ProjetoDTO dto) {

        String id = dto.getProjId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(BuscaProjetoController.class).buscarPorId(id))
                .withSelfRel();

        Link cadastrarLink = WebMvcLinkBuilder

                .linkTo(WebMvcLinkBuilder.methodOn(CriaProjetoController.class).cadastrarProjeto(dto, null))
                .withRel("cadastrar");

        Link allLink = WebMvcLinkBuilder

                .linkTo(WebMvcLinkBuilder.methodOn(BuscaProjetoController.class).listarTodas())
                .withRel("listar");

        Link updateLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EditaProjetoController.class).atualizar(id, dto))
                .withRel("atualizar");

        Link deleteLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ExcluiProjetoController.class).apagarProjeto(id))
                .withRel("excluir");

        dto.add(selfLink, cadastrarLink, allLink, updateLink, deleteLink);
    }

    public void adicionarLink(List<ProjetoDTO> lista) {
        for (ProjetoDTO dto : lista) {
            adicionarLink(dto);
        }
    }
}