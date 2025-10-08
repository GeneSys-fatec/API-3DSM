package com.taskmanager.taskmngr_backend.model.converter;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.dto.UsuarioDTO;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;

@Component
public class EquipeConverter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public EquipeDTO modelParaDto(EquipeModel model) {
        if (model == null) {
            return null;
        }

        EquipeDTO dto = new EquipeDTO();
        dto.setEquId(model.getEquId());
        dto.setEquNome(model.getEquNome());
        dto.setEquDescricao(model.getEquDescricao());

        if (model.getEquDataCriacao() != null) {
            dto.setEquDataCriacao(model.getEquDataCriacao().format(formatter));
        }
        if (model.getEquDataAtualizacao() != null) {
            dto.setEquDataAtualizacao(model.getEquDataAtualizacao().format(formatter));
        }

        if (model.getUsuarios() != null) {
            dto.setEquMembros(model.getUsuarios().stream()
                    .map(this::usuarioModelParaDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public EquipeModel dtoParaModel(EquipeDTO dto) {
        if (dto == null) {
            return null;
        }
        EquipeModel model = new EquipeModel();
        model.setEquNome(dto.getEquNome());
        model.setEquDescricao(dto.getEquDescricao());
        return model;
    }
    private UsuarioDTO usuarioModelParaDto(UsuarioModel model) {
        if (model == null) {
            return null;
        }
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsuId(model.getUsuId());
        dto.setUsuNome(model.getUsuNome());
        dto.setUsuEmail(model.getUsuEmail());
        dto.setUsuCaminhoFoto(model.getUsuCaminhoFoto());
        return dto;
    }
}