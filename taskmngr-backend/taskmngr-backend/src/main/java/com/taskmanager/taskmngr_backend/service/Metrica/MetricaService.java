package com.taskmanager.taskmngr_backend.service.Metrica;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.métricas.DashboardSemDadosException;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
import com.taskmanager.taskmngr_backend.repository.TarefaRepository;

@Service
public class MetricaService {

        @Autowired
        private TarefaRepository tarefaRepository;

        // GRAFICO 1

        public List<Map<String, Object>> calcularPrazosPorMembro(String projId) {
                List<TarefaModel> tarefas = tarefaRepository.findByProjId(projId);

                if (tarefas.isEmpty()) {
                        throw new DashboardSemDadosException("Dados Indisponíveis",
                                        "Não há tarefas cadastradas neste projeto para gerar métricas.");
                }

                // filtra apenas tarefas concluídas
                List<TarefaModel> concluidas = tarefas.stream()
                                .filter(t -> "Concluída".equalsIgnoreCase(t.getTarStatus()))
                                .toList();

                if (concluidas.isEmpty())
                        return List.of();

                Map<String, List<TarefaModel>> tarefasPorMembro = concluidas.stream()
                                .filter(t -> t.getResponsaveis() != null && !t.getResponsaveis().isEmpty())
                                .flatMap(tarefa -> tarefa.getResponsaveis().stream()
                                                .map(resp -> Map.entry(resp.getUsuNome(), tarefa)))
                                .collect(Collectors.groupingBy(
                                                Map.Entry::getKey,
                                                Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

                List<Map<String, Object>> resultado = new ArrayList<>();

                for (Map.Entry<String, List<TarefaModel>> entry : tarefasPorMembro.entrySet()) {
                        String usuario = entry.getKey();
                        List<TarefaModel> tarefasUsuario = entry.getValue();

                        long dentroPrazo = tarefasUsuario.stream()
                                        .filter(t -> Boolean.TRUE.equals(t.getConcluidaNoPrazo()))
                                        .count();

                        long foraPrazo = tarefasUsuario.stream()
                                        .filter(t -> Boolean.FALSE.equals(t.getConcluidaNoPrazo()))
                                        .count();

                        Map<String, Object> dados = new HashMap<>();
                        dados.put("usuNome", usuario);
                        dados.put("dentroPrazo", dentroPrazo);
                        dados.put("foraPrazo", foraPrazo);

                        resultado.add(dados);
                }

                return resultado;
        }

        // GRAFICO 1 
        public Map<String, Long> calcularPrazosGerais(String projId) {
                List<TarefaModel> tarefas = tarefaRepository.findByProjId(projId);
                if (tarefas.isEmpty()) {
                        throw new DashboardSemDadosException("Dados Indisponíveis",
                                        "Não há tarefas cadastradas neste projeto para gerar métricas.");
                }


                List<TarefaModel> concluidas = tarefas.stream()
                                .filter(t -> "Concluída".equalsIgnoreCase(t.getTarStatus()))
                                .toList();

                if (concluidas.isEmpty()) {
                        return Map.of("dentroPrazo", 0L, "foraPrazo", 0L);
                }

                long dentroPrazo = concluidas.stream()
                                .filter(t -> Boolean.TRUE.equals(t.getConcluidaNoPrazo()))
                                .count();

                long foraPrazo = concluidas.stream()
                                .filter(t -> Boolean.FALSE.equals(t.getConcluidaNoPrazo()))
                                .count();

                return Map.of(
                                "dentroPrazo", dentroPrazo,
                                "foraPrazo", foraPrazo);
        }

        // GRAFICO 2
        public List<Map<String, Object>> contarTarefasConcluidasPorMembro(String projId) {
                List<TarefaModel> tarefas = tarefaRepository.findByProjId(projId);
                if (tarefas.isEmpty()) {
                        throw new DashboardSemDadosException("Dados Indisponíveis",
                                        "Não há tarefas cadastradas neste projeto para gerar métricas.");
                }


                List<TarefaModel> concluidas = tarefas.stream()
                                .filter(t -> "Concluída".equalsIgnoreCase(t.getTarStatus()))
                                .toList();

                Map<String, Map<String, Object>> agrupadas = new HashMap<>();

                for (TarefaModel tarefa : concluidas) {
                        if (tarefa.getResponsaveis() == null || tarefa.getResponsaveis().isEmpty())
                                continue;

                        for (var resp : tarefa.getResponsaveis()) {
                                String nome = resp.getUsuNome();
                                if (nome == null || nome.isBlank())
                                        continue;

                                agrupadas.putIfAbsent(nome, new HashMap<>());
                                Map<String, Object> dados = agrupadas.get(nome);

                                Long count = (Long) dados.getOrDefault("tarefasConcluidas", 0L);
                                dados.put("tarefasConcluidas", count + 1);

                                @SuppressWarnings("unchecked")
                                List<Map<String, Object>> datasConclusao = (List<Map<String, Object>>) dados
                                                .getOrDefault("datasConclusao",
                                                                new ArrayList<Map<String, Object>>());

                                if (tarefa.getTarDataConclusao() != null) {
                                        Map<String, Object> dataInfo = new HashMap<>();
                                        dataInfo.put("data", tarefa.getTarDataConclusao());
                                        dataInfo.put("noPrazo", Boolean.TRUE.equals(tarefa.getConcluidaNoPrazo()));
                                        dataInfo.put("id", tarefa.getTarId());
                                        datasConclusao.add(dataInfo);
                                }
                                dados.put("datasConclusao", datasConclusao);
                                dados.put("usuNome", nome);
                                dados.put("tarId", tarefa.getTarId());
                        }
                }
                return new ArrayList<>(agrupadas.values());
        }

        // GRAFICO 3
        public Map<String, Object> calcularProdutividadePorMembro(String projId) {
                List<TarefaModel> tarefas = tarefaRepository.findByProjId(projId);
                if (tarefas.isEmpty()) {
                        throw new DashboardSemDadosException("Dados Indisponíveis",
                                        "Não há tarefas cadastradas neste projeto para gerar métricas.");
                }


                Map<YearMonth, Map<String, List<TarefaModel>>> dadosAgrupados = new TreeMap<>();

                for (TarefaModel tarefa : tarefas) {
                        if (!"Concluída".equalsIgnoreCase(tarefa.getTarStatus())
                                        || tarefa.getTarDataConclusao() == null
                                        || tarefa.getResponsaveis() == null
                                        || tarefa.getResponsaveis().isEmpty()) {
                                continue;
                        }

                        try {
                                LocalDate dataConclusao = LocalDate.parse(tarefa.getTarDataConclusao());


                                YearMonth mes = YearMonth.from(dataConclusao);

                                for (var resp : tarefa.getResponsaveis()) {
                                        String nome = resp.getUsuNome();
                                        if (nome == null || nome.isBlank())
                                                continue;

                                        dadosAgrupados.putIfAbsent(mes, new HashMap<>());
                                        dadosAgrupados.get(mes).putIfAbsent(nome, new ArrayList<>());
                                        dadosAgrupados.get(mes).get(nome).add(tarefa);
                                }

                        } catch (DateTimeParseException e) {

                        }
                }


                List<Map.Entry<YearMonth, Map<String, List<TarefaModel>>>> allEntries = new ArrayList<>(
                                dadosAgrupados.entrySet());


                int startIndex = Math.max(0, allEntries.size() - 6);


                List<Map.Entry<YearMonth, Map<String, List<TarefaModel>>>> lastSixEntries = allEntries
                                .subList(startIndex, allEntries.size());


                Set<String> usersInLastSix = new HashSet<>();
                for (Map.Entry<YearMonth, Map<String, List<TarefaModel>>> entry : lastSixEntries) {
                        usersInLastSix.addAll(entry.getValue().keySet());
                }


                List<Map<String, Object>> resultadoFormatado = new ArrayList<>();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yy");

                for (Map.Entry<YearMonth, Map<String, List<TarefaModel>>> entry : lastSixEntries) {
                        YearMonth mes = entry.getKey();
                        Map<String, List<TarefaModel>> dadosDoMes = entry.getValue();

                        Map<String, Object> linhaMes = new HashMap<>();
                        linhaMes.put("mes", mes.format(formatter));

                        for (String usuario : usersInLastSix) {
                                List<TarefaModel> tarefasUsuarioMes = dadosDoMes.getOrDefault(usuario, List.of());

                                long total = tarefasUsuarioMes.size();
                                long noPrazo = tarefasUsuarioMes.stream()
                                                .filter(t -> Boolean.TRUE.equals(t.getConcluidaNoPrazo()))
                                                .count();

                                double produtividade = (total > 0) ? (noPrazo * 100.0) / total : 0.0;
                                linhaMes.put(usuario, Math.round(produtividade * 10.0) / 10.0);
                        }
                        resultadoFormatado.add(linhaMes);
                }

                Map<String, Object> resultadoFinal = new HashMap<>();
                resultadoFinal.put("usuarios", usersInLastSix); 
                resultadoFinal.put("dadosMensais", resultadoFormatado);

                return resultadoFinal;
        }
}
