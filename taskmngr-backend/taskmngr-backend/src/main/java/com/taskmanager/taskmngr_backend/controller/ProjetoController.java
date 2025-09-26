package com.taskmanager.taskmngr_backend.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.AdicionadorLinkProjetos;
import com.taskmanager.taskmngr_backend.model.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.UsuarioModel;
import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;
import com.taskmanager.taskmngr_backend.service.ProjetoConverterService;
import com.taskmanager.taskmngr_backend.service.ProjetoService;

@RestController
@RequestMapping("/projeto")
@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;
    @Autowired
    private AdicionadorLinkProjetos adicionadorLink;
    @Autowired
    private ProjetoConverterService projetoConverterService;

    @GetMapping("/meus-projetos")
    public ResponseEntity<List<ProjetoDTO>> listarProjetosDoUsuario(@AuthenticationPrincipal UsuarioModel usuario) {
        List<ProjetoModel> projetos = projetoService.listarPorUsuario(usuario);
        List<ProjetoDTO> dtos = projetos.stream()
                .map(projetoConverterService::modelParaDto)
                .collect(Collectors.toList());
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{proj_id}")
    public ResponseEntity<ProjetoDTO> buscarPorId(@PathVariable String proj_id) {
        ProjetoModel projeto = projetoService.buscarPorId(proj_id)
            .orElseThrow(() -> new ProjetoNaoEncontradoException(
                "Projeto não encontrado",
                "Projeto com id " + proj_id + " não foi encontrado"
            ));

        ProjetoDTO dto = projetoConverterService.modelParaDto(projeto);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    // ENDPOINT para admin. (talvez remover depois)
    @GetMapping("/listar")
    public ResponseEntity<List<ProjetoDTO>> listarTodas() {
        List<ProjetoModel> projetos= projetoService.listarTodas();
        List<ProjetoDTO> dtos = projetos.stream().map(projetoConverterService::modelParaDto).collect(Collectors.toList());
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    // ... dentro da classe ProjetoController

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarProjeto(@RequestBody ProjetoDTO dto, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        ProjetoModel projeto = projetoConverterService.dtoParaModel(dto);

        if (usuarioLogado != null) {
            projeto.setUsuarioIds(List.of(usuarioLogado.getUsu_id()));
        }
        projetoService.criarNovoProjeto(projeto);

        return ResponseEntity.status(HttpStatus.CREATED).body("Projeto cadastrado com sucesso!");
    }


    @PutMapping("/atualizar/{proj_id}")
    public ResponseEntity<String> atualizar(@PathVariable String proj_id, @RequestBody ProjetoDTO dto) {
        Optional<ProjetoModel> projetoExistente = projetoService.buscarPorId(proj_id);
        if (projetoExistente.isEmpty()) {
            throw new ProjetoNaoEncontradoException(
                    "Projeto não encontrado",
                    "Projeto com id " + proj_id + " não foi encontrado"
            );
        }
        ProjetoModel p = projetoExistente.get();
        p.setProj_nome(dto.getProj_nome());
        p.setProj_descricao(dto.getProj_descricao());
        p.setProj_dataCriacao(dto.getProj_dataCriacao());
        p.setProj_dataAtualizacao(dto.getProj_dataAtualizacao());
        projetoService.salvar(p);
        return ResponseEntity.ok("Projeto atualizado com sucesso");
    }

    @DeleteMapping("/apagar/{proj_id}")
    public ResponseEntity<String> apagarProjeto(@PathVariable String proj_id) {
        Optional<ProjetoModel> projetoExistente = projetoService.buscarPorId(proj_id);
        if (projetoExistente.isEmpty()){
            throw new ProjetoNaoEncontradoException("Projeto não encontrado",
                "Não foi possivel deletar o projeto com id " + proj_id + ", não foi encontrado");
        } 
        projetoService.deletar(proj_id);
        return ResponseEntity.ok("Projeto apagado com sucesso");
    }

    @ExceptionHandler(ProjetoNaoEncontradoException.class)
    public ResponseEntity<String> handleProjetoNaoEncontrado(ProjetoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMensagem());
    }
}
