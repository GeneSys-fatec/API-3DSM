package com.taskmanager.taskmngr_backend.model.converter;

import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.dto.NotificacaoDTO;
import com.taskmanager.taskmngr_backend.model.entidade.NotificacaoModel;

@Service
public class NotificacaoConverter {
    public static NotificacaoDTO modelParaDto(NotificacaoModel model) {
        NotificacaoDTO dto = new NotificacaoDTO();
        dto.setNotId(model.getNotId());
        dto.setNotMensagem(model.getNotMensagem());
        dto.setNotTipo(model.getNotTipo());
        dto.setNotLida(model.isNotLida());
        dto.setNotDataCriacao(model.getNotDataCriacao());
        return dto;
    }

    public static NotificacaoModel dtoParaModel(NotificacaoDTO dto) {
        NotificacaoModel model = new NotificacaoModel();
        model.setNotId(dto.getNotId());
        model.setNotMensagem(dto.getNotMensagem());
        model.setNotTipo(dto.getNotTipo());
        model.setNotLida(dto.isNotLida());
        model.setNotDataCriacao(dto.getNotDataCriacao());
        return model;
    }
}
