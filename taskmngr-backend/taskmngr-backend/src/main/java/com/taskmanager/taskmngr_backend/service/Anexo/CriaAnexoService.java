package com.taskmanager.taskmngr_backend.service.Anexo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.tarefas.AnexoTamanhoExcedente;
import com.taskmanager.taskmngr_backend.model.entidade.AnexoTarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
import com.taskmanager.taskmngr_backend.repository.TarefaRepository;

@Service
public class CriaAnexoService {
    
    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private ComprimiAnexoService comprimiAnexoService;

    private final String UPLOAD_DIR = new File("").getAbsolutePath() + "/taskmngr-backend/uploads/";

    // 2 MiB (tamanho MÁXIMO para arquivos depois de processados)
    private static final long MB = 1024L * 1024L;
    private static final long MAX_FILE_SIZE = 2L * MB;
    private static final float PDF_IMAGE_QUALITY = 0.6f;
    private static final int MAX_IMAGE_WIDTH = 1600;
    
    public AnexoTarefaModel adicionarAnexo(String tarefaId, MultipartFile arquivo) throws IOException {
        if (arquivo.isEmpty()) {
            throw new IOException("Arquivo vazio!");
        }

        String tipo = arquivo.getContentType();
        if (tipo == null)
            throw new IOException("Tipo do arquivo não reconhecido");

        // Apenas: PDF, DOCX, XLSX e imagens JPG/PNG
        boolean permitido = tipo.matches(
                "application/pdf"
                        + "|application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                        + "|application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        + "|image/(jpeg|jpg|png)");
        if (!permitido)
            throw new IOException("Tipo de arquivo não permitido");

        boolean isImage = tipo.startsWith("image/");
        boolean isPdf = "application/pdf".equals(tipo);

        File pasta = new File(UPLOAD_DIR);
        if (!pasta.exists())
            pasta.mkdirs();

        String nomeOriginal = StringUtils.cleanPath(arquivo.getOriginalFilename());
        String extensao = StringUtils.getFilenameExtension(nomeOriginal);
        String nomeArquivoFinal = UUID.randomUUID().toString() + "." + extensao;
        String caminho = UPLOAD_DIR + nomeArquivoFinal;
        File destino = new File(caminho);

        long tamanhoFinal;
        if (isPdf) {
            long originalSize = arquivo.getSize();
            File arquivoBaseParaSalvar;
            if (originalSize > MAX_FILE_SIZE) {
                File comprimido = comprimiAnexoService.comprimirPdfAvancado(arquivo, PDF_IMAGE_QUALITY, MAX_IMAGE_WIDTH);
                long compressedSize = comprimido.length();
                if (compressedSize > MAX_FILE_SIZE) {
                    try {
                        comprimido.delete();
                    } catch (Exception ignored) {
                    }
                    throw new AnexoTamanhoExcedente(
                            "Impossível comprimir PDF.",
                            "Tente reduzir a qualidade ou remover páginas do arquivo original.");
                }
                arquivoBaseParaSalvar = comprimido;
                tamanhoFinal = compressedSize;
            } else {
                arquivoBaseParaSalvar = File.createTempFile("pdf_ok_", ".pdf");
                arquivo.transferTo(arquivoBaseParaSalvar);
                tamanhoFinal = originalSize;
            }
            Files.copy(arquivoBaseParaSalvar.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            try {
                arquivoBaseParaSalvar.delete();
            } catch (Exception ignored) {
            }
        } else {
            // Não-PDF
            File arquivoBaseParaSalvar;
            if (isImage) {
                // Imagens: compacta automaticamente quando > 2 MiB
                if (arquivo.getSize() > MAX_FILE_SIZE) {
                    File comprimido = comprimiAnexoService.comprimirImagemAdaptativa(arquivo, tipo, MAX_IMAGE_WIDTH, 0.85f);
                    long compressedSize = comprimido.length();
                    if (compressedSize > MAX_FILE_SIZE) {
                        try {
                            comprimido.delete();
                        } catch (Exception ignored) {
                        }
                        throw new AnexoTamanhoExcedente(
                                "Imagem excede o limite após compactação.",
                                "Reduza a resolução/qualidade da imagem e tente novamente.");
                    }
                    arquivoBaseParaSalvar = comprimido;
                } else {
                    arquivoBaseParaSalvar = File.createTempFile("img_ok_", ".tmp");
                    arquivo.transferTo(arquivoBaseParaSalvar);
                }
            } else {
                // DOCX/XLSX: até 2 MiB
                if (arquivo.getSize() > MAX_FILE_SIZE) {
                    throw new AnexoTamanhoExcedente(
                            "Tamanho de arquivo excedente",
                            "Arquivos (DOCX/XLSX) devem ter no máximo " + (MAX_FILE_SIZE / MB) + " MB.");
                }
                arquivoBaseParaSalvar = File.createTempFile("file_ok_", ".tmp");
                arquivo.transferTo(arquivoBaseParaSalvar);
            }

            tamanhoFinal = arquivoBaseParaSalvar.length();
            Files.copy(arquivoBaseParaSalvar.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            try {
                arquivoBaseParaSalvar.delete();
            } catch (Exception ignored) {
            }
        }

        Optional<TarefaModel> tarefaOpt = tarefaRepository.findById(tarefaId);
        if (tarefaOpt.isEmpty()) {
            try {
                destino.delete();
            } catch (Exception ignored) {
            }
            throw new IOException("Tarefa não encontrada");
        }

        TarefaModel tarefa = tarefaOpt.get();
        AnexoTarefaModel anexo = new AnexoTarefaModel();
        anexo.setArquivoNome(nomeArquivoFinal);
        anexo.setArquivoTipo(tipo);
        anexo.setArquivoTamanho(tamanhoFinal);
        anexo.setArquivoCaminho(caminho);

        if (tarefa.getTarAnexos() == null) {
            tarefa.setTarAnexos(new java.util.ArrayList<>());
        }
        tarefa.getTarAnexos().add(anexo);
        tarefaRepository.save(tarefa);
        return anexo;
    }
}
