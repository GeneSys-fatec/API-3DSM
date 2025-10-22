package com.taskmanager.taskmngr_backend.controller.Usuario;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.dto.usuario.UsuarioDTO;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class EditaUsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PutMapping("/atualizar/{usuId}")
    public ResponseEntity<String> atualizarUsuario(@PathVariable String usuId, @RequestBody UsuarioDTO dto) {
        Optional<UsuarioModel> usuarioExistente = usuarioService.buscarPorId(usuId);
      
        if (usuarioExistente.isPresent()) {
            UsuarioModel u = usuarioExistente.get();
            u.setUsuId(dto.getUsuId());
            u.setUsuNome(dto.getUsuNome());
            u.setUsuEmail(dto.getUsuEmail());
            u.setUsuCaminhoFoto(dto.getUsuCaminhoFoto());
            u.setUsuDataCriacao(dto.getUsuDataCriacao());
            u.setUsuDataAtualizacao(dto.getUsuDataAtualizacao());
            usuarioService.salvar(u);
            return ResponseEntity.ok("Usuário atualizado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Usuário não encontrado");
        }
    }

}
