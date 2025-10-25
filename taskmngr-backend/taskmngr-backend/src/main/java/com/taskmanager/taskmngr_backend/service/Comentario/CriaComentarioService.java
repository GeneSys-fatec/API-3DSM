package com.taskmanager.taskmngr_backend.service.Comentario;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.entidade.ComentarioModel;
import com.taskmanager.taskmngr_backend.model.entidade.ResponsavelTarefa;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.ComentarioRepository;
import com.taskmanager.taskmngr_backend.service.Notificacao.CriaNotificacaoService;
import com.taskmanager.taskmngr_backend.service.Tarefa.BuscaTarefaService;

@Service
public class CriaComentarioService {
    @Autowired
    private CriaNotificacaoService criaNotificacaoService;

    @Autowired
    private ComentarioRepository repository;

    @Autowired
    private BuscaTarefaService buscaTarefaService;

    @Autowired
    private ValidaComentarioService validaComentarioService;

    public ComentarioModel adicionarComentario(ComentarioModel comentario, UsuarioModel usuarioLogado) {
        validaComentarioService.validarComentario(comentario);
        ComentarioModel comentarioSalvo = repository.save(comentario);

        Optional<TarefaModel> tarefaOpt = buscaTarefaService.buscarPorId(comentario.getTarId());
        if (tarefaOpt.isPresent()) {
            TarefaModel tarefa = tarefaOpt.get();
            if (tarefa.getResponsaveis() != null && !tarefa.getResponsaveis().isEmpty()) {
                for (ResponsavelTarefa responsavel : tarefa.getResponsaveis()) {

                    if (!responsavel.getUsuId().equals(usuarioLogado.getUsuId())) {
                        criaNotificacaoService.criarNotificacaoComentario(
                                usuarioLogado.getUsuId(),
                                responsavel.getUsuId(),
                                usuarioLogado.getUsuNome(),
                                tarefa.getTarId()
                        );
                    }
                }
            }
        }

        return comentarioSalvo;
    }
}
