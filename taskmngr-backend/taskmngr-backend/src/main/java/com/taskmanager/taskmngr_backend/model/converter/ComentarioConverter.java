package com.taskmanager.taskmngr_backend.model.converter;

import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.dto.ComentarioDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ComentarioModel;

@Service
public class ComentarioConverter {
    public ComentarioDTO modelParaDto(ComentarioModel model) {
        ComentarioDTO dto = new ComentarioDTO();
        dto.setComId(model.getComId());
        dto.setComMensagem(model.getComMensagem());
        dto.setComResposta(model.getComResposta());
        dto.setComDataCriacao(model.getComDataCriacao());
        dto.setComDataAtualizacao(model.getComDataAtualizacao());
        dto.setUsuId(model.getUsuId());
        dto.setUsuNome(model.getUsuNome());
        dto.setTarId(model.getTarId());
        return dto;
    }

    public ComentarioModel dtoParaModel(ComentarioDTO dto) {
        ComentarioModel model = new ComentarioModel();
        model.setComId(dto.getComId());
        model.setComMensagem(dto.getComMensagem());
        model.setComResposta(dto.getComResposta());
        model.setComDataCriacao(dto.getComDataCriacao());
        model.setComDataAtualizacao(dto.getComDataAtualizacao());
        model.setTarId(dto.getTarId());
        return model;
    }
}
