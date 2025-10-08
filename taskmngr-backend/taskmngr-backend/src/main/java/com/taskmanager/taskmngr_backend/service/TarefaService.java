package com.taskmanager.taskmngr_backend.service;

import com.taskmanager.taskmngr_backend.model.entidade.AnexoTarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
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

    // 1 MiB
    private static final long MB = 1024L * 1024L;
    // mínimo de 2 MiB para arquivos não‑imagem
    private static final long FILE_SIZE = 2L;
    private static final long MAX_FILE_SIZE = FILE_SIZE * MB;

    public List<TarefaModel> listarTodas() {
        return tarefaRepository.findAll();
    }

    public List<TarefaModel> listarPorProjetos(List<ProjetoModel> projetos) {
        if (projetos == null || projetos.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> projetoIds = projetos.stream()
                .map(ProjetoModel::getProjId)
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

        boolean isImage = tipo.startsWith("image/");
        // Verificação de tamanho mínimo para arquivos não-imagem (>= 2MB)
        if (!isImage && arquivo.getSize() > MAX_FILE_SIZE) {
            throw new AnexoTamanhoExcedente(
                "Tamanho de arquivo insuficiente",
                "Arquivos devem ter pelo menos " + FILE_SIZE + " MB."
            );
        }

        File pasta = new File(UPLOAD_DIR);
        if (!pasta.exists()) pasta.mkdirs();

        String uuid = UUID.randomUUID().toString();
        String caminho = UPLOAD_DIR + uuid + "_" + arquivo.getOriginalFilename();
        File destino = new File(caminho);

        // Imagens serão tratadas depois; por ora apenas salva o arquivo
        arquivo.transferTo(destino);

        Optional<TarefaModel> tarefaOpt = tarefaRepository.findById(tarefaId);
        if (tarefaOpt.isPresent()) {
            TarefaModel tarefa = tarefaOpt.get();

            AnexoTarefaModel anexo = new AnexoTarefaModel();
            anexo.setArquivoNome(arquivo.getOriginalFilename());
            anexo.setArquivoTipo(tipo);
            anexo.setArquivoTamanho(arquivo.getSize());
            anexo.setArquivoCaminho(caminho);

            if (tarefa.getTarAnexos() == null) {
                tarefa.setTarAnexos(new java.util.ArrayList<>());
            }
            tarefa.getTarAnexos().add(anexo);
            tarefaRepository.save(tarefa);

            return anexo;
        } else {
            throw new IOException("Tarefa não encontrada");
        }
    }
}