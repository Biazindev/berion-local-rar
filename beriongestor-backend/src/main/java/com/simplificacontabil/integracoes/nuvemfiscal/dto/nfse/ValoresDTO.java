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
public class ValoresDTO {
    @JsonProperty("vServPrest")
    private ValorServPrestDTO vServPrest;
    @JsonProperty("vDescCondIncond")
    private ValorDescontosDTO vDescCondIncond;
    @JsonProperty("vDedRed")
    private ValorDeducoesDTO vDedRed;
    @JsonProperty("trib")
    private TributosDTO trib;
}
