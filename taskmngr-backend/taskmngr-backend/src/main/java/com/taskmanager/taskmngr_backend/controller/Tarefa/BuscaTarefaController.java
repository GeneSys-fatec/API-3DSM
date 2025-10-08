package com.taskmanager.taskmngr_backend.controller.Tarefa;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.AdicionadorLinkTarefa;
import com.taskmanager.taskmngr_backend.model.converter.TarefaConverter;
import com.taskmanager.taskmngr_backend.model.dto.TarefaDTO;
import com.taskmanager.taskmngr_backend.model.entidade.AnexoTarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.ProjetoService;
import com.taskmanager.taskmngr_backend.service.TarefaService;

@RestController
@RequestMapping("/tarefa")
@CrossOrigin(origins = "http://localhost:5173")
public class BuscaTarefaController {
    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private AdicionadorLinkTarefa adicionadorLink;

    @Autowired
    private TarefaConverter tarefaConverterService;

    @GetMapping("/listar-por-usuario")
    public ResponseEntity<List<TarefaDTO>> listarTarefasDoUsuario(@AuthenticationPrincipal UsuarioModel usuario) {
        List<ProjetoModel> projetosDoUsuario = projetoService.listarPorUsuario(usuario);

        List<TarefaModel> tarefas = tarefaService.listarPorProjetos(projetosDoUsuario);

        List<TarefaDTO> dtos = tarefas.stream()
                .map(tarefaConverterService::modelParaDto)
                .toList();
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{tarId}")
    public ResponseEntity<TarefaDTO> buscarPorId(@PathVariable String tarId) {
        Optional<TarefaModel> tarefaOpt = tarefaService.buscarPorId(tarId);
        if (tarefaOpt.isPresent()) {
            TarefaDTO dto = tarefaConverterService.modelParaDto(tarefaOpt.get());
            adicionadorLink.adicionarLink(dto);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/por-projeto/{projId}")
    public ResponseEntity<List<TarefaDTO>> listarTarefasPorProjeto(@PathVariable String projId) {
        List<TarefaModel> tarefas = tarefaService.listarPorProjetoUnico(projId);
        List<TarefaDTO> dtos = tarefas.stream()
                .map(tarefaConverterService::modelParaDto)
                .toList();
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<TarefaDTO>> listarTarefa() {
        List<TarefaModel> tarefas = tarefaService.listarTodas();
        List<TarefaDTO> dtos = tarefas.stream()
                .map(tarefaConverterService::modelParaDto)
                .toList();
        adicionadorLink.adicionarLink(dtos);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{tarId}/anexos/{nomeArquivo}")
    public ResponseEntity<?> baixarAnexo(@PathVariable String tarId, @PathVariable String nomeArquivo) {
        Optional<TarefaModel> tarefaOpt = tarefaService.buscarPorId(tarId);
        if (tarefaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }

        TarefaModel tarefa = tarefaOpt.get();
        AnexoTarefaModel anexo = tarefa.getTarAnexos().stream()
                .filter(a -> a.getArquivoNome().equals(nomeArquivo))
                .findFirst()
                .orElse(null);

        if (anexo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Anexo não encontrado");
        }

        File arquivo = new File(anexo.getArquivoCaminho());
        if (!arquivo.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Arquivo não existe mais no servidor");
        }

        try {
            Path path = arquivo.toPath().normalize();
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + anexo.getArquivoNome() + "\"")
                    .header("Content-Type", anexo.getArquivoTipo())
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao carregar o arquivo");
        }
    }

    @GetMapping("/{tarId}/anexos")
    public ResponseEntity<?> listarAnexos(@PathVariable String tarId) {
        Optional<TarefaModel> tarefaOpt = tarefaService.buscarPorId(tarId);
        if (tarefaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }
        return ResponseEntity.ok(tarefaOpt.get().getTarAnexos());
    }
}
