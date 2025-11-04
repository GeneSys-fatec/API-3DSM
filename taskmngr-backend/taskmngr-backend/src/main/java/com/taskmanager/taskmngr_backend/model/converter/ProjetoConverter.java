package com.taskmanager.taskmngr_backend.model.converter;

import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjetoConverter {

    @Autowired
    private EquipeRepository equipeRepository;

    public ProjetoModel dtoParaModel(ProjetoDTO dto) {
        ProjetoModel model = new ProjetoModel();
        model.setProjId(dto.getProjId());
        model.setProjNome(dto.getProjNome());
        model.setProjDescricao(dto.getProjDescricao());
        model.setProjStatus(dto.getProjStatus());

        if (dto.getEquId() != null) {
            model.setEquipeId(dto.getEquId());
        }

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


        String equipeId = model.getEquipeId();

        if (equipeId != null) {
            EquipeModel equipe = equipeRepository.findById(equipeId).orElse(null);

            if (equipe != null) {
                dto.setEquId(equipe.getEquId());
                dto.setEquNome(equipe.getEquNome());
            }
        }

        return dto;
    }
}