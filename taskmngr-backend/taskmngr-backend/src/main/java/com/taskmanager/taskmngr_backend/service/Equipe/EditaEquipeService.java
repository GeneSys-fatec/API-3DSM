package com.taskmanager.taskmngr_backend.service.Equipe;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskmanager.taskmngr_backend.model.dto.EquipeDTO;
import com.taskmanager.taskmngr_backend.model.entidade.EquipeModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.EquipeRepository;
import com.taskmanager.taskmngr_backend.service.Notificacao.CriaNotificacaoService;

@Service
public class EditaEquipeService {
    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private BuscaEquipeService buscaEquipeService;
    @Autowired
    private ValidaEquipeService validacaoEquipeService;
    @Autowired
    private CriaNotificacaoService criaNotificacaoService;

    public EquipeModel editar(String id, EquipeDTO dto, UsuarioModel usuarioLogado) {
        EquipeModel equipe = buscaEquipeService.getEquipeById(id);

        if (dto.getEquNome() != null && !dto.getEquNome().equals(equipe.getEquNome())) {
            validacaoEquipeService.verificarSeUsuarioPodeAlterarNome(equipe, usuarioLogado);
            validacaoEquipeService.validarNomeUnico(dto.getEquNome(), id);
            equipe.setEquNome(dto.getEquNome());
        }

        if (dto.getEquDescricao() != null && !dto.getEquDescricao().equals(equipe.getEquDescricao())) {
            validacaoEquipeService.verificarSeUsuarioPodeAlterarDescricao(equipe, usuarioLogado);
            equipe.setEquDescricao(dto.getEquDescricao());
        }

        if (dto.getMembrosEmails() != null) {
            validacaoEquipeService.verificarSeUsuarioPodeAlterarMembros(equipe, usuarioLogado);
            List<UsuarioModel> novosMembros = validacaoEquipeService.buscarEValidarMembrosPorEmails(dto.getMembrosEmails());

            Set<String> antigosIds = equipe.getUsuarios().stream()
                    .map(UsuarioModel::getUsuId)
                    .collect(Collectors.toSet());
            Set<String> novosIds = novosMembros.stream()
                    .map(UsuarioModel::getUsuId)
                    .collect(Collectors.toSet());

            List<UsuarioModel> adicionados = novosMembros.stream()
                    .filter(u -> !antigosIds.contains(u.getUsuId()))
                    .collect(Collectors.toList());

            List<UsuarioModel> removidos = equipe.getUsuarios().stream()
                    .filter(u -> !novosIds.contains(u.getUsuId()))
                    .collect(Collectors.toList());

            for (UsuarioModel membro : adicionados) {
                criaNotificacaoService.criarNotificacaoAdicaoEquipe(
                    usuarioLogado.getUsuId(),
                    membro.getUsuId(),
                    equipe.getEquNome(),
                    usuarioLogado.getUsuNome()
                );
            }

            for (UsuarioModel membro : removidos) {
                criaNotificacaoService.criarNotificacaoRemocaoEquipe(
                    usuarioLogado.getUsuId(),
                    membro.getUsuId(),
                    equipe.getEquNome(),
                    usuarioLogado.getUsuNome()
                );
            }

            equipe.setUsuarios(novosMembros);
        }

        return equipeRepository.save(equipe);
    }
}
