package com.simplificacontabil.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.Objects;

@Service
public class CertificadoService {

    private final Path certificadoPath = Paths.get("C:/certificados");

    public String uploadCertificado(MultipartFile file) throws Exception {
        try {
            // Cria diretório, caso não exista
            Files.createDirectories(certificadoPath);
            Path filePath = certificadoPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));
            Files.write(filePath, file.getBytes());
            return "Certificado digital enviado com sucesso!";
        } catch (Exception e) {
            throw new Exception("Erro ao enviar o certificado: " + e.getMessage());
        }
    }
}
