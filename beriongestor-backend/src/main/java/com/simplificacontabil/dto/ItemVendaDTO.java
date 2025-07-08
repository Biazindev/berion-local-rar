package com.simplificacontabil.dto;

import com.simplificacontabil.util.MoedaUtil;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemVendaDTO {

    private Long id;
    private Long produtoId;  // Referência ao ID do produto (não o objeto completo)
    private String nomeProduto;  // Nome do produto
    private String  precoUnitario;  // Preço unitário do produto
    private Integer quantidade;  // Quantidade do produto vendido
    private String totalItem;  // Total do item (preço unitário * quantidade)


    public BigDecimal getTotalItemAsBigDecimal() {
        return MoedaUtil.converterParaBigDecimal(totalItem);
    }

    public BigDecimal getPrecoUnitarioAsBigDecimal() {
        return MoedaUtil.converterParaBigDecimal(precoUnitario);
    }
}