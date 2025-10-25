package com.taskmanager.taskmngr_backend.service.Coluna;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.coluna.NomeDeColunaJaExisteException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.dto.ColunaDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ColunaModel;
import com.taskmanager.taskmngr_backend.repository.ColunaRepository;

@Service
public class EditaColunaService {
    @Autowired
    private ColunaRepository colunaRepository;

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



    @Transactional
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