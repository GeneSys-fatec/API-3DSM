package com.taskmanager.taskmngr_backend.model.converter;

import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.model.dto.UsuarioDTO;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;

@Component
public class UsuarioConverter {
    public UsuarioModel dtoParaModel(UsuarioDTO dto) {
        UsuarioModel model = new UsuarioModel();
        model.setUsuId(dto.getUsuId());
        model.setUsuNome(dto.getUsuNome());
        model.setUsuEmail(dto.getUsuEmail());
        model.setUsuCaminhoFoto(dto.getUsuCaminhoFoto());
        model.setUsuDataCriacao(dto.getUsuDataCriacao());
        model.setUsuDataAtualizacao(dto.getUsuDataAtualizacao());
        return model;
    }

    public UsuarioDTO modelParaDto(UsuarioModel model) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsuId(model.getUsuId());
        dto.setUsuNome(model.getUsuNome());
        dto.setUsuEmail(model.getUsuEmail());
        dto.setUsuCaminhoFoto(model.getUsuCaminhoFoto());
        dto.setUsuDataCriacao(model.getUsuDataCriacao());
        dto.setUsuDataAtualizacao(model.getUsuDataAtualizacao());
        return dto;
    }
}
