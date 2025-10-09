package com.taskmanager.taskmngr_backend.controller.Tarefa;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.taskmanager.taskmngr_backend.model.AdicionadorLinkComentario;
import com.taskmanager.taskmngr_backend.model.converter.ComentarioConverter;
import com.taskmanager.taskmngr_backend.model.dto.ComentarioDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ComentarioModel;
import com.taskmanager.taskmngr_backend.service.ComentarioService;

@RestController
@RequestMapping("/comentario")
@CrossOrigin(origins = "http://localhost:5173")
public class EditaComentarioController {
    @Autowired
    private ComentarioService comentarioService;
    @Autowired
    private AdicionadorLinkComentario adicionadorLink;
    @Autowired
    private ComentarioConverter comentarioConverterService;

    @PutMapping("/atualizar/{comId}")
    public ResponseEntity<ComentarioDTO> atualizarComentario(@PathVariable String comId, @RequestBody ComentarioDTO dto) {
        dto.setComId(comId);
        ComentarioModel existente = comentarioService.listarPorId(comId).orElse(null);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        existente.setComMensagem(dto.getComMensagem());
        existente.setComDataAtualizacao(new Date());
        ComentarioModel atualizado = comentarioService.atualizarComentario(existente);
        ComentarioDTO dtoAtualizado = comentarioConverterService.modelParaDto(atualizado);
        adicionadorLink.adicionarLink(dtoAtualizado);
        return ResponseEntity.ok(dtoAtualizado);
    }
}
