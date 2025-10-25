package com.taskmanager.taskmngr_backend.service.Comentario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.entidade.ComentarioModel;
import com.taskmanager.taskmngr_backend.repository.ComentarioRepository;

@Service
public class EditaComentarioService {

    @Autowired
    private ComentarioRepository repository;

    @Autowired
    private ValidaComentarioService validaComentarioService;

    public ComentarioModel atualizarComentario(ComentarioModel comentario) {
        validaComentarioService.validarComentario(comentario);
        return repository.save(comentario);
    }
}
