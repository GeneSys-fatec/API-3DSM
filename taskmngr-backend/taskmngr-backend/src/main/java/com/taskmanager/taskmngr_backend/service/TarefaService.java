package com.taskmanager.taskmngr_backend.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.tarefas.AnexoTamanhoExcedente;
import com.taskmanager.taskmngr_backend.model.entidade.AnexoTarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
import com.taskmanager.taskmngr_backend.repository.TarefaRepository;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    private final String UPLOAD_DIR = new File("").getAbsolutePath() + "/taskmngr-backend/uploads/";

    // 2 MiB (tamanho MÁXIMO para arquivos depois de processados)
    private static final long MB = 1024L * 1024L;
    private static final long MAX_FILE_SIZE = 2L * MB;
    private static final float PDF_IMAGE_QUALITY = 0.6f; 
    private static final int MAX_IMAGE_WIDTH = 1600;

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
        boolean permitido = tipo.matches(
            "application/pdf|application/vnd.openxmlformats-officedocument.wordprocessingml.document|application/vnd.openxmlformats-officedocument.spreadsheetml.sheet|image/.*"
        );
        if (!permitido) throw new IOException("Tipo de arquivo não permitido");

        boolean isImage = tipo.startsWith("image/");
        boolean isPdf = "application/pdf".equals(tipo);

        // cria a pasta de upload se não existir
        File pasta = new File(UPLOAD_DIR);
        if (!pasta.exists()) pasta.mkdirs();

        String uuid = UUID.randomUUID().toString();
        String caminho = UPLOAD_DIR + uuid + "_" + arquivo.getOriginalFilename();
        File destino = new File(caminho);

        //inicia a compressão dos pdfs
        long tamanhoFinal;
        if (isPdf) {
            long originalSize = arquivo.getSize();

            // Tenta comprimir somente se maior que nosso limite lógico
            File arquivoBaseParaSalvar;
            if (originalSize > MAX_FILE_SIZE) {
                File comprimido = comprimirPdfAvancado(arquivo, PDF_IMAGE_QUALITY, MAX_IMAGE_WIDTH);
                long compressedSize = comprimido.length();

                if (compressedSize > MAX_FILE_SIZE) {
                    try { comprimido.delete(); } catch (Exception ignored) {}
                    throw new AnexoTamanhoExcedente(
                        "Impossível comprimir PDF.",
                        "Tente reduzir a qualidade ou remover páginas do arquivo original."
                    );
                }
                arquivoBaseParaSalvar = comprimido;
                tamanhoFinal = compressedSize;
            } else {
                arquivoBaseParaSalvar = File.createTempFile("pdf_ok_", ".pdf");
                arquivo.transferTo(arquivoBaseParaSalvar);
                tamanhoFinal = originalSize;    
            }

            Files.copy(arquivoBaseParaSalvar.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            try { arquivoBaseParaSalvar.delete(); } catch (Exception ignored) {}

        } else {
            if (!isImage && arquivo.getSize() > MAX_FILE_SIZE) {
                throw new AnexoTamanhoExcedente(
                    "Tamanho de arquivo excedente",
                    "Arquivos não-PDF e não-imagem devem ter no máximo " + (MAX_FILE_SIZE / MB) + " MB."
                );
            }
            arquivo.transferTo(destino);
            tamanhoFinal = arquivo.getSize();
        }
        // fim da validacao de compressao

        Optional<TarefaModel> tarefaOpt = tarefaRepository.findById(tarefaId);
        if (tarefaOpt.isEmpty()) {
            try { destino.delete(); } catch (Exception ignored) {}
            throw new IOException("Tarefa não encontrada");
        }

        TarefaModel tarefa = tarefaOpt.get();
        AnexoTarefaModel anexo = new AnexoTarefaModel();
        anexo.setArquivoNome(arquivo.getOriginalFilename());
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

    // Compressão avançada: recomprime imagens, redimensiona, remove metadata simples
    private File comprimirPdfAvancado(MultipartFile arquivo, float qualidadeJpeg, int maxImageWidth) throws IOException {
        File tmp = File.createTempFile("pdfcmp_", ".pdf");
        try (PDDocument doc = PDDocument.load(arquivo.getInputStream())) {
            // Remove metadata (opcional)
            if (doc.getDocumentInformation() != null) {
                doc.getDocumentInformation().getCOSObject().clear();
            }
            if (doc.getDocumentCatalog() != null && doc.getDocumentCatalog().getMetadata() != null) {
                doc.getDocumentCatalog().setMetadata(null);
            }

            for (PDPage page : doc.getPages()) {
                PDResources res = page.getResources();
                if (res == null) continue;

                for (COSName name : res.getXObjectNames()) {
                    PDXObject xobj = res.getXObject(name);
                    if (xobj instanceof PDImageXObject) {
                        PDImageXObject img = (PDImageXObject) xobj;
                        if (img.getBitsPerComponent() == 1) continue; // evita máscaras 1-bit

                        BufferedImage bi = img.getImage();
                        if (bi == null) continue;

                        // Redimensiona se necessário
                        if (bi.getWidth() > maxImageWidth) {
                            int newWidth = maxImageWidth;
                            int newHeight = (int) ((bi.getHeight() / (double) bi.getWidth()) * newWidth);
                            BufferedImage scaled = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                            java.awt.Graphics2D g2 = scaled.createGraphics();
                            g2.drawImage(bi, 0, 0, newWidth, newHeight, null);
                            g2.dispose();
                            bi = scaled;
                        }
                        PDImageXObject jpeg = JPEGFactory.createFromImage(doc, bi, qualidadeJpeg);
                        res.put(name, jpeg);
                    }
                }
            }doc.save(tmp);
        }return tmp;
    }
}