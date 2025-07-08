package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class NfceDTO {
    private String id;
    private String ambiente;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    private String status;
    private String referencia;
    @JsonProperty("data_emissao")
    private OffsetDateTime dataEmissao;
    private Integer modelo;
    private Integer serie;
    @JsonProperty("tipo_emissao")
    private Integer tipoEmissao;
    @JsonProperty("valor_total")
    private BigDecimal valorTotal;
    private String chave;
    private AutorizacaoDTO autorizacao;
}
