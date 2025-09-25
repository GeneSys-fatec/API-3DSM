package com.taskmanager.taskmngr_backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.ColunaModel;
import com.taskmanager.taskmngr_backend.model.dto.ColunaDTO;
import com.taskmanager.taskmngr_backend.repository.ColunaRepository;

@Service
public class ColunaService {
    
    @Autowired
    private ColunaRepository colunaRepository;

    public List<ColunaDTO> listarTodas() {
        List<ColunaModel> colunas = colunaRepository.findAllByOrderByColOrdemAsc();
        return colunas.stream()
                .map(c -> new ColunaDTO(c.getColId(), c.getColTitulo(), c.getColOrdem()))
                .collect(Collectors.toList());
    }
    public ColunaDTO criarColuna(ColunaDTO colunaDTO) {
        long totalColunas = colunaRepository.count();
        
        ColunaModel novaColuna = new ColunaModel();
        novaColuna.setColTitulo(colunaDTO.getTitulo());
        novaColuna.setColOrdem((int) totalColunas);
        
        ColunaModel colunaSalva = colunaRepository.save(novaColuna);
        
        return new ColunaDTO(colunaSalva.getColId(), colunaSalva.getColTitulo(), colunaSalva.getColOrdem());
    }

    public ColunaDTO atualizarColuna(String col_id, ColunaDTO colunaDTO) {
        ColunaModel colunaExistente = colunaRepository.findById(col_id)
                .orElseThrow(() -> new UsernameNotFoundException("Coluna não encontrada com id: " + col_id));

        colunaExistente.setColTitulo(colunaDTO.getTitulo());
        
        ColunaModel colunaAtualizada = colunaRepository.save(colunaExistente);
        
        return new ColunaDTO(colunaAtualizada.getColId(), colunaAtualizada.getColTitulo(), colunaAtualizada.getColOrdem());
    }

    public void deletarColuna(String col_id) {
        if (!colunaRepository.existsById(col_id)) {
            throw new UsernameNotFoundException("Coluna não encontrada com id: " + col_id);
        }
        colunaRepository.deleteById(col_id);
    }
}
