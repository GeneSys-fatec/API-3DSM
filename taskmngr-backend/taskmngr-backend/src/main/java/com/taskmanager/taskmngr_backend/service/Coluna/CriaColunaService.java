package com.taskmanager.taskmngr_backend.service.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.coluna.LimiteDeColunasExcedidoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.coluna.NomeDeColunaJaExisteException;
import com.taskmanager.taskmngr_backend.model.entidade.ColunaModel;
import com.taskmanager.taskmngr_backend.repository.ColunaRepository;

@Service
public class CriaColunaService {
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
}