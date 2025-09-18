package com.taskmanager.taskmngr_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.AdicionadorLinkUsuario;
import com.taskmanager.taskmngr_backend.model.UsuarioModel;
import com.taskmanager.taskmngr_backend.model.dto.UsuarioDTO;
import com.taskmanager.taskmngr_backend.service.UsuarioConverterService;
import com.taskmanager.taskmngr_backend.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AdicionadorLinkUsuario adicionadorLink;
    @Autowired
    private UsuarioConverterService usuarioConverterService;

    @GetMapping("/{usu_id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable String usu_id) {
        Optional<UsuarioModel> usuarioOpt = usuarioService.buscarPorId(usu_id);
        if (usuarioOpt.isPresent()) {
            UsuarioDTO dto = usuarioConverterService.modelParaDto(usuarioOpt.get());
            adicionadorLink.adicionarLink(dto);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    } //caso precise buscar por id

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioDTO>> listarUsuario() {
        List<UsuarioModel> usuarios = usuarioService.listarTodas();
        List<UsuarioDTO> dtos = usuarios.stream()
            .map(usuarioConverterService::modelParaDto)
            .toList();
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarUsuario(@RequestBody UsuarioDTO dto) { //aqui tbm request body pede os atributos
        UsuarioModel usuario = usuarioConverterService.dtoParaModel(dto);
        usuarioService.salvar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Usuário cadastrado com sucesso!");
    }

    @PutMapping("/atualizar/{usu_id}")
    public ResponseEntity<String> atualizarUsuario(@PathVariable String usu_id, @RequestBody UsuarioDTO dto) {
        Optional<UsuarioModel> usuarioExistente = usuarioService.buscarPorId(usu_id);
        //busca no banco uma usuario pelo id que vai ser opcional (pode ou nao existir)
        if (usuarioExistente.isPresent()) {
            UsuarioModel u = usuarioExistente.get();
            u.setUsu_id(dto.getUsu_id());
            u.setUsu_nome(dto.getUsu_nome());
            u.setUsu_email(dto.getUsu_email());
            u.setUsu_caminhoFoto(dto.getUsu_caminhoFoto());
            u.setUsu_senha(dto.getUsu_senha());
            u.setUsu_dataCriacao(dto.getUsu_dataCriacao());
            u.setUsu_dataAtualizacao(dto.getUsu_dataAtualizacao());
            usuarioService.salvar(u);
            return ResponseEntity.ok("Usuário atualizado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Usuário não encontrado");
        }
    }

    @DeleteMapping("/apagar/{usu_id}")
    public ResponseEntity<String> apagarUsuario(@PathVariable String usu_id) { //enqnt o path so o id ? 
        Optional<UsuarioModel> usuarioExistente = usuarioService.buscarPorId(usu_id);
        if (usuarioExistente.isPresent()){
            usuarioService.deletar(usu_id);
            return ResponseEntity.ok("Usuário apagado com sucesso!");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Usuário não encontrado");
        }
    }   
}
