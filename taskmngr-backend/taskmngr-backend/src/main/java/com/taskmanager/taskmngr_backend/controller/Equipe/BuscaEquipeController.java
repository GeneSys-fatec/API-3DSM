package com.taskmanager.taskmngr_backend.controller.Equipe;

import java.util.List;
import java.util.stream.Collectors;

import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.AdicionadorLinkEquipe;
import com.taskmanager.taskmngr_backend.model.converter.EquipeConverter;
import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.service.Equipe.BuscaEquipeService;


@RestController
@RequestMapping("/equipe")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class BuscaEquipeController {

    @Autowired
    private EquipeConverter equipeConverterService;

    @Autowired
    private AdicionadorLinkEquipe adicionadorLink;

    @Autowired
    private BuscaEquipeService buscaEquipeService;

    @GetMapping("/listar")
    public ResponseEntity<List<EquipeDTO>> listarEquipes() {
        List<EquipeModel> equipes = buscaEquipeService.getAllEquipes();
        List<EquipeDTO> dtos = equipes.stream()
                .map(equipeConverterService::modelParaDto)
                .collect(Collectors.toList());
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{equId}")
    public ResponseEntity<EquipeDTO> getEquipeById(@PathVariable String equId) {
        EquipeModel equipe = buscaEquipeService.getEquipeById(equId);
        EquipeDTO dto = equipeConverterService.modelParaDto(equipe);
        adicionadorLink.adicionarLink(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/minhas-equipes")
    public ResponseEntity<List<EquipeDTO>> listarMinhasEquipes(@AuthenticationPrincipal UsuarioModel usuarioLogado) {
        List<EquipeModel> equipes = buscaEquipeService.getEquipesPorUsuario(usuarioLogado);

        List<EquipeDTO> dtos = equipes.stream()
                .map(equipeConverterService::modelParaDto)
                .collect(Collectors.toList());

        adicionadorLink.adicionarLink(dtos);

        return ResponseEntity.ok(dtos);
    }

    //rota de listar as equipes de um usuario especifico
    //a rota só vai ser usada para adjuar no desenvolvimento
    @GetMapping("/{usuId}/equipes")
    public ResponseEntity<List<EquipeDTO>> listarEquipesDoUsuario(@PathVariable String usuId) {
        List<EquipeModel> equipes = buscaEquipeService.getEquipesPorIdUsuario(usuId);

        List<EquipeDTO> dtos = equipes.stream()
                .map(equipeConverterService::modelParaDto)
                .collect(Collectors.toList());

        adicionadorLink.adicionarLink(dtos);

        return ResponseEntity.ok(dtos);
    }
}
