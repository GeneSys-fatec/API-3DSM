package com.taskmanager.taskmngr_backend.service.Equipe;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.taskmanager.taskmngr_backend.repository.UsuarioRepository;
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
    @Autowired
    private UsuarioRepository usuarioRepository;

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


            List<String> antigosIdsList = equipe.getUsuarioIds();

            List<UsuarioModel> antigosMembros = usuarioRepository.findAllById(antigosIdsList);

            Set<String> antigosIdsSet = antigosMembros.stream()
                    .map(UsuarioModel::getUsuId)
                    .collect(Collectors.toSet());
            Set<String> novosIdsSet = novosMembros.stream()
                    .map(UsuarioModel::getUsuId)
                    .collect(Collectors.toSet());

            List<UsuarioModel> adicionados = novosMembros.stream()
                    .filter(u -> !antigosIdsSet.contains(u.getUsuId()))
                    .collect(Collectors.toList());

            List<UsuarioModel> removidos = antigosMembros.stream()
                    .filter(u -> !novosIdsSet.contains(u.getUsuId()))
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

            List<String> novosMembrosIds = novosMembros.stream()
                    .map(UsuarioModel::getUsuId)
                    .collect(Collectors.toList());

            equipe.setUsuarioIds(novosMembrosIds);

        }
        return equipeRepository.save(equipe);
    }
}
