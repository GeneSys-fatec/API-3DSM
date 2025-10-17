package com.taskmanager.taskmngr_backend.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taskmanager.taskmngr_backend.exceptions.personalizados.tarefas.AnexoTamanhoExcedente;
import com.taskmanager.taskmngr_backend.model.entidade.AnexoTarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.ProjetoModel;
import com.taskmanager.taskmngr_backend.model.entidade.TarefaModel;
import com.taskmanager.taskmngr_backend.model.entidade.UsuarioModel;
import com.taskmanager.taskmngr_backend.repository.TarefaRepository;

@Service
public class TarefaService {

    @Autowired
    private NotificacaoService notificacaoService;

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

    public TarefaModel salvarSemNotificacao(TarefaModel tarefa) {
    return tarefaRepository.save(tarefa);
    }

    public TarefaModel salvar(TarefaModel tarefa, UsuarioModel usuarioLogado) {
    TarefaModel tarefaSalva = tarefaRepository.save(tarefa);

    notificacaoService.criarNotificacaoAtribuicao(
        usuarioLogado.getUsuId(),
        tarefaSalva.getUsuId(),
        usuarioLogado.getUsuNome(),
        tarefaSalva.getTarId(),
        tarefaSalva.getTarTitulo()
    );
        return tarefaSalva;
    }

    // 1 dia antes do prazo
    @Scheduled(cron = "0 0 8 * * ?") // todo dia às 08:00
    public void notificarTarefasProximoVencimento() {
        LocalDate hoje = LocalDate.now();
        LocalDate prazoProximo = hoje.plusDays(1); // 1 dia antes
        List<TarefaModel> tarefas = tarefaRepository.findByTarPrazo(prazoProximo);
        
        for (TarefaModel tarefa : tarefas) {
            notificacaoService.criarNotificacaoPrazo(
                tarefa.getTarId(),
                tarefa.getTarTitulo(),
                tarefa.getUsuId()
            );
        }
    }

    @Scheduled(cron = "0 0 8 * * ?") // roda todo dia às 08:00
    public void notificarTarefasVencidas() {
        LocalDate hoje = LocalDate.now();

        // busca tarefas com prazo anterior a hoje e que ainda não estão concluídas
        List<TarefaModel> vencidas = tarefaRepository.findByTarPrazoBeforeAndTarStatusNot(hoje, "Concluída");

        for (TarefaModel tarefa : vencidas) {
            notificacaoService.criarNotificacaoPrazoExpirado(
                tarefa.getTarId(),
                tarefa.getTarTitulo(),
             tarefa.getUsuId()
        );
        }
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

        // Apenas: PDF, DOCX, XLSX e imagens JPG/PNG
        boolean permitido = tipo.matches(
            "application/pdf"
            + "|application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            + "|application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            + "|image/(jpeg|jpg|png)"
        );
        if (!permitido) throw new IOException("Tipo de arquivo não permitido");

        boolean isImage = tipo.startsWith("image/");
        boolean isPdf = "application/pdf".equals(tipo);

        File pasta = new File(UPLOAD_DIR);
        if (!pasta.exists()) pasta.mkdirs();

        String uuid = UUID.randomUUID().toString();
        String caminho = UPLOAD_DIR + uuid + "_" + arquivo.getOriginalFilename();
        File destino = new File(caminho);

        long tamanhoFinal;
        if (isPdf) {
            long originalSize = arquivo.getSize();
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
            // Não-PDF
            File arquivoBaseParaSalvar;
            if (isImage) {
                // Imagens: compacta automaticamente quando > 2 MiB
                if (arquivo.getSize() > MAX_FILE_SIZE) {
                    File comprimido = comprimirImagemAdaptativa(arquivo, tipo, MAX_IMAGE_WIDTH, 0.85f);
                    long compressedSize = comprimido.length();
                    if (compressedSize > MAX_FILE_SIZE) {
                        try { comprimido.delete(); } catch (Exception ignored) {}
                        throw new AnexoTamanhoExcedente(
                            "Imagem excede o limite após compactação.",
                            "Reduza a resolução/qualidade da imagem e tente novamente."
                        );
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
                        "Arquivos (DOCX/XLSX) devem ter no máximo " + (MAX_FILE_SIZE / MB) + " MB."
                    );
                }
                arquivoBaseParaSalvar = File.createTempFile("file_ok_", ".tmp");
                arquivo.transferTo(arquivoBaseParaSalvar);
            }

            tamanhoFinal = arquivoBaseParaSalvar.length();
            Files.copy(arquivoBaseParaSalvar.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            try { arquivoBaseParaSalvar.delete(); } catch (Exception ignored) {}
        }

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

    // Compactação adaptativa de imagens (JPG/PNG): redimensiona e ajusta qualidade
    private File comprimirImagemAdaptativa(MultipartFile arquivo, String contentType, int maxWidth, float initialJpegQuality) throws IOException {
        BufferedImage original = ImageIO.read(arquivo.getInputStream());
        if (original == null) throw new IOException("Imagem inválida.");

        int w = original.getWidth();
        int h = original.getHeight();
        double aspect = h / (double) w;

        if (w > maxWidth) {
            w = maxWidth;
            h = (int) Math.round(w * aspect);
        }

        BufferedImage atual = redimensionar(original, w, h, contentType);
        File tmp = File.createTempFile("imgcmp_", contentType.contains("png") ? ".png" : ".jpg");

        // múltiplas tentativas reduzindo qualidade (para JPEG) e resolução
        float[] qualidades = new float[] { initialJpegQuality, 0.75f, 0.65f, 0.55f, 0.45f, 0.35f, 0.25f };
        double escala = 0.9;
        int minWidth = 600;

        for (int pass = 0; pass < 12; pass++) {
            if (contentType.contains("jpeg") || contentType.contains("jpg")) {
                float q = qualidades[Math.min(pass, qualidades.length - 1)];
                escreverJpeg(atual, tmp, q);
            } else {
                // PNG: regrava e reduz resolução progressivamente
                escreverPng(atual, tmp);
            }
            if (tmp.length() <= MAX_FILE_SIZE) return tmp;

            int newW = (int) Math.round(atual.getWidth() * escala);
            if (newW < minWidth) break;
            int newH = (int) Math.round(newW * aspect);
            atual = redimensionar(atual, newW, newH, contentType);
        }
        return tmp;
    }

    private BufferedImage redimensionar(BufferedImage src, int newW, int newH, String contentType) {
        int type = (contentType.contains("png") ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        BufferedImage scaled = new BufferedImage(newW, newH, type);
        java.awt.Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(src, 0, 0, newW, newH, null);
        g2.dispose();
        return scaled;
    }

    private void escreverJpeg(BufferedImage img, File destino, float quality) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(destino)) {
            writer.setOutput(ios);
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(Math.max(0.1f, Math.min(quality, 1.0f)));
            writer.write(null, new javax.imageio.IIOImage(img, null, null), param);
        } finally {
            writer.dispose();
        }
    }

    private void escreverPng(BufferedImage img, File destino) throws IOException {
        ImageIO.write(img, "png", destino);
    }
}