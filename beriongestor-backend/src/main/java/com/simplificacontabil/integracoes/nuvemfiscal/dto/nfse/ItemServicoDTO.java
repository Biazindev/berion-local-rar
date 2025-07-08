package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemServicoDTO {

    @JsonProperty("itemListaServico")
    private String itemListaServico;

    @JsonProperty("codigoCnae")
    private Integer codigoCnae;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("unidade")
    private String unidade;

    @JsonProperty("tributavel")
    private Integer tributavel;

    @JsonProperty("quantidade")
    private BigDecimal quantidade;

    @JsonProperty("valorUnitario")
    private BigDecimal valorUnitario;

    @JsonProperty("valorDesconto")
    private BigDecimal valorDesconto;

    @JsonProperty("valorLiquido")
    private BigDecimal valorLiquido;

}
