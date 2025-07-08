package com.simplificacontabil.repository;

import com.simplificacontabil.model.HistoricoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricoMovimentacaoRepository extends JpaRepository<HistoricoMovimentacao, Long> {
}
