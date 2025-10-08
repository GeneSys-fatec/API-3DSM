package com.taskmanager.taskmngr_backend.controller.Projeto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoSemInformacaoException;
import com.taskmanager.taskmngr_backend.model.converter.ProjetoConverter;
import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.ProjetoService;

@RestController
@RequestMapping("/projeto")
@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*")
public class CriaProjetoController {
    @Autowired
    private ProjetoService projetoService;
    @Autowired
    private ProjetoConverter projetoConverter;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarProjeto(
            @RequestBody ProjetoDTO dto,
            @AuthenticationPrincipal UsuarioModel usuarioLogado) {

        if (dto.getProjNome() == null || dto.getProjNome().isBlank() ||
                dto.getProjDescricao() == null || dto.getProjDescricao().isBlank()) {
            throw new ProjetoSemInformacaoException(
                    "Erro ao cadastrar projeto",
                    "Nome e descrição do projeto são obrigatórios.");
        }

        ProjetoModel projeto = projetoConverter.dtoParaModel(dto);

        if (usuarioLogado != null) {
            projeto.setUsuarioIds(List.of(usuarioLogado.getUsuId()));
        }
        projetoService.criarNovoProjeto(projeto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Projeto cadastrado com sucesso!");
    }

    @ExceptionHandler(ProjetoNaoEncontradoException.class)
    public ResponseEntity<String> handleProjetoNaoEncontrado(ProjetoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMensagem());
    }
}
