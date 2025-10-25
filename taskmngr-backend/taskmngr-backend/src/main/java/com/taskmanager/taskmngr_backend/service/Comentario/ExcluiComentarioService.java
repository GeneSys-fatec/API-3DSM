package com.taskmanager.taskmngr_backend.service.Comentario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.entidade.ComentarioModel;
import com.taskmanager.taskmngr_backend.repository.ComentarioRepository;

@Service
public class ExcluiComentarioService {

    @Autowired
    private ComentarioRepository repository;

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