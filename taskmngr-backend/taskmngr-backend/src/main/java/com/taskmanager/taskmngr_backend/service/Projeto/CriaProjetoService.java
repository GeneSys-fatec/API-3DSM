package com.taskmanager.taskmngr_backend.service.Projeto;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.EquipeNaoEncontradaException;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;
import com.taskmanager.taskmngr_backend.repository.ProjetoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CriaProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private CriaColunasPadraoProjetoService criaColunasPadraoProjetoService;

    @Autowired
    private EquipeRepository equipeRepository;

    public ProjetoModel criarNovoProjeto(ProjetoModel projeto, String equipeId) {
        EquipeModel equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new EquipeNaoEncontradaException("Equipe não encontrada", "A equipe com ID " + equipeId + " não existe."));

        projeto.setEquipe(equipe);
        ProjetoModel projetoSalvo = projetoRepository.save(projeto);

        equipe.getProjetos().add(projetoSalvo);
        equipeRepository.save(equipe);

        criaColunasPadraoProjetoService.criarColunasPadraoParaProjeto(projetoSalvo.getProjId());

        return projetoSalvo;
    }
}