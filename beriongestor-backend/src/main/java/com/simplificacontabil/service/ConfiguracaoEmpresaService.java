package com.simplificacontabil.service;

import com.simplificacontabil.model.ConfiguracaoEmpresa;
import com.simplificacontabil.repository.ConfiguracaoEmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracaoEmpresaService {

    @Autowired
    private  ConfiguracaoEmpresaRepository repository;

    public ConfiguracaoEmpresa buscarPorDocumento(String documento) {
        return repository.findByDocumento(documento)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com o documento informado"));
    }

    public ConfiguracaoEmpresa buscarPorApiKey(String apiKey) {
        return repository.findByApiKey(apiKey)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com esta API Key"));
    }
}
