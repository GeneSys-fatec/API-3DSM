package com.taskmanager.taskmngr_backend.controller.Projeto;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.taskmngr_backend.service.Projeto.BuscaProjetoService;
import com.taskmanager.taskmngr_backend.service.Projeto.ExcluiProjetoService;

@RestController
@RequestMapping("/projeto")
@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*")
public class ExcluiProjetoController {
    @Autowired
    private ExcluiProjetoService excluiProjetoService;

    @Autowired
    private BuscaProjetoService buscaProjetoService;

    @DeleteMapping("/apagar/{projId}")
    public ResponseEntity<String> apagarProjeto(@PathVariable String projId, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        ProjetoModel projeto = buscaProjetoService.buscarPorId(projId)
                .orElseThrow(() -> new ProjetoNaoEncontradoException("Projeto não encontrado", "Não foi possível deletar o projeto com id " + projId + ", pois não foi encontrado"));


        List<UsuarioModel> membros = buscaProjetoService.buscarMembrosDoProjeto(projId);

        boolean isMember = membros.stream()
                .anyMatch(membro -> membro.getUsuId().equals(usuarioLogado.getUsuId()));


        if (!isMember) {
            throw new AcessoNaoAutorizadoException("Acesso Negado", "Você não tem permissão para apagar este projeto, pois não é membro da equipe.");
        }

        excluiProjetoService.deletar(projId);
        return ResponseEntity.ok("Projeto apagado com sucesso");
    }
}