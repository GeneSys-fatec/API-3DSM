package com.taskmanager.taskmngr_backend.controller;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.tarefas.InvalidTaskDataException;
import com.taskmanager.taskmngr_backend.model.AdicionadorLinkTarefa;
import com.taskmanager.taskmngr_backend.model.AnexoTarefaModel;
import com.taskmanager.taskmngr_backend.model.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.TarefaModel;
import com.taskmanager.taskmngr_backend.model.UsuarioModel;
import com.taskmanager.taskmngr_backend.model.dto.TarefaDTO;
import com.taskmanager.taskmngr_backend.service.ProjetoService;
import com.taskmanager.taskmngr_backend.service.TarefaConverterService;
import com.taskmanager.taskmngr_backend.service.TarefaService;

@RestController
@RequestMapping("/tarefa")
@CrossOrigin(origins = "http://localhost:5173")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private AdicionadorLinkTarefa adicionadorLink;

    @Autowired
    private TarefaConverterService tarefaConverterService;

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

    @GetMapping("/{tar_id}")
    public ResponseEntity<TarefaDTO> buscarPorId(@PathVariable String tar_id) {
        Optional<TarefaModel> tarefaOpt = tarefaService.buscarPorId(tar_id);
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

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarTarefa(@RequestBody TarefaDTO dto,
            @AuthenticationPrincipal UsuarioModel usuarioLogado) {

        if (dto.getTar_titulo() == null || dto.getTar_titulo().isBlank() ||
                dto.getTar_descricao() == null || dto.getTar_descricao().isBlank() ||
                dto.getTar_prazo() == null || dto.getTar_prazo().isBlank()) {

            throw new InvalidTaskDataException("Erro ao cadastrar tarefa",
                    "Título, descrição e data são obrigatórios.");
        }
        TarefaModel tarefa = tarefaConverterService.dtoParaModel(dto);
        if (usuarioLogado != null) {
            tarefa.setUsu_id(usuarioLogado.getUsu_id());
            tarefa.setUsu_nome(usuarioLogado.getUsu_nome());
        }
        TarefaModel salva = tarefaService.salvar(tarefa);
        tarefaService.salvar(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PostMapping("/{tar_id}/upload")
    public ResponseEntity<?> uploadAnexo(@PathVariable String tar_id, @RequestParam("file") MultipartFile file) {
        try {
            AnexoTarefaModel anexo = tarefaService.adicionarAnexo(tar_id, file);
            return ResponseEntity.ok(anexo);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro no upload: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno: " + e.getMessage());
        }
    }

    @GetMapping("/{tar_id}/anexos/{nomeArquivo}")
    public ResponseEntity<?> baixarAnexo(@PathVariable String tar_id, @PathVariable String nomeArquivo) {
        Optional<TarefaModel> tarefaOpt = tarefaService.buscarPorId(tar_id);
        if (tarefaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }

        TarefaModel tarefa = tarefaOpt.get();
        AnexoTarefaModel anexo = tarefa.getTar_anexos().stream()
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

    @GetMapping("/{tar_id}/anexos")
    public ResponseEntity<?> listarAnexos(@PathVariable String tar_id) {
        Optional<TarefaModel> tarefaOpt = tarefaService.buscarPorId(tar_id);
        if (tarefaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }
        return ResponseEntity.ok(tarefaOpt.get().getTar_anexos());
    }

    @DeleteMapping("/{tar_id}/anexos/{nomeArquivo}")
    public ResponseEntity<?> removerAnexo(@PathVariable String tar_id, @PathVariable String nomeArquivo) {
        Optional<TarefaModel> tarefaOpt = tarefaService.buscarPorId(tar_id);
        if (tarefaOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }

        TarefaModel tarefa = tarefaOpt.get();

        AnexoTarefaModel anexo = tarefa.getTar_anexos().stream()
                .filter(a -> a.getArquivoNome().equals(nomeArquivo))
                .findFirst()
                .orElse(null);

        if (anexo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Anexo não encontrado");
        }

        tarefa.getTar_anexos().remove(anexo);
        tarefaService.salvar(tarefa);

        File arquivo = new File(anexo.getArquivoCaminho());
        if (arquivo.exists()) {
            if (!arquivo.delete()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao apagar arquivo físico");
            }
        }

        return ResponseEntity.ok("Anexo removido com sucesso");
    }

    @PutMapping("/atualizar/{tar_id}")
    public ResponseEntity<String> atualizarTarefa(@PathVariable String tar_id, @RequestBody TarefaDTO dto,
            @AuthenticationPrincipal UsuarioModel usuarioLogado) {
        if (dto.getTar_titulo() == null || dto.getTar_titulo().isBlank() ||
                dto.getTar_descricao() == null || dto.getTar_descricao().isBlank() ||
                dto.getTar_prazo() == null || dto.getTar_prazo().isBlank()) {

            throw new InvalidTaskDataException("Erro ao cadastrar tarefa",
                    "Título, descrição e data são obrigatórios.");
        }
        Optional<TarefaModel> tarefaExistente = tarefaService.buscarPorId(tar_id);
        if (tarefaExistente.isPresent()) {
            TarefaModel t = tarefaExistente.get();
            t.setTar_titulo(dto.getTar_titulo());
            t.setTar_descricao(dto.getTar_descricao());
            t.setTar_status(dto.getTar_status());
            t.setTar_prioridade(dto.getTar_prioridade());
            t.setTar_prazo(dto.getTar_prazo());
            t.setTar_dataCriacao(dto.getTar_dataCriacao());
            t.setTar_dataAtualizacao(dto.getTar_dataAtualizacao());

            t.setUsu_id(dto.getUsu_id());
            t.setUsu_nome(dto.getUsu_nome());

            t.setProj_id(dto.getProj_id());
            t.setProj_nome(dto.getProj_nome());
            tarefaService.salvar(t);
            return ResponseEntity.ok("Tarefa atualizada com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tarefa não encontrada");
        }
    }

    @DeleteMapping("/apagar/{tar_id}")
    public ResponseEntity<String> apagarTarefa(@PathVariable String tar_id) {
        Optional<TarefaModel> tarefaExistente = tarefaService.buscarPorId(tar_id);
        if (tarefaExistente.isPresent()) {
            tarefaService.deletar(tar_id);
            return ResponseEntity.ok("Tarefa apagada com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tarefa não encontrada");
        }
    }
}