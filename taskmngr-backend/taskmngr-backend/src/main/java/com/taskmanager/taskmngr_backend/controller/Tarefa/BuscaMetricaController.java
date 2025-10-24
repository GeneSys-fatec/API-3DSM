package com.taskmanager.taskmngr_backend.controller.Tarefa;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskmanager.taskmngr_backend.service.MetricaService;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class BuscaMetricaController {
    @Autowired
    private MetricaService MetricaService;


    @GetMapping("/prazos/{projId}")
    public ResponseEntity<?> prazosPorMembro(@PathVariable String projId) {
        List<Map<String, Object>> resultado = MetricaService.calcularPrazosPorMembro(projId);

        if (resultado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/prazos-geral/{projId}")
    public ResponseEntity<?> prazosGerais(@PathVariable String projId) {
        Map<String, Long> resultado = MetricaService.calcularPrazosGerais(projId);

        if (resultado.get("dentroPrazo") == 0 && resultado.get("foraPrazo") == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/tarefas-concluidas/{projId}")
    public ResponseEntity<List<Map<String, Object>>> TarefasConcluidas(@PathVariable String projId) {
        List<Map<String, Object>> ranking = MetricaService.contarTarefasConcluidasPorMembro(projId);
        if (ranking.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/produtividade/{projId}")
    public ResponseEntity<?> produtividadePorMembro(@PathVariable String projId) {
        Map<String, Object> resultado = MetricaService.calcularProdutividadePorMembro(projId);

        // verifica se a lista de usuários dentro do mapa está vazia
        @SuppressWarnings("unchecked")
        Set<String> usuarios = (Set<String>) resultado.get("usuarios");

        if (usuarios == null || usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(resultado);
    }
}
