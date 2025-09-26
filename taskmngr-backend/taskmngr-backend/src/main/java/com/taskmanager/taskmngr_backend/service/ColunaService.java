package com.taskmanager.taskmngr_backend.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException; // Usando uma exceção mais apropriada
import com.taskmanager.taskmngr_backend.model.ColunaModel;
import com.taskmanager.taskmngr_backend.model.dto.ColunaDTO;
import com.taskmanager.taskmngr_backend.repository.ColunaRepository;

@Service
public class ColunaService {

    @Autowired
    private ColunaRepository colunaRepository;

    // MÉTODO MODIFICADO: Agora lista colunas por ID de projeto
    public List<ColunaDTO> listarPorProjeto(String proj_id) {
        List<ColunaModel> colunas = colunaRepository.findByProjIdOrderByColOrdemAsc(proj_id);
        return colunas.stream()
                .map(c -> new ColunaDTO(c.getColId(), c.getColTitulo(), c.getColOrdem(), c.getProj_id()))
                .collect(Collectors.toList());
    }

    // MÉTODO MODIFICADO: Agora cria a coluna associada a um projeto
    public ColunaDTO criarColuna(ColunaDTO colunaDTO) {
        // A contagem agora é específica do projeto
        long totalColunasNoProjeto = colunaRepository.countByProj_id(colunaDTO.getProj_id());

        ColunaModel novaColuna = new ColunaModel();
        novaColuna.setColTitulo(colunaDTO.getTitulo());
        novaColuna.setProj_id(colunaDTO.getProj_id()); // Salva o ID do projeto
        novaColuna.setColOrdem((int) totalColunasNoProjeto);

        ColunaModel colunaSalva = colunaRepository.save(novaColuna);

        return new ColunaDTO(colunaSalva.getColId(), colunaSalva.getColTitulo(), colunaSalva.getColOrdem(), colunaSalva.getProj_id());
    }

    public ColunaDTO atualizarColuna(String col_id, ColunaDTO colunaDTO) {
        ColunaModel colunaExistente = colunaRepository.findById(col_id)
                .orElseThrow(() -> new ProjetoNaoEncontradoException("Coluna não encontrada", "Coluna com id " + col_id + " não foi encontrada"));

        colunaExistente.setColTitulo(colunaDTO.getTitulo());

        ColunaModel colunaAtualizada = colunaRepository.save(colunaExistente);

        return new ColunaDTO(colunaAtualizada.getColId(), colunaAtualizada.getColTitulo(), colunaAtualizada.getColOrdem(), colunaAtualizada.getProj_id());
    }

    public void deletarColuna(String col_id) {
        if (!colunaRepository.existsById(col_id)) {
            throw new ProjetoNaoEncontradoException("Coluna não encontrada", "Coluna com id " + col_id + " não foi encontrada");
        }
        colunaRepository.deleteById(col_id);
    }
}