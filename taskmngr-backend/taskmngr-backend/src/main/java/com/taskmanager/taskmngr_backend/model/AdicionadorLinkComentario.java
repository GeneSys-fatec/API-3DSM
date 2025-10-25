package com.taskmanager.taskmngr_backend.model;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.controller.Comentario.BuscaComentarioController;
import com.taskmanager.taskmngr_backend.controller.Comentario.EditaComentarioController;
import com.taskmanager.taskmngr_backend.controller.Comentario.ExcluiComentarioController;
import com.taskmanager.taskmngr_backend.model.dto.ComentarioDTO;

@Component
public class AdicionadorLinkComentario {
    public void adicionarLink(ComentarioDTO dto) {
        String id = dto.getComId();
        String tarId = dto.getTarId();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EditaComentarioController.class).atualizarComentario(id, dto))
                .withSelfRel();

        Link cadastrarLink = Link.of("/comentario/cadastrar", "cadastrar");

        Link listarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(BuscaComentarioController.class).listarComentariosPorTarefa(tarId))
                .withRel("listar");

        Link deletarLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ExcluiComentarioController.class).apagarComentario(id))
                .withRel("excluir");

        dto.add(selfLink, cadastrarLink, listarLink, deletarLink);
    }

    public void adicionarLink(List<ComentarioDTO> lista) {
        for (ComentarioDTO dto : lista) {
            adicionarLink(dto);
        }
    }
}
