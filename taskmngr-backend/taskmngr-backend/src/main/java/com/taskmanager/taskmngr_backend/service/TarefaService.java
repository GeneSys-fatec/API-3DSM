package com.taskmanager.taskmngr_backend.service;

import com.taskmanager.taskmngr_backend.model.AnexoTarefaModel;
import com.taskmanager.taskmngr_backend.model.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.TarefaModel;
import com.taskmanager.taskmngr_backend.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.UUID;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    private final String UPLOAD_DIR = new File("").getAbsolutePath() + "/taskmngr-backend/uploads/";

    public List<TarefaModel> listarTodas() {
        return tarefaRepository.findAll();
    }

    public List<TarefaModel> listarPorProjetos(List<ProjetoModel> projetos) {
        if (projetos == null || projetos.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> projetoIds = projetos.stream()
                .map(ProjetoModel::getProj_id)
                .collect(Collectors.toList());
        return tarefaRepository.findByProjIdIn(projetoIds);
    }

    public List<TarefaModel> listarPorProjetoUnico(String projId) {
        if (projId == null || projId.isEmpty()) {
            return Collections.emptyList();
        }
        return tarefaRepository.findByProjId(projId);
    }

    public Optional<TarefaModel> buscarPorId(String id) {
        return tarefaRepository.findById(id);
    }

    public TarefaModel salvar(TarefaModel tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public TarefaModel atualizar(TarefaModel tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public void deletar(String id) {
        tarefaRepository.deleteById(id);
    }

    public AnexoTarefaModel adicionarAnexo(String tarefaId, MultipartFile arquivo) throws IOException {
        if (arquivo.isEmpty()) {
            throw new IOException("Arquivo vazio!");
        }

        String tipo = arquivo.getContentType();
        if (tipo == null) throw new IOException("Tipo do arquivo não reconhecido");

        boolean permitido = tipo.matches("application/pdf|application/vnd.openxmlformats-officedocument.wordprocessingml.document|application/vnd.openxmlformats-officedocument.spreadsheetml.sheet|image/.*");
        if (!permitido) throw new IOException("Tipo de arquivo não permitido");

        File pasta = new File(UPLOAD_DIR);
        if (!pasta.exists()) pasta.mkdirs();

        String uuid = UUID.randomUUID().toString();
        String caminho = UPLOAD_DIR + uuid + "_" + arquivo.getOriginalFilename();
        File destino = new File(caminho);

        if (tipo.startsWith("image/") && arquivo.getSize() > 2 * 1024 * 1024) {
            BufferedImage image = ImageIO.read(arquivo.getInputStream());
            ImageIO.write(image, "jpg", destino); 
        } else if (tipo.equals("application/pdf") && arquivo.getSize() > 5 * 1024 * 1024) {
            arquivo.transferTo(destino); 
        } else {
            arquivo.transferTo(destino);
        }

        Optional<TarefaModel> tarefaOpt = tarefaRepository.findById(tarefaId);
        if (tarefaOpt.isPresent()) {
            TarefaModel tarefa = tarefaOpt.get();

            AnexoTarefaModel anexo = new AnexoTarefaModel();
            anexo.setArquivoNome(arquivo.getOriginalFilename());
            anexo.setArquivoTipo(tipo);
            anexo.setArquivoTamanho(arquivo.getSize());
            anexo.setArquivoCaminho(caminho);

            if (tarefa.getTar_anexos() == null) {
                tarefa.setTar_anexos(new java.util.ArrayList<>());
            }
            tarefa.getTar_anexos().add(anexo);
            tarefaRepository.save(tarefa);

            return anexo;
        } else {
            throw new IOException("Tarefa não encontrada");
        }
    }
}