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

import com.taskmanager.taskmngr_backend.model.AdicionadorLinkProjetos;
import com.taskmanager.taskmngr_backend.model.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.dto.ProjetoDTO;
import com.taskmanager.taskmngr_backend.service.ProjetoConverterService;
import com.taskmanager.taskmngr_backend.service.ProjetoService;

@RestController
@RequestMapping("/projeto")
public class ProjetoController {
    @Autowired
    private ProjetoService projetoService;
    @Autowired
    private AdicionadorLinkProjetos adicionadorLink;
    @Autowired
    private ProjetoConverterService projetoConverterService;

    @GetMapping("/{proj_id}")
    public ResponseEntity<ProjetoDTO> buscarPorId(@PathVariable String proj_id) {
        Optional<ProjetoModel> projetoOpt = projetoService.buscarPorId(proj_id);
        if (projetoOpt.isPresent()) {
            ProjetoDTO dto = projetoConverterService.modelParaDto(projetoOpt.get());
            adicionadorLink.adicionarLink(dto);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ProjetoDTO>> listarTodas() {
        List<ProjetoModel> projetos= projetoService.listarTodas();
        List<ProjetoDTO> dtos = projetos.stream().map(projetoConverterService::modelParaDto).toList();
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarProjeto(@RequestBody ProjetoDTO dto) {
        ProjetoModel projeto = projetoConverterService.dtoParaModel(dto);
        projetoService.salvar(projeto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Projeto cadastrado com sucesso!");
    }

    @PutMapping("/atualizar/{proj_id}")
    public ResponseEntity<String> atualizar(@PathVariable String proj_id, @RequestBody ProjetoDTO dto) {
        Optional<ProjetoModel> projetoExistente = projetoService.buscarPorId(proj_id);
        if (projetoExistente.isPresent()) {
            ProjetoModel p= projetoExistente.get();
                p.setProj_nome(dto.getProj_nome());
                p.setProj_descricao(dto.getProj_descricao());
                p.setProj_dataCriacao(dto.getProj_dataCriacao());
                p.setProj_dataAtualizacao(dto.getProj_dataAtualizacao());
                projetoService.salvar(p);
                return ResponseEntity.ok("Projeto atualizado com sucesso");
        } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }
    }

    @DeleteMapping("/apagar/{proj_id}")
    public ResponseEntity<String> apagarProjeto(@PathVariable String proj_id) {
        Optional<ProjetoModel> projetoExistente = projetoService.buscarPorId(proj_id);
        if (projetoExistente.isPresent()){
            projetoService.deletar(proj_id);
            return ResponseEntity.ok("Projeto apagado com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }
    }

}