package com.taskmanager.taskmngr_backend.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.converter.ComentarioConverter;
import com.taskmanager.taskmngr_backend.model.entidade.ComentarioModel;
import com.taskmanager.taskmngr_backend.repository.ComentarioRepository;

@Service
public class ComentarioService {
    @Autowired
    private ComentarioRepository repository;

    @Autowired ComentarioConverter converter;

    public ComentarioModel adicionarComentario(ComentarioModel comentario) {
        return repository.save(comentario);
    }

    public List<ComentarioModel> listarPorTarefa(String tarId) {
        if (tarId == null || tarId.isBlank()) {
            return Collections.emptyList();
        }
        return repository.findBytarId(tarId);    }

    public List<ComentarioModel> listarTodos() {
        return repository.findAll();
    }

    public Optional<ComentarioModel> listarPorId(String comId) {
        return repository.findById(comId);
    }

    public ComentarioModel atualizarComentario(ComentarioModel comentario) {
        return repository.save(comentario);
    }

    public void deletarRespostaComentario(String comId) {
        List<ComentarioModel> respostas = repository.findByRespostaComentario(comId);
        if (respostas != null && !respostas.isEmpty()) {
            for (ComentarioModel resposta : respostas) {
                deletarRespostaComentario(resposta.getComId());
            }
        }
        repository.deleteById(comId);
    }
}
