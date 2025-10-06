package com.taskmanager.taskmngr_backend.model;

import com.taskmanager.taskmngr_backend.controller.EquipeController;
import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkEquipe {

    public void adicionarLink(EquipeDTO dto) {
        String id = dto.getEqu_id();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EquipeController.class).getEquipeById(id))
                .withSelfRel();

        Link cadastrarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EquipeController.class).cadastrarEquipe(dto))
                .withRel("cadastrar");

        Link listarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EquipeController.class).listarEquipes())
                .withRel("listar");

        Link atualizarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EquipeController.class).atualizarEquipe(id, dto))
                .withRel("atualizar");

        Link apagarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EquipeController.class).apagarEquipe(id))
                .withRel("excluir");

        dto.add(selfLink, cadastrarLink, listarLink, atualizarLink, apagarLink);
    }

    public void adicionarLink(List<EquipeDTO> lista) {
        for (EquipeDTO dto : lista) {
            adicionarLink(dto);
        }
    }
}