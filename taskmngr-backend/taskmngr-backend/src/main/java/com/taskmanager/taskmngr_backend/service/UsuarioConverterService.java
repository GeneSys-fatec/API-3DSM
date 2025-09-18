package com.taskmanager.taskmngr_backend.service;

import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.model.UsuarioModel;
import com.taskmanager.taskmngr_backend.model.dto.UsuarioDTO;

@Component
public class UsuarioConverterService {
    public UsuarioModel dtoParaModel(UsuarioDTO dto) {
        UsuarioModel model = new UsuarioModel();
        model.setUsu_id(dto.getUsu_id());
        model.setUsu_nome(dto.getUsu_nome());
        model.setUsu_email(dto.getUsu_email());
        model.setUsu_caminhoFoto(dto.getUsu_caminhoFoto());
        model.setUsu_senha(dto.getUsu_senha());
        model.setUsu_dataCriacao(dto.getUsu_dataCriacao());
        model.setUsu_dataAtualizacao(dto.getUsu_dataAtualizacao());
        return model;
    }

    public UsuarioDTO modelParaDto(UsuarioModel model) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsu_id(model.getUsu_id());
        dto.setUsu_nome(model.getUsu_nome());
        dto.setUsu_email(model.getUsu_email());
        dto.setUsu_caminhoFoto(model.getUsu_caminhoFoto());
        dto.setUsu_senha(model.getUsu_senha());
        dto.setUsu_dataCriacao(model.getUsu_dataCriacao());
        dto.setUsu_dataAtualizacao(model.getUsu_dataAtualizacao());
        return dto;
    }
}
