package com.simplificacontabil.repository;

import com.simplificacontabil.model.Configuracao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, String> {
    Configuracao findByChave(String chave);
}
