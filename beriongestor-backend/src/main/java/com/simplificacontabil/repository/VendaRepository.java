package com.simplificacontabil.repository;

import com.simplificacontabil.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository  extends JpaRepository<Venda, Long> {

    List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Venda> findByDataVendaBetweenAndStatus(LocalDateTime inicio, LocalDateTime fim, String status);
    List<Venda> findByDataVendaAfter(LocalDateTime inicio);
    List<Venda> findByDataVendaBefore(LocalDateTime fim);
    List<Venda> findByStatus(String status);
    long countByStatusIgnoreCase(String status);
    @Query("SELECT COALESCE(SUM(v.totalVenda), 0) FROM Venda v WHERE v.dataVenda BETWEEN :inicio AND :fim")
    BigDecimal sumByDataBetween(LocalDateTime inicio, LocalDateTime fim);
    @Query("SELECT COUNT(v) FROM Venda v WHERE v.dataVenda BETWEEN :inicio AND :fim")
    Long countByDataBetween(LocalDateTime inicio, LocalDateTime fim);
    @Query("""
    SELECT v FROM Venda v
    JOIN FETCH v.pagamento p
    WHERE v.dataVenda BETWEEN :inicio AND :fim
      AND p.formaPagamento IS NOT NULL
""")
    List<Venda> findComPagamentoByDataVendaBetween(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("""
    SELECT v FROM Venda v
    WHERE
        LOWER(v.cliente.pessoaFisica.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
        OR LOWER(v.cliente.pessoaJuridica.nomeFantasia) LIKE LOWER(CONCAT('%', :busca, '%'))
        OR LOWER(v.cliente.pessoaJuridica.razaoSocial) LIKE LOWER(CONCAT('%', :busca, '%'))
        OR LOWER(v.documentoCliente) LIKE LOWER(CONCAT('%', :busca, '%'))
""")
    List<Venda> buscarPorClienteOuDocumento(@Param("busca") String busca);


}
