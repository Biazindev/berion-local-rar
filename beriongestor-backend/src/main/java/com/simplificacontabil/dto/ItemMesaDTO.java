package com.simplificacontabil.dto;

import com.simplificacontabil.model.Produto;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ItemMesaDTO {
    private Long idItemproduto;
    private long produtoID;
    private String nomeProduto;
    private int quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal totalItem;
}
