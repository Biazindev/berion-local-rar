package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValoresTotaisTributoDTO {

    @JsonProperty("vTotTribFed")
    private Double vTotTribFed;

    @JsonProperty("vTotTribEst")
    private Double vTotTribEst;

    @JsonProperty("vTotTribMun")
    private Double vTotTribMun;
}
