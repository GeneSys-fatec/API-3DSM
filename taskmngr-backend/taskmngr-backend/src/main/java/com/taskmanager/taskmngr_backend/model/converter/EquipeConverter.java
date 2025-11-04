package com.taskmanager.taskmngr_backend.model.converter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;
import com.taskmanager.taskmngr_backend.model.dto.usuario.UsuarioDTO;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;

import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.repository.ProjetoRepository;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

@Component
public class EquipeConverter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Autowired
    private ProjetoConverter projetoConverter;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

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


        List<String> usuarioIds = model.getUsuarioIds();

        if (usuarioIds != null && !usuarioIds.isEmpty()) {
            List<UsuarioModel> membros = usuarioRepository.findAllById(usuarioIds);

            dto.setEquMembros(membros.stream()
                    .map(this::usuarioModelParaDto)
                    .collect(Collectors.toList()));
        }


        List<String> projetoIds = model.getProjetoIds();

        if (projetoIds != null && !projetoIds.isEmpty()) {
            List<ProjetoModel> projetos = projetoRepository.findAllById(projetoIds);

            List<ProjetoDTO> projetosDto = projetos.stream()
                    .filter(Objects::nonNull)
                    .map(projetoConverter::modelParaDto)
                    .collect(Collectors.toList());
            dto.setProjetos(projetosDto);
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