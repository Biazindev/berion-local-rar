package com.simplificacontabil.repository;

import com.simplificacontabil.model.Estoque;
import com.simplificacontabil.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByProduto(Produto produto);
    Optional<Estoque> findByProdutoId(Long produtoId);
    @Query("SELECT e FROM Estoque e WHERE e.produto.ean = :ean")
    Optional<Estoque> findByProdutoEan(@Param("ean") String ean);
    @Query("SELECT SUM(e.quantidade) FROM Estoque e")
    Integer somarQuantidadeTotal();
    Long countByQuantidadeGreaterThan(int quantidade);
    Estoque findByProdutoAndFilialId(Produto produto, Long filialId);

}
