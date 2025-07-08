package com.simplificacontabil.service;

import com.simplificacontabil.model.ImpostoProduto;
import com.simplificacontabil.model.ItemVenda;
import com.simplificacontabil.model.Produto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ImpostoService {

    public BigDecimal calcularImpostos(Produto produto, int quantidade) {
        BigDecimal totalImpostos = BigDecimal.ZERO;

        if (produto.getImpostos() == null) return totalImpostos;

        for (ImpostoProduto imposto : produto.getImpostos()) {
            BigDecimal base = produto.getPrecoUnitario().multiply(BigDecimal.valueOf(quantidade));
            BigDecimal valor = base
                    .multiply(imposto.getAliquota())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            totalImpostos = totalImpostos.add(valor);
        }

        return totalImpostos;
    }

    public BigDecimal calcularImpostosTotalVenda(List<ItemVenda> itens) {
        return itens.stream()
                .map(item -> calcularImpostos(item.getProduto(), item.getQuantidade()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
