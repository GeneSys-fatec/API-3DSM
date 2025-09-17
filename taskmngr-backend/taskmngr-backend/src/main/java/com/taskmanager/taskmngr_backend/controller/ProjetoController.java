package com.taskmanager.taskmngr_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.taskmanager.taskmngr_backend.model.ProjetoModel;
import com.taskmanager.taskmngr_backend.service.ProjetoService;
// import java.util.Optional;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus; // pra retornar erro/sucesso
// import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/projeto")
public class ProjetoController {
    @Autowired
    private ProjetoService projetoService;

    @GetMapping("/listar")
    public List<ProjetoModel> listarTodas() {
        return projetoService.listarTodas();
    }

    @PostMapping("/cadastrar")
    public String cadastrarProjeto(@RequestBody ProjetoModel projeto) {
        projetoService.salvar(projeto);
        return "Projeto cadastrado com sucesso!";
    }

    @PutMapping("/atualizar")
    public String atualizar(@RequestBody ProjetoModel projeto) {
        return projetoService.buscarPorId(projeto.getProj_id())
                .map(p -> {
                    p.setProj_nome(projeto.getProj_nome());
                    p.setProj_descricao(projeto.getProj_descricao());
                    p.setProj_dataCriacao(projeto.getProj_dataCriacao());
                    p.setProj_dataAtualizacao(projeto.getProj_dataAtualizacao());
                    // p.setUsu_Id(projeto.getUsu_Id());
                    // p.setUsuNome(projeto.getUsu_Nome());
                    projetoService.salvar(p);
                    return "Projeto atualizado com sucesso";
                })
                .orElse("Projeto não encontrado");
    }

    @DeleteMapping("/apagar")
    public String apagarProjeto(@RequestBody ProjetoModel projeto) {
        projetoService.deletar(projeto.getProj_id());
        return "Projeto apagado com sucesso";
    }

}
