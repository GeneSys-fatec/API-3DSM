package com.taskmanager.taskmngr_backend.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.dto.ColunaDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ColunaModel;
import com.taskmanager.taskmngr_backend.repository.ColunaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DuplicateKeyException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.coluna.NomeDeColunaJaExisteException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.coluna.LimiteDeColunasExcedidoException;

@Service
public class ColunaService {

    @Autowired
    private ColunaRepository colunaRepository;

    private static final String DEFAULT_COLUMN_BASE = "Nova Coluna";

    public ColunaModel criarColuna(ColunaModel novaColuna) {
        String projId = novaColuna.getProjId();
        if (projId == null || projId.trim().isEmpty()) {
            throw new IllegalArgumentException("projId é obrigatório para criar coluna");
        }

        String titulo = novaColuna.getColTitulo() == null ? "" : novaColuna.getColTitulo().trim();

        long totalNoProjeto = colunaRepository.countByProjId(projId);
        if (totalNoProjeto >= 10) {
            throw new LimiteDeColunasExcedidoException("Limite de colunas atingido", "Não é possível criar mais que 10 colunas neste projeto.");
        }

        if (titulo.isEmpty() || titulo.equalsIgnoreCase(DEFAULT_COLUMN_BASE)) {
            titulo = generateUniqueDefaultTitle(projId, DEFAULT_COLUMN_BASE);
            novaColuna.setColTitulo(titulo);
        } else {
            if (colunaRepository.existsByProjIdAndColTitulo(projId, titulo)) {
                throw new NomeDeColunaJaExisteException("Título de coluna já existe", "Já existe uma coluna com esse título neste projeto.");
            }
            novaColuna.setColTitulo(titulo);
        }

        novaColuna.setColOrdem((int) totalNoProjeto);

        try {
            return colunaRepository.save(novaColuna);
        } catch (DuplicateKeyException ex) {
            throw new NomeDeColunaJaExisteException("Título de coluna já existe", "Já existe uma coluna com esse título neste projeto.");
        }
    }

    public ColunaDTO atualizarColuna(String col_id, ColunaDTO colunaDTO) {
        ColunaModel colunaExistente = colunaRepository.findById(col_id)
                .orElseThrow(() -> new ProjetoNaoEncontradoException("Coluna não encontrada", "Coluna com id " + col_id + " não foi encontrada"));

        String novoTitulo = colunaDTO.getTitulo() == null ? "" : colunaDTO.getTitulo().trim();
        if (!novoTitulo.isEmpty() && !novoTitulo.equals(colunaExistente.getColTitulo())) {
            long duplicatas = colunaRepository.countByProjIdAndColTituloAndColIdNot(colunaExistente.getProjId(), novoTitulo, colunaExistente.getColId());
            if (duplicatas > 0) {
                throw new NomeDeColunaJaExisteException("Título de coluna já existe", "Já existe uma coluna com esse título neste projeto.");
            }
            colunaExistente.setColTitulo(novoTitulo);
        }

        try {
            ColunaModel colunaAtualizada = colunaRepository.save(colunaExistente);
            return new ColunaDTO(colunaAtualizada.getColId(), colunaAtualizada.getColTitulo(), colunaAtualizada.getColOrdem(), colunaAtualizada.getProjId());
        } catch (DuplicateKeyException ex) {
            throw new NomeDeColunaJaExisteException("Título de coluna já existe", "Já existe uma coluna com esse título neste projeto.");
        }
    }

    private String generateUniqueDefaultTitle(String projId, String base) {
        String candidate = base;
        int suffix = 1;
        while (colunaRepository.countByProjIdAndColTitulo(projId, candidate) > 0) {
            suffix++;
            candidate = base + " " + suffix;
            if (suffix > 1000) break;
        }
        return candidate;
    }

    public List<ColunaDTO> listarPorProjeto(String projId) {
        List<ColunaModel> colunas = colunaRepository.findByProjIdOrderByColOrdemAsc(projId);
        return colunas.stream()
                .map(c -> new ColunaDTO(c.getColId(), c.getColTitulo(), c.getColOrdem(), c.getProjId()))
                .collect(Collectors.toList());
    }

    public void deletarColuna(String col_id) {
        if (!colunaRepository.existsById(col_id)) {
            throw new ProjetoNaoEncontradoException("Coluna não encontrada", "Coluna com id " + col_id + " não foi encontrada");
        }
        colunaRepository.deleteById(col_id);
    }

    @Transactional // Garante que ou tudo salva, ou nada salva
    public void atualizarOrdemColunas(List<Map<String, Object>> colunasOrdem) {
        if (colunasOrdem == null || colunasOrdem.isEmpty()) {
            return;
        }
        List<String> ids = colunasOrdem.stream()
                .map(map -> (String) map.get("id"))
                .collect(Collectors.toList());
        List<ColunaModel> colunas = colunaRepository.findAllById(ids);
        Map<String, Integer> ordemMap = colunasOrdem.stream()
                .collect(Collectors.toMap(
                        map -> (String) map.get("id"),
                        map -> (Integer) map.get("ordem")
                ));
        for (ColunaModel coluna : colunas) {
            Integer novaOrdem = ordemMap.get(coluna.getColId());
            if (novaOrdem != null) {
                coluna.setColOrdem(novaOrdem);
            }
        }
        colunaRepository.saveAll(colunas);
    }
}