package com.taskmanager.taskmngr_backend.model.converter;

import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.model.dto.TarefaDTO;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;

@Component
public class TarefaConverter {

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
        model.setUsuId(dto.getUsuId());
        model.setUsuNome(dto.getUsuNome());
        model.setProjId(dto.getProjId());
        model.setProjNome(dto.getProjNome());
        return model;
    }

    public TarefaDTO modelParaDto(TarefaModel model) {
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
        dto.setUsuId(model.getUsuId());
        dto.setUsuNome(model.getUsuNome());
        dto.setProjId(model.getProjId());
        dto.setProjNome(model.getProjNome());
        return dto;
    }

    //ta explicacao rapida:
    //o >>modelpradto<< converte a entidade para objeto de resposta da api(dto)
    //vc usa quando for retornar dados pro cliente (get, get by id etc)

    //o >>dtopra model<<quando vai salvar ou atualizar dados no banco


}
