package com.simplificacontabil.repository;

import com.simplificacontabil.model.ConfiguracaoEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfiguracaoEmpresaRepository extends JpaRepository<ConfiguracaoEmpresa, Long> {
    Optional<ConfiguracaoEmpresa> findByDocumento(String documento);
    Optional<ConfiguracaoEmpresa> findByApiKey(String apiKey);
}
