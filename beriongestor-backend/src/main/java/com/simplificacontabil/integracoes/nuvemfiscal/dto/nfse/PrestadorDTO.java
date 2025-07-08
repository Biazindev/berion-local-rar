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
public class PrestadorDTO {

    @JsonProperty("CNPJ")
    private String CNPJ;

    @JsonProperty("CPF")
    private String CPF;

    @JsonProperty("IM")
    private String IM;

    @JsonProperty("regTrib")
    private RegimeTributario regTrib;
}
