package com.taskmanager.taskmngr_backend.service.Coluna;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.dto.ColunaDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ColunaModel;
import com.taskmanager.taskmngr_backend.repository.ColunaRepository;

@Service
public class BuscaColunaService {
    @Autowired
    private ColunaRepository colunaRepository;

    public List<ColunaDTO> listarPorProjeto(String projId) {
        List<ColunaModel> colunas = colunaRepository.findByProjIdOrderByColOrdemAsc(projId);
        return colunas.stream()
                .map(c -> new ColunaDTO(c.getColId(), c.getColTitulo(), c.getColOrdem(), c.getProjId()))
                .collect(Collectors.toList());
    }
}