package com.taskmanager.taskmngr_backend.service;

import com.taskmanager.taskmngr_backend.model.EquipeModel;
import com.taskmanager.taskmngr_backend.model.UsuarioModel;
import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.dto.UsuarioDTO;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
public class EquipeConverterService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public EquipeDTO modelParaDto(EquipeModel model) {
        if (model == null) {
            return null;
        }

        EquipeDTO dto = new EquipeDTO();
        dto.setEqu_id(model.getId());
        dto.setEqu_nome(model.getNome());
        dto.setEqu_descricao(model.getDescricao());

        if (model.getDataCriacao() != null) {
            dto.setEqu_dataCriacao(model.getDataCriacao().format(formatter));
        }
        if (model.getDataAtualizacao() != null) {
            dto.setEqu_dataAtualizacao(model.getDataAtualizacao().format(formatter));
        }

        if (model.getUsuarios() != null) {
            dto.setEqu_membros(model.getUsuarios().stream()
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
        model.setNome(dto.getEqu_nome());
        model.setDescricao(dto.getEqu_descricao());
        return model;
    }
    private UsuarioDTO usuarioModelParaDto(UsuarioModel model) {
        if (model == null) {
            return null;
        }
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsu_id(model.getUsu_id());
        dto.setUsu_nome(model.getUsu_nome());
        dto.setUsu_email(model.getUsu_email());
        dto.setUsu_caminhoFoto(model.getUsu_caminhoFoto());
        return dto;
    }
}