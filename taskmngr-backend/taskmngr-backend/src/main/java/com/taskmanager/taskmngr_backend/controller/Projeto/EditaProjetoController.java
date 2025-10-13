package com.taskmanager.taskmngr_backend.controller.Projeto;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.AcessoNaoAutorizadoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projeto")
@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*")
public class EditaProjetoController {
    @Autowired
    private ProjetoService projetoService;

    @PutMapping("/atualizar/{projId}")
    public ResponseEntity<String> atualizar(@PathVariable String projId, @RequestBody ProjetoDTO dto, @AuthenticationPrincipal UsuarioModel usuarioLogado) {

        ProjetoModel projeto = projetoService.buscarPorId(projId)
                .orElseThrow(() -> new ProjetoNaoEncontradoException("Projeto não encontrado", "Projeto com id " + projId + " não foi encontrado"));

        boolean isMember = projeto.getEquipe().getUsuarios().stream()
                .anyMatch(membro -> membro.getUsuId().equals(usuarioLogado.getUsuId()));

        if (!isMember) {
            throw new AcessoNaoAutorizadoException("Acesso Negado", "Você não tem permissão para editar este projeto, pois não é membro da equipe.");
        }

        projeto.setProjNome(dto.getProjNome());
        projeto.setProjDescricao(dto.getProjDescricao());
        projeto.setProjStatus(dto.getProjStatus());

        projetoService.salvar(projeto);
        return ResponseEntity.ok("Projeto atualizado com sucesso");
    }
}