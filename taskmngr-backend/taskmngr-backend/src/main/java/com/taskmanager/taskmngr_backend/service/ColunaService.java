package com.taskmanager.taskmngr_backend.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.dto.ColunaDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ColunaModel;
import com.taskmanager.taskmngr_backend.repository.ColunaRepository;

@Service
public class ColunaService {

    @Autowired
    private ColunaRepository colunaRepository;

    public List<ColunaDTO> listarPorProjeto(String projId) {
        List<ColunaModel> colunas = colunaRepository.findByProjIdOrderByColOrdemAsc(projId);
        return colunas.stream()
                .map(c -> new ColunaDTO(c.getColId(), c.getColTitulo(), c.getColOrdem(), c.getProjId()))
                .collect(Collectors.toList());
    }

    public ColunaModel criarColuna(ColunaModel novaColuna) {
        long totalColunasNoProjeto = colunaRepository.countByprojId(novaColuna.getProjId());

        novaColuna.setColOrdem((int) totalColunasNoProjeto);

        return colunaRepository.save(novaColuna);
    }

    public ColunaDTO atualizarColuna(String col_id, ColunaDTO colunaDTO) {
        ColunaModel colunaExistente = colunaRepository.findById(col_id)
                .orElseThrow(() -> new ProjetoNaoEncontradoException("Coluna não encontrada", "Coluna com id " + col_id + " não foi encontrada"));

        colunaExistente.setColTitulo(colunaDTO.getTitulo());

        ColunaModel colunaAtualizada = colunaRepository.save(colunaExistente);

        return new ColunaDTO(colunaAtualizada.getColId(), colunaAtualizada.getColTitulo(), colunaAtualizada.getColOrdem(), colunaAtualizada.getProjId());
    }

    public void deletarColuna(String col_id) {
        if (!colunaRepository.existsById(col_id)) {
            throw new ProjetoNaoEncontradoException("Coluna não encontrada", "Coluna com id " + col_id + " não foi encontrada");
        }
        colunaRepository.deleteById(col_id);
    }
}