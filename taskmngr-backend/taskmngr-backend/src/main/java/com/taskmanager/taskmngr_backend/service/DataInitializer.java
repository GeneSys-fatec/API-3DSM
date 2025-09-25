package com.taskmanager.taskmngr_backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.taskmanager.taskmngr_backend.model.ColunaModel;
import com.taskmanager.taskmngr_backend.repository.ColunaRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ColunaRepository colunaRepository;

    @Override
    public void run(String... args) throws Exception {  
        if (colunaRepository.count() == 0) {
            System.out.println("Nenhuma coluna encontrada, criando colunas padrão...");

            // Cria as três colunas padrão
            ColunaModel pendente = new ColunaModel();
            pendente.setColTitulo("Pendente");
            pendente.setColOrdem(0);

            ColunaModel emDesenvolvimento = new ColunaModel();
            emDesenvolvimento.setColTitulo("Em Desenvolvimento");
            emDesenvolvimento.setColOrdem(1);

            ColunaModel concluida = new ColunaModel();
            concluida.setColTitulo("Concluída");
            concluida.setColOrdem(2);

            List<ColunaModel> colunasPadrao = Arrays.asList(pendente, emDesenvolvimento, concluida);

            colunaRepository.saveAll(colunasPadrao);

            System.out.println("Colunas padrão criadas com sucesso!");
        } else {
            System.out.println("Colunas já existem no banco de dados. Nenhuma ação necessária.");
        }
    }
}
