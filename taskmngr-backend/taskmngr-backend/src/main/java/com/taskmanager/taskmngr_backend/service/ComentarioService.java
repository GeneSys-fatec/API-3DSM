package com.taskmanager.taskmngr_backend.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.comentário.ComentarioEmBrancoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.comentário.ConteudoInapropriadoException;
import com.taskmanager.taskmngr_backend.model.converter.ComentarioConverter;
import com.taskmanager.taskmngr_backend.model.entidade.ComentarioModel;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.ComentarioRepository;
import com.taskmanager.taskmngr_backend.repository.TarefaRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ComentarioService {

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private ComentarioRepository repository;

    @Autowired
    private TarefaService tarefaService;

    @Autowired ComentarioConverter converter;

    @Value("${app.validation.palavras-proibidas:palavrao1,ofensa,improprio}")
    private String palavrasProibidasConfig;

    private Set<String> PALAVRAS_PROIBIDAS;

    @PostConstruct
    public void init() {
        PALAVRAS_PROIBIDAS = new HashSet<>(Arrays.asList(palavrasProibidasConfig.split(",")));
    }
    public ComentarioModel adicionarComentario(ComentarioModel comentario, UsuarioModel usuarioLogado) {
        validarComentario(comentario);
        ComentarioModel comentarioSalvo = repository.save(comentario);

        Optional<TarefaModel> tarefaOpt = tarefaService.buscarPorId(comentario.getTarId());
        if (tarefaOpt.isPresent()) {
            TarefaModel tarefa = tarefaOpt.get();

            if (!tarefa.getUsuId().equals(usuarioLogado.getUsuId())) {
                notificacaoService.criarNotificacaoComentario(
                    usuarioLogado.getUsuId(),
                    tarefa.getUsuId(),
                    usuarioLogado.getUsuNome(),
                    tarefa.getTarId()
                );
            }
        }

        return comentarioSalvo;
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
        validarComentario(comentario);
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

    private void validarConteudo(String mensagem) {
        String mensagemNormalizada = mensagem.toLowerCase();

        for (String palavra : PALAVRAS_PROIBIDAS) {
            if (mensagemNormalizada.contains(palavra)) {
                throw new ConteudoInapropriadoException("Mensagem do comentário contém palavra imprópria","O comentário contém palavras impróprias e não pode ser salvo.");
            }
        }
    }

    private void validarComentario(ComentarioModel comentario) {
        String mensagem = comentario.getComMensagem();

        if (mensagem == null || mensagem.trim().isEmpty()) {
            throw new ComentarioEmBrancoException("Mensagem do comentário em branco", "A mensagem não pode estar em branco!");
        }
        validarConteudo(mensagem);
    }
}
