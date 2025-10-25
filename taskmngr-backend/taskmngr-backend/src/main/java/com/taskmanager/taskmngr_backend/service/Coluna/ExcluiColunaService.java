package com.taskmanager.taskmngr_backend.service.Coluna;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.taskmanager.taskmngr_backend.repository.ColunaRepository;

@Service
public class ExcluiColunaService {
    @Autowired
    private ColunaRepository colunaRepository;

    public void deletarColuna(String col_id) {
        if (!colunaRepository.existsById(col_id)) {
            throw new ProjetoNaoEncontradoException("Coluna não encontrada", "Coluna com id " + col_id + " não foi encontrada");
        }
        colunaRepository.deleteById(col_id);
    }
}