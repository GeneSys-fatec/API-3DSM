package com.taskmanager.taskmngr_backend.controller.Comentario;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.AdicionadorLinkComentario;
import com.taskmanager.taskmngr_backend.model.converter.ComentarioConverter;
import com.taskmanager.taskmngr_backend.model.dto.ComentarioDTO;
import com.taskmanager.taskmngr_backend.service.Comentario.BuscaComentarioService;

@RestController
@RequestMapping("/comentario")
public class BuscaComentarioController {
    @Autowired
    private BuscaComentarioService buscaComentarioService;
    @Autowired
    private AdicionadorLinkComentario adicionadorLink;
    @Autowired
    private ComentarioConverter comentarioConverterService;

@GetMapping("/listar")
    public ResponseEntity<List<ComentarioDTO>> listarTodos() {
        List<ComentarioDTO> dtos = buscaComentarioService.listarTodos().stream()
                .map(comentarioConverterService::modelParaDto).collect(Collectors.toList());
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/tarefa/{tarId}")
    public ResponseEntity<List<ComentarioDTO>> listarComentariosPorTarefa(@PathVariable String tarId) {
        List<ComentarioDTO> dtos = buscaComentarioService.listarPorTarefa(tarId).stream()
                .map(comentarioConverterService::modelParaDto).collect(Collectors.toList());
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{comId}")
    public ResponseEntity<ComentarioDTO> listarPorId(@PathVariable String comId) {
        ComentarioDTO dto = buscaComentarioService.listarPorId(comId).map(comentarioConverterService::modelParaDto)
                .orElse(null);
        if (dto != null) {
            adicionadorLink.adicionarLink(dto);
        }
        return ResponseEntity.ok(dto);
    }
}
