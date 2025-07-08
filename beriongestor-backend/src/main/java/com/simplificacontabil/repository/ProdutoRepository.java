package com.simplificacontabil.repository;

import com.simplificacontabil.model.Estoque;
import com.simplificacontabil.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByQuantidadeLessThanEqual(int quantidade);
        boolean existsByNomeAndPrecoUnitario(String nome, BigDecimal precoUnitario);
        Optional<Produto> findByNome(String nome);
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    @Query("SELECT p FROM Produto p WHERE p.ean = :ean")
    Optional<Produto> findByEan(@Param("ean") String ean);
    Optional<Produto> findByNomeIgnoreCaseAndNcm(String nome, String ncm);

}
