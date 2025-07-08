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
public class PercentuaisTotaisTributoDTO {
    @JsonProperty("pTotTribFed")
    private Double pTotTribFed;

    @JsonProperty("pTotTribEst")
    private Double pTotTribEst;

    @JsonProperty("pTotTribMun")
    private Double pTotTribMun;
}

