package com.taskmanager.taskmngr_backend.controller.Tarefa;

import java.io.File;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.model.entidade.AnexoTarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
import com.taskmanager.taskmngr_backend.service.Tarefa.BuscaTarefaService;
import com.taskmanager.taskmngr_backend.service.Tarefa.ExcluiTarefaService;
import com.taskmanager.taskmngr_backend.service.Tarefa.NotificacaoTarefaService;

@RestController
@RequestMapping("/tarefa")
@CrossOrigin(origins = "http://localhost:5173")
public class ExcluiTarefaController {
    @Autowired
    private BuscaTarefaService buscaTarefaService;

    @Autowired
    private NotificacaoTarefaService notificacaoTarefaService;

    @Autowired
    private ExcluiTarefaService excluiTarefaService;

    @DeleteMapping("/{tarId}/anexos/{nomeArquivo}")
    public ResponseEntity<?> removerAnexo(@PathVariable String tarId, @PathVariable String nomeArquivo) {
        Optional<TarefaModel> tarefaOpt = buscaTarefaService.buscarPorId(tarId);
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

        tarefa.getTarAnexos().remove(anexo);
        notificacaoTarefaService.salvarSemNotificacao(tarefa);

        File arquivo = new File(anexo.getArquivoCaminho());
        if (arquivo.exists()) {
            if (!arquivo.delete()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao apagar arquivo físico");
            }
        }

        return ResponseEntity.ok("Anexo removido com sucesso");
    }

    @DeleteMapping("/apagar/{tarId}")
    public ResponseEntity<String> apagarTarefa(@PathVariable String tarId) {
        Optional<TarefaModel> tarefaExistente = buscaTarefaService.buscarPorId(tarId);
        if (tarefaExistente.isPresent()) {
            excluiTarefaService.deletar(tarId);
            return ResponseEntity.ok("Tarefa apagada com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tarefa não encontrada");
        }
    }
}
