package com.taskmanager.taskmngr_backend.service.Tarefa;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.converter.TarefaConverter;
import com.taskmanager.taskmngr_backend.model.dto.ResponsavelTarefaDTO;
import com.taskmanager.taskmngr_backend.model.dto.TarefaDTO;
import com.taskmanager.taskmngr_backend.model.entidade.ResponsavelTarefa;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;

@Service
public class CriaTarefaService {
    @Autowired
    private TarefaConverter tarefaConverter;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Autowired
    private NotificacaoTarefaService notificacaoTarefaService;

    private static final char[] BASE32HEX = "0123456789abcdefghijklmnopqrstuv".toCharArray();
    private static final SecureRandom RNG = new SecureRandom();
    
    public TarefaModel criarTarefa(TarefaDTO dto, UsuarioModel usuarioLogado) {
        TarefaModel tarefa = tarefaConverter.dtoParaModel(dto);

        if (dto.getResponsaveis() != null && !dto.getResponsaveis().isEmpty()) {
            List<ResponsavelTarefa> responsaveisVerificados = buildResponsaveisList(dto.getResponsaveis());
            tarefa.setResponsaveis(responsaveisVerificados);
        } else {
            tarefa.setResponsaveis(new ArrayList<>());
        }

        if (tarefa.getGoogleId() == null || tarefa.getGoogleId().isBlank()) {
            tarefa.setGoogleId(generateGoogleEventId());
        }
        
        if ("Concluída".equalsIgnoreCase(tarefa.getTarStatus())) {
            String dataConclusao = LocalDate.now().toString();
            tarefa.setTarDataConclusao(dataConclusao);

            try {
                LocalDate prazo = LocalDate.parse(tarefa.getTarPrazo());
                LocalDate conclusao = LocalDate.parse(dataConclusao);

                tarefa.setConcluidaNoPrazo(!conclusao.isAfter(prazo));

            } catch (DateTimeParseException e) {
                tarefa.setConcluidaNoPrazo(false);
            }
        }
        return notificacaoTarefaService.salvarEEnviarNotificacao(tarefa, usuarioLogado);
    }

    public List<ResponsavelTarefa> buildResponsaveisList(List<ResponsavelTarefaDTO> responsaveisDto) {
        List<String> ids = responsaveisDto.stream()
                .map(ResponsavelTarefaDTO::getUsuId)
                .collect(Collectors.toList());

        List<UsuarioModel> usuariosReais = usuarioRepository.findByUsuIdIn(ids);

        return usuariosReais.stream()
                .map(usuario -> new ResponsavelTarefa(usuario.getUsuId(), usuario.getUsuNome()))
                .collect(Collectors.toList());
    }
    
    public String generateGoogleEventId() {
        String prefix = "taskmngr";
        int len = 22;
        char[] buf = new char[prefix.length() + len];

        for (int i = 0; i < prefix.length(); i++)
            buf[i] = prefix.charAt(i);
        for (int i = prefix.length(); i < buf.length; i++) {
            buf[i] = BASE32HEX[RNG.nextInt(BASE32HEX.length)];
        }
        return new String(buf);
    }
}

