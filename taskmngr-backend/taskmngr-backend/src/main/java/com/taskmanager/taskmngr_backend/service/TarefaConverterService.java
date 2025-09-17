package com.taskmanager.taskmngr_backend.service;

import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.model.TarefaModel;
import com.taskmanager.taskmngr_backend.model.dto.TarefaDTO;

@Component
public class TarefaConverterService {

    public TarefaModel dtoParaModel(TarefaDTO dto) {
        TarefaModel model = new TarefaModel();
        model.setTar_id(dto.getTar_id());
        model.setTar_titulo(dto.getTar_titulo());
        model.setTar_descricao(dto.getTar_descricao());
        model.setTar_prazo(dto.getTar_prazo());
        model.setTar_status(dto.getTar_status());
        model.setTar_prioridade(dto.getTar_prioridade());
        model.setTar_anexo(dto.getTar_anexo());
        model.setTar_dataCriacao(dto.getTar_dataCriacao());
        model.setTar_dataAtualizacao(dto.getTar_dataAtualizacao());
        model.setUsu_id(dto.getUsu_id());
        model.setUsu_nome(dto.getUsu_nome());
        model.setProj_id(dto.getProj_id());
        model.setProj_nome(dto.getProj_nome());
        return model;
    }

    public TarefaDTO modelParaDto(TarefaModel model) {
        TarefaDTO dto = new TarefaDTO();
        dto.setTar_id(model.getTar_id());
        dto.setTar_titulo(model.getTar_titulo());
        dto.setTar_descricao(model.getTar_descricao());
        dto.setTar_prazo(model.getTar_prazo());
        dto.setTar_status(model.getTar_status());
        dto.setTar_prioridade(model.getTar_prioridade());
        dto.setTar_anexo(model.getTar_anexo());
        dto.setTar_dataCriacao(model.getTar_dataCriacao());
        dto.setTar_dataAtualizacao(model.getTar_dataAtualizacao());
        dto.setUsu_id(model.getUsu_id());
        dto.setUsu_nome(model.getUsu_nome());
        dto.setProj_id(model.getProj_id());
        dto.setProj_nome(model.getProj_nome());
        return dto;
    }

    //ta explicacao rapida:
    //o >>modelpradto<< converte a entidade para objeto de resposta da api(dto)
    //vc usa quando for retornar dados pro cliente (get, get by id etc)

    //o >>dtopra model<<quando vai salvar ou atualizar dados no banco


}
