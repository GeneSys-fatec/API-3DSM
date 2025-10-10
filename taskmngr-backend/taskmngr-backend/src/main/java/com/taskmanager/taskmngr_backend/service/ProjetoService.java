package com.taskmanager.taskmngr_backend.service;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.EquipeNaoEncontradaException;
import com.taskmanager.taskmngr_backend.model.entidade.ColunaModel;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;
import com.taskmanager.taskmngr_backend.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ColunaService colunaService;

    @Autowired
    private EquipeRepository equipeRepository;

    public ProjetoModel criarNovoProjeto(ProjetoModel projeto, String equipeId) {
        EquipeModel equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new EquipeNaoEncontradaException("Equipe não encontrada", "A equipe com ID " + equipeId + " não existe."));

        projeto.setEquipe(equipe);
        ProjetoModel projetoSalvo = projetoRepository.save(projeto);

        equipe.getProjetos().add(projetoSalvo);
        equipeRepository.save(equipe);

        criarColunasPadraoParaProjeto(projetoSalvo.getProjId());

        return projetoSalvo;
    }

    public List<ProjetoModel> listarPorUsuario(UsuarioModel usuario) {
        if (usuario == null) {
            return Collections.emptyList();
        }

        List<EquipeModel> equipesDoUsuario = equipeRepository.findByUsuariosUsuId(usuario.getUsuId());

        if (equipesDoUsuario.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> idsDasEquipes = equipesDoUsuario.stream()
                .map(EquipeModel::getEquId)
                .collect(Collectors.toList());

        return projetoRepository.findByEquipeEquIdIn(idsDasEquipes);
    }

    public List<ProjetoModel> listarTodas() { return projetoRepository.findAll(); }
    public Optional<ProjetoModel> buscarPorId(String id) { return projetoRepository.findById(id); }
    public ProjetoModel salvar(ProjetoModel projeto) { return projetoRepository.save(projeto); }
    public void deletar(String id) { projetoRepository.deleteById(id); }

    private void criarColunasPadraoParaProjeto(String projetoId) {
        ColunaModel pendente = new ColunaModel();
        pendente.setColTitulo("Pendente");
        pendente.setProjId(projetoId);
        colunaService.criarColuna(pendente);

        ColunaModel emDesenvolvimento = new ColunaModel();
        emDesenvolvimento.setColTitulo("Em Desenvolvimento");
        emDesenvolvimento.setProjId(projetoId);
        colunaService.criarColuna(emDesenvolvimento);

        ColunaModel concluida = new ColunaModel();
        concluida.setColTitulo("Concluída");
        concluida.setProjId(projetoId);
        colunaService.criarColuna(concluida);
    }
}