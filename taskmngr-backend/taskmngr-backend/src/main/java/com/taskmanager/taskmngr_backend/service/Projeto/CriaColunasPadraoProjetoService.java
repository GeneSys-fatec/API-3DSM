package com.taskmanager.taskmngr_backend.service.Projeto;

import com.taskmanager.taskmngr_backend.model.entidade.ColunaModel;
import com.taskmanager.taskmngr_backend.service.Coluna.CriaColunaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriaColunasPadraoProjetoService {

    @Autowired
    private CriaColunaService criaColunaService;


    public void criarColunasPadraoParaProjeto(String projetoId) {
        ColunaModel pendente = new ColunaModel();
        pendente.setColTitulo("Pendente");
        pendente.setProjId(projetoId);
        criaColunaService.criarColuna(pendente);

        ColunaModel emDesenvolvimento = new ColunaModel();
        emDesenvolvimento.setColTitulo("Em Desenvolvimento");
        emDesenvolvimento.setProjId(projetoId);
        criaColunaService.criarColuna(emDesenvolvimento);

        ColunaModel concluida = new ColunaModel();
        concluida.setColTitulo("Concluída");
        concluida.setProjId(projetoId);
        criaColunaService.criarColuna(concluida);
    }
}