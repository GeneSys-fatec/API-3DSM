package com.taskmanager.taskmngr_backend.controller.Projeto;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.equipes.EquipeSemInformacaoException;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoSemInformacaoException;
import com.taskmanager.taskmngr_backend.model.converter.ProjetoConverter;
import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.taskmngr_backend.service.Projeto.CriaProjetoService;

@RestController
@RequestMapping("/projeto")
@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*")
public class CriaProjetoController {
    @Autowired
    private CriaProjetoService criaProjetoService;
    @Autowired
    private ProjetoConverter projetoConverter;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarProjeto(
            @RequestBody ProjetoDTO dto,
            @AuthenticationPrincipal UsuarioModel usuarioLogado) {

        if (dto.getProjNome() == null || dto.getProjNome().isBlank()) {
            throw new ProjetoSemInformacaoException("Erro ao cadastrar projeto", "Nome do projeto é obrigatório.");
        }

        if (dto.getEquipeId() == null || dto.getEquipeId().isBlank()) {
            throw new EquipeSemInformacaoException("Erro ao cadastrar projeto", "A equipe do projeto é obrigatória.");
        }

        ProjetoModel projeto = projetoConverter.dtoParaModel(dto);
        criaProjetoService.criarNovoProjeto(projeto, dto.getEquipeId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Projeto cadastrado com sucesso!");
    }
}