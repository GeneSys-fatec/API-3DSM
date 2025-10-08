package com.taskmanager.taskmngr_backend.model;

import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

import com.taskmanager.taskmngr_backend.controller.Equipe.BuscaEquipeController;
import com.taskmanager.taskmngr_backend.controller.Equipe.CriaEquipeController;
import com.taskmanager.taskmngr_backend.controller.Equipe.EditaEquipeController;
import com.taskmanager.taskmngr_backend.controller.Equipe.ExcluiEquipeController;

@Component
public class AdicionadorLinkEquipe {

    public void adicionarLink(EquipeDTO dto) {
        String id = dto.getEquId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(BuscaEquipeController.class).getEquipeById(id))
                .withSelfRel();

        Link cadastrarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(CriaEquipeController.class).cadastrarEquipe(dto))
                .withRel("cadastrar");

        Link listarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(BuscaEquipeController.class).listarEquipes())
                .withRel("listar");

        Link atualizarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EditaEquipeController.class).atualizarEquipe(id, dto))
                .withRel("atualizar");

        Link apagarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ExcluiEquipeController.class).apagarEquipe(id))
                .withRel("excluir");

        dto.add(selfLink, cadastrarLink, listarLink, atualizarLink, apagarLink);
    }

    public void adicionarLink(List<EquipeDTO> lista) {
        for (EquipeDTO dto : lista) {
            adicionarLink(dto);
        }
    }
}