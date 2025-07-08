package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.simplificacontabil.util.MoedaUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagamentoDTO {
    private String formaPagamento; // Ex: "01" (Dinheiro), "03" (Cartão de crédito)
    private BigDecimal valorPagamento;
}
