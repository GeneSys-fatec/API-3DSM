package com.taskmanager.taskmngr_backend.model.converter;

import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import org.springframework.stereotype.Component;

@Component
public class ProjetoConverter {

    public ProjetoModel dtoParaModel(ProjetoDTO dto) {
        ProjetoModel model = new ProjetoModel();
        model.setProjId(dto.getProjId());
        model.setProjNome(dto.getProjNome());
        model.setProjDescricao(dto.getProjDescricao());
        model.setProjStatus(dto.getProjStatus());

        return model;
    }

    public ProjetoDTO modelParaDto(ProjetoModel model) {
        ProjetoDTO dto = new ProjetoDTO();
        dto.setProjId(model.getProjId());
        dto.setProjNome(model.getProjNome());
        dto.setProjDescricao(model.getProjDescricao());
        dto.setProjStatus(model.getProjStatus());

        dto.setProjDataCriacao(model.getProjDataCriacao());
        dto.setProjDataAtualizacao(model.getProjDataAtualizacao());

        if (model.getEquipe() != null) {
            dto.setEquId(model.getEquipe().getEquId());
            dto.setEquNome(model.getEquipe().getEquNome());
        }

        return dto;
    }
}