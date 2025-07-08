package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemNFeDTO {
    private String descricao;
    private String codigoProduto;
    private String ncm;
    private String cfop;
    private String unidadeComercial;
    private BigDecimal quantidadeComercial;
    private BigDecimal valorUnitarioComercial;
    private BigDecimal valorBruto;
}
