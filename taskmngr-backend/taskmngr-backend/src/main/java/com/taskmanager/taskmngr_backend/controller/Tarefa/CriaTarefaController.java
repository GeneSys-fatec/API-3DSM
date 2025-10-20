package com.taskmanager.taskmngr_backend.controller.Tarefa;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.tarefas.AnexoTamanhoExcedente;
import com.taskmanager.taskmngr_backend.exceptions.personalizados.tarefas.InvalidTaskDataException;
import com.taskmanager.taskmngr_backend.model.converter.TarefaConverter;
import com.taskmanager.taskmngr_backend.model.dto.ColunaDTO;
import com.taskmanager.taskmngr_backend.model.dto.TarefaDTO;
import com.taskmanager.taskmngr_backend.model.entidade.AnexoTarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.service.TarefaService;

@RestController
@RequestMapping("/tarefa")
@CrossOrigin(origins = "http://localhost:5173")
public class CriaTarefaController {
    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private TarefaConverter tarefaConverterService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarTarefa(@RequestBody TarefaDTO dto,
            @AuthenticationPrincipal UsuarioModel usuarioLogado) {

        if (dto.getTarTitulo() == null || dto.getTarTitulo().isBlank() ||
            dto.getTarDescricao() == null || dto.getTarDescricao().isBlank() ||
            dto.getTarPrazo() == null || dto.getTarPrazo().isBlank()) {

            throw new InvalidTaskDataException("Erro ao cadastrar tarefa",
                    "Título, descrição e data são obrigatórios.");
        }
        TarefaModel tarefa = tarefaConverterService.dtoParaModel(dto);
        TarefaModel salva = tarefaService.criarTarefa(dto, usuarioLogado);
        TarefaDTO dtoDeResposta = tarefaConverterService.modelParaDto(salva);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoDeResposta);
    }

    @PostMapping("/{tarId}/upload")
    public ResponseEntity<?> uploadAnexo(@PathVariable String tarId, @RequestParam("file") MultipartFile file) {
        try {
            AnexoTarefaModel anexo = tarefaService.adicionarAnexo(tarId, file);
            return ResponseEntity.ok(anexo);
        } catch (AnexoTamanhoExcedente e) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("Arquivo excede 2MB após compressão: " + e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro no upload: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno: " + e.getMessage());
        }
    }
}
