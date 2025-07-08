package com.simplificacontabil.controller;

import com.simplificacontabil.model.ConfiguracaoEmpresa;
import com.simplificacontabil.service.ConfiguracaoEmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configuracao")
@RequiredArgsConstructor
public class ConfiguracaoEmpresaController {

    private final ConfiguracaoEmpresaService configuracaoEmpresaService;

    @GetMapping("/{documento}")
    public ResponseEntity<ConfiguracaoEmpresa> buscarPorDocumento(@PathVariable String documento) {
        return ResponseEntity.ok(configuracaoEmpresaService.buscarPorDocumento(documento));
    }

    @GetMapping("/apikey/{apiKey}")
    public ResponseEntity<ConfiguracaoEmpresa> buscarPorApiKey(@PathVariable String apiKey) {
        return ResponseEntity.ok(configuracaoEmpresaService.buscarPorApiKey(apiKey));
    }
}