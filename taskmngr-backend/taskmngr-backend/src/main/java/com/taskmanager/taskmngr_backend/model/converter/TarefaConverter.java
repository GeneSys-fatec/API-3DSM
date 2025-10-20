package com.taskmanager.taskmngr_backend.model.converter;

import com.taskmanager.taskmngr_backend.model.dto.ResponsavelTarefaDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ResponsavelTarefa;
import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.model.dto.TarefaDTO;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;

import java.util.stream.Collectors;

@Component
public class TarefaConverter {

    private ResponsavelTarefa convertResponsavelDtoToEmbedded(ResponsavelTarefaDTO dto) {
        return new ResponsavelTarefa(dto.getUsuId(), dto.getUsuNome());
    }

    private ResponsavelTarefaDTO convertResponsavelEmbeddedToDto(ResponsavelTarefa embedded) {
        return new ResponsavelTarefaDTO(embedded.getUsuId(), embedded.getUsuNome());
    }

    public TarefaModel dtoParaModel(TarefaDTO dto) {
        TarefaModel model = new TarefaModel();
        model.setTarId(dto.getTarId());
        model.setTarTitulo(dto.getTarTitulo());
        model.setTarDescricao(dto.getTarDescricao());
        model.setTarPrazo(dto.getTarPrazo());
        model.setTarStatus(dto.getTarStatus());
        model.setTarPrioridade(dto.getTarPrioridade());
        model.setTarAnexos(dto.getTarAnexos());
        model.setTarDataCriacao(dto.getTarDataCriacao());
        model.setTarDataAtualizacao(dto.getTarDataAtualizacao());
        model.setTarDataConclusao(dto.getTarDataConclusao());
        model.setConcluidaNoPrazo(dto.getConcluidaNoPrazo());
        model.setProjId(dto.getProjId());
        model.setProjNome(dto.getProjNome());

        if (dto.getResponsaveis() != null) {
            model.setResponsaveis(
                    dto.getResponsaveis().stream()
                            .map(this::convertResponsavelDtoToEmbedded)
                            .collect(Collectors.toList())
            );

        }

        return model;
    }


    public TarefaDTO modelParaDto (TarefaModel model){
        TarefaDTO dto = new TarefaDTO();
        dto.setTarId(model.getTarId());
        dto.setTarTitulo(model.getTarTitulo());
        dto.setTarDescricao(model.getTarDescricao());
        dto.setTarPrazo(model.getTarPrazo());
        dto.setTarStatus(model.getTarStatus());
        dto.setTarPrioridade(model.getTarPrioridade());
        dto.setTarAnexos(model.getTarAnexos());
        dto.setTarDataCriacao(model.getTarDataCriacao());
        dto.setTarDataAtualizacao(model.getTarDataAtualizacao());
        dto.setTarDataConclusao(model.getTarDataConclusao());
        dto.setConcluidaNoPrazo(model.getConcluidaNoPrazo());
        dto.setProjId(model.getProjId());
        dto.setProjNome(model.getProjNome());

        if (model.getResponsaveis() != null) {
            dto.setResponsaveis(
                    model.getResponsaveis().stream()
                            .map(this::convertResponsavelEmbeddedToDto)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }


}