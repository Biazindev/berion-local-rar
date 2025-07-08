package com.simplificacontabil.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/certificados")
public class CertificadoController {

    @PostMapping("/upload")
    public String uploadCertificado(@RequestParam("certificado") MultipartFile file) {
        try {
            // Salvar o arquivo em um diretório do servidor
            Path path = Paths.get("C:/certificados/" + file.getOriginalFilename());
            Files.write(path, file.getBytes());
            return "Certificado digital enviado com sucesso!";
        } catch (IOException e) {
            return "Erro ao enviar o certificado: " + e.getMessage();
        }
    }
}

