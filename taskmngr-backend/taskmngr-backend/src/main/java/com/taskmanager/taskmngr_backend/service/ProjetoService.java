package com.taskmanager.taskmngr_backend.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.taskmanager.taskmngr_backend.model.ColunaModel;
import com.taskmanager.taskmngr_backend.model.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.ColunaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taskmanager.taskmngr_backend.model.ProjetoModel;
import com.taskmanager.taskmngr_backend.repository.ProjetoRepository;

@Service
public class ProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ColunaRepository colunaRepository;

    public ProjetoModel criarNovoProjeto(ProjetoModel projeto) {
        ProjetoModel projetoSalvo = projetoRepository.save(projeto);
        String novoProjetoId = projetoSalvo.getProj_id();

        // Logica de criação das colunas padrão
        ColunaModel pendente = new ColunaModel();
        pendente.setColTitulo("Pendente");
        pendente.setColOrdem(0);
        pendente.setProj_id(novoProjetoId);

        ColunaModel emDesenvolvimento = new ColunaModel();
        emDesenvolvimento.setColTitulo("Em Desenvolvimento");
        emDesenvolvimento.setColOrdem(1);
        emDesenvolvimento.setProj_id(novoProjetoId);

        ColunaModel concluida = new ColunaModel();
        concluida.setColTitulo("Concluída");
        concluida.setColOrdem(2);
        concluida.setProj_id(novoProjetoId);

        List<ColunaModel> colunasPadrao = Arrays.asList(pendente, emDesenvolvimento, concluida);
        colunaRepository.saveAll(colunasPadrao);

        return projetoSalvo;
    }

    public List<ProjetoModel> listarTodas() {
        return projetoRepository.findAll();
    }

    public List<ProjetoModel> listarPorUsuario(UsuarioModel usuario) {
        if (usuario == null || usuario.getUsu_id() == null) {
            return Collections.emptyList();
        }
        return projetoRepository.findByUsuarioIdsContaining(usuario.getUsu_id());}

    public Optional<ProjetoModel> buscarPorId(String id) {
        return projetoRepository.findById(id);
    }

    public ProjetoModel salvar(ProjetoModel tarefa) {
        return projetoRepository.save(tarefa);
    }

    public ProjetoModel atualizar(ProjetoModel tarefa) {
        return projetoRepository.save(tarefa);
    }

    public void deletar(String id) {
        projetoRepository.deleteById(id);
    }
}
