package com.simplificacontabil.repository;

import com.simplificacontabil.model.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {

    @Query("SELECT iv.produto.nome AS nome, SUM(iv.quantidade) AS totalVendida " +
            "FROM ItemVenda iv GROUP BY iv.produto.nome ORDER BY totalVendida DESC")
    List<Map<String, Object>> findMaisVendidos();

}
