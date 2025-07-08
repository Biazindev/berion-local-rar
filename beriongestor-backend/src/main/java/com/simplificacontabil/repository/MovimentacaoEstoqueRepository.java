package com.simplificacontabil.repository;

import com.simplificacontabil.model.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    /**
     * Calcula o saldo atual do estoque para um dado produto:
     * soma das entradas menos soma das saídas.
     */
    @Query("""
      SELECT COALESCE(
        SUM(
          CASE WHEN m.tipo = com.simplificacontabil.enums.TipoMovimentacao.ENTRADA
               THEN m.quantidade ELSE 0 END
        ), 0
      )
      -
      COALESCE(
        SUM(
          CASE WHEN m.tipo = com.simplificacontabil.enums.TipoMovimentacao.SAIDA
               THEN m.quantidade ELSE 0 END
        ), 0
      )
      FROM MovimentacaoEstoque m
      WHERE m.produto.id = :produtoId
    """)
    int calculaSaldo(@Param("produtoId") Long produtoId);

    List<MovimentacaoEstoque> findByProdutoIdOrderByDataMovimentacaoDesc(Long produtoId);

}
