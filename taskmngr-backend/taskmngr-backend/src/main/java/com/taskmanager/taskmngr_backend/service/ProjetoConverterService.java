package com.taskmanager.taskmngr_backend.service;

import org.springframework.stereotype.Component;
import com.taskmanager.taskmngr_backend.model.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;

@Component
public class ProjetoConverterService {

    public ProjetoModel dtoParaModel(ProjetoDTO dto) {
        ProjetoModel model = new ProjetoModel();
        model.setProj_id(dto.getProj_id());
        model.setProj_nome(dto.getProj_nome());
        model.setProj_descricao(dto.getProj_descricao());
        model.setProj_status(dto.getProj_status());
        model.setProj_dataCriacao(dto.getProj_dataCriacao());
        model.setProj_dataAtualizacao(dto.getProj_dataAtualizacao());
        model.setEqu_id(dto.getEqu_id());
        model.setEqu_nome(dto.getEqu_nome());
        return model;
    }

    public ProjetoDTO modelParaDto(ProjetoModel model) {
        ProjetoDTO dto = new ProjetoDTO();
        dto.setProj_id(model.getProj_id());
        dto.setProj_nome(model.getProj_nome());
        dto.setProj_descricao(model.getProj_descricao());
        dto.setProj_status(model.getProj_status());
        dto.setProj_dataCriacao(model.getProj_dataCriacao());
        dto.setProj_dataAtualizacao(model.getProj_dataAtualizacao());
        dto.setEqu_id(model.getEqu_id());
        dto.setEqu_nome(model.getEqu_nome());
        return dto;
    }

}