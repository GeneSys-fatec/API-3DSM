package com.taskmanager.taskmngr_backend.controller.Comentario;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.AdicionadorLinkComentario;
import com.taskmanager.taskmngr_backend.model.converter.ComentarioConverter;
import com.taskmanager.taskmngr_backend.model.dto.ComentarioDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ComentarioModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.Comentario.CriaComentarioService;

@RestController
@RequestMapping("/comentario")
public class CriaComentarioController {
    @Autowired
    private CriaComentarioService criaComentarioService;
    @Autowired
    private AdicionadorLinkComentario adicionadorLink;
    @Autowired
    private ComentarioConverter comentarioConverterService;

    @PostMapping("/cadastrar")
    public ResponseEntity<ComentarioDTO> cadastrarComentario(@RequestBody ComentarioDTO dto, @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        ComentarioModel comentario = comentarioConverterService.dtoParaModel(dto);
        if (usuarioLogado != null) {
            comentario.setUsuId(usuarioLogado.getUsuId());
            comentario.setUsuNome(usuarioLogado.getUsuNome());
            comentario.setComDataAtualizacao(new Date());
        }
        ComentarioModel salvo = criaComentarioService.adicionarComentario(comentario, usuarioLogado);
        ComentarioDTO dtoSalvo = comentarioConverterService.modelParaDto(salvo);
        adicionadorLink.adicionarLink(dtoSalvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoSalvo);
    }
}
