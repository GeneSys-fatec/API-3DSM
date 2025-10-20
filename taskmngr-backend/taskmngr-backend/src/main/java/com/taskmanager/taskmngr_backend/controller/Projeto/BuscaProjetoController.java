package com.taskmanager.taskmngr_backend.controller.Projeto;

import java.util.List;
import java.util.stream.Collectors;

import com.taskmanager.taskmngr_backend.model.AdicionadorLinkUsuario;
import com.taskmanager.taskmngr_backend.model.converter.UsuarioConverter;
import com.taskmanager.taskmngr_backend.model.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.projetos.ProjetoNaoEncontradoException;
import com.taskmanager.taskmngr_backend.model.AdicionadorLinkProjetos;
import com.taskmanager.taskmngr_backend.model.converter.ProjetoConverter;
import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.ProjetoService;

@RestController
@RequestMapping("/projeto")
@CrossOrigin(origins = "http://localhost:5173/", allowedHeaders = "*")
public class BuscaProjetoController {
    @Autowired
    private ProjetoService projetoService;
    @Autowired
    private AdicionadorLinkProjetos adicionadorLink;
    @Autowired
    private ProjetoConverter projetoConverterService;
    @Autowired
    private UsuarioConverter usuarioConverter;
    @Autowired
    private AdicionadorLinkUsuario adicionadorLinkUsuario;

    @GetMapping("/meus-projetos")
    public ResponseEntity<List<ProjetoDTO>> listarProjetosDoUsuario(@AuthenticationPrincipal UsuarioModel usuario) {
        List<ProjetoModel> projetos = projetoService.listarPorUsuario(usuario);
        List<ProjetoDTO> dtos = projetos.stream()
                .map(projetoConverterService::modelParaDto)
                .collect(Collectors.toList());
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{projId}")
    public ResponseEntity<ProjetoDTO> buscarPorId(@PathVariable String projId) {
        ProjetoModel projeto = projetoService.buscarPorId(projId)
                .orElseThrow(() -> new ProjetoNaoEncontradoException(
                        "Projeto não encontrado",
                        "Projeto com id " + projId + " não foi encontrado"));

        ProjetoDTO dto = projetoConverterService.modelParaDto(projeto);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ProjetoDTO>> listarTodas() {
        List<ProjetoModel> projetos = projetoService.listarTodas();
        List<ProjetoDTO> dtos = projetos.stream().map(projetoConverterService::modelParaDto)
                .collect(Collectors.toList());
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{projId}/membros")
    public ResponseEntity<List<UsuarioDTO>> listarMembrosDoProjeto(@PathVariable String projId) {
        List<UsuarioModel> membros = projetoService.buscarMembrosDoProjeto(projId);

        if (membros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<UsuarioDTO> dtos = membros.stream()
                .map(usuarioConverter::modelParaDto)
                .collect(Collectors.toList());

        adicionadorLinkUsuario.adicionarLink(dtos);

        return ResponseEntity.ok(dtos);
    }

    @ExceptionHandler(ProjetoNaoEncontradoException.class)
    public ResponseEntity<String> handleProjetoNaoEncontrado(ProjetoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMensagem());
    }
}
