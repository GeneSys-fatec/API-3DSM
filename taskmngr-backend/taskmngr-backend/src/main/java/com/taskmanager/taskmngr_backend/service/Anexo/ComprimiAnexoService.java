package com.taskmanager.taskmngr_backend.service.Anexo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ComprimiAnexoService {

    // 2 MiB (tamanho MÁXIMO para arquivos depois de processados)
    private static final long MB = 1024L * 1024L;
    private static final long MAX_FILE_SIZE = 2L * MB;

    // Compressão avançada: recomprime imagens, redimensiona, remove metadata
    // simples
    public File comprimirPdfAvancado(MultipartFile arquivo, float qualidadeJpeg, int maxImageWidth)
            throws IOException {
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
                if (res == null)
                    continue;

                for (COSName name : res.getXObjectNames()) {
                    PDXObject xobj = res.getXObject(name);
                    if (xobj instanceof PDImageXObject) {
                        PDImageXObject img = (PDImageXObject) xobj;
                        if (img.getBitsPerComponent() == 1)
                            continue; // evita máscaras 1-bit

                        BufferedImage bi = img.getImage();
                        if (bi == null)
                            continue;

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
            }
            doc.save(tmp);
        }
        return tmp;
    }

    // Compactação adaptativa de imagens (JPG/PNG): redimensiona e ajusta qualidade
    public File comprimirImagemAdaptativa(MultipartFile arquivo, String contentType, int maxWidth,
            float initialJpegQuality) throws IOException {
        BufferedImage original = ImageIO.read(arquivo.getInputStream());
        if (original == null)
            throw new IOException("Imagem inválida.");

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
            if (tmp.length() <= MAX_FILE_SIZE)
                return tmp;

            int newW = (int) Math.round(atual.getWidth() * escala);
            if (newW < minWidth)
                break;
            int newH = (int) Math.round(newW * aspect);
            atual = redimensionar(atual, newW, newH, contentType);
        }
        return tmp;
    }

    public BufferedImage redimensionar(BufferedImage src, int newW, int newH, String contentType) {
        int type = (contentType.contains("png") ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        BufferedImage scaled = new BufferedImage(newW, newH, type);
        java.awt.Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
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
