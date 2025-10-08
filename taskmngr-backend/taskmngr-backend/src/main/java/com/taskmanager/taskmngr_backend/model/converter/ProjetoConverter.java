package com.taskmanager.taskmngr_backend.model.converter;

import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;

@Component
public class ProjetoConverter {

    public ProjetoModel dtoParaModel(ProjetoDTO dto) {
        ProjetoModel model = new ProjetoModel();
        model.setProjId(dto.getProjId());
        model.setProjNome(dto.getProjNome());
        model.setProjDescricao(dto.getProjDescricao());
        model.setProjStatus(dto.getProjStatus());
        model.setProjDataCriacao(dto.getProjDataCriacao());
        model.setProjDataAtualizacao(dto.getProjDataAtualizacao());
        model.setEqu_id(dto.getEqu_id());
        model.setEqu_nome(dto.getEqu_nome());
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
        dto.setEqu_id(model.getEqu_id());
        dto.setEqu_nome(model.getEqu_nome());
        return dto;
    }

}