package com.taskmanager.taskmngr_backend.controller.Tarefa;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/anexos")
@CrossOrigin(origins = "http://localhost:5173")
public class AnexoController {

    private static final Logger logger = LoggerFactory.getLogger(AnexoController.class);

    private final String UPLOAD_DIR = new File("").getAbsolutePath() + "/taskmngr-backend/uploads/";

    @GetMapping("/{nomeArquivo}")
    public ResponseEntity<Resource> visualizarAnexo(@PathVariable String nomeArquivo) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(nomeArquivo).normalize();
            logger.info("AnexoController: Tentando acessar o caminho: {}", filePath.toString());

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                logger.warn("Arquivo não encontrado ou inacessível: {}", filePath.toString());
                return ResponseEntity.notFound().build();
            }

            String contentType = null;
            try {
                contentType = Files.probeContentType(filePath);
            } catch (IOException ex) {
            }
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            String headerValue = "inline; filename=\"" + resource.getFilename() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);

        } catch (MalformedURLException ex) {
            logger.error("Erro ao construir a URL do arquivo: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception ex) {
            logger.error("Erro interno ao servir o anexo: {}", ex.getMessage(), ex);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}