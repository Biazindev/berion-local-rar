package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValoresDeclaracaoServicoDTO {

    @JsonProperty("valorServicos")
    private Double valorServicos;

    @JsonProperty("valorDeducoes")
    private Double valorDeducoes;

    @JsonProperty("aliquotaPis")
    private Double aliquotaPis;

    @JsonProperty("retidoPis")
    private Integer retidoPis;

    @JsonProperty("valorPis")
    private Double valorPis;

    @JsonProperty("aliquotaCofins")
    private Double aliquotaCofins;

    @JsonProperty("retidoCofins")
    private Integer retidoCofins;

    @JsonProperty("valorCofins")
    private Double valorCofins;

    @JsonProperty("aliquotaInss")
    private Double aliquotaInss;

    @JsonProperty("retidoInss")
    private Integer retidoInss;

    @JsonProperty("valorInss")
    private Double valorInss;

    @JsonProperty("aliquotaIr")
    private Double aliquotaIr;

    @JsonProperty("retidoIr")
    private Integer retidoIr;

    @JsonProperty("valorIr")
    private Double valorIr;

    @JsonProperty("aliquotaCsll")
    private Double aliquotaCsll;

    @JsonProperty("retidoCsll")
    private Integer retidoCsll;

    @JsonProperty("valorCsll")
    private Double valorCsll;

    @JsonProperty("aliquotaCpp")
    private Double aliquotaCpp;

    @JsonProperty("retidoCpp")
    private Integer retidoCpp;

    @JsonProperty("valorCpp")
    private Double valorCpp;

    @JsonProperty("outrasRetencoes")
    private Double outrasRetencoes;

    @JsonProperty("retidoOutrasRetencoes")
    private Integer retidoOutrasRetencoes;

    @JsonProperty("aliquotaTotTributos")
    private Double aliquotaTotTributos;

    @JsonProperty("valTotTributos")
    private Double valTotTributos;

    @JsonProperty("valorIss")
    private Double valorIss;

    @JsonProperty("aliquota")
    private Double aliquota;

    @JsonProperty("descontoIncondicionado")
    private Double descontoIncondicionado;

    @JsonProperty("descontoCondicionado")
    private Double descontoCondicionado;
}
