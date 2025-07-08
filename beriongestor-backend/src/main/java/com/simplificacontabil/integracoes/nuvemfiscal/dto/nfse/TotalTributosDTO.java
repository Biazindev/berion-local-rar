package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TotalTributosDTO {
    @JsonProperty("vTotTrib")
    private ValoresTotaisTributoDTO vTotTrib;

    @JsonProperty("pTotTrib")
    private PercentuaisTotaisTributoDTO pTotTrib;

    @JsonProperty("indTotTrib")
    private Integer indTotTrib;

    @JsonProperty("pTotTribSN")
    private Double pTotTribSN;

}
