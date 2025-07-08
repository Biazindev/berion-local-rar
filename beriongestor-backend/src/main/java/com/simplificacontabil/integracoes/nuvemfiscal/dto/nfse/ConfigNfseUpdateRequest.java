package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigNfseUpdateRequest {

    @JsonProperty("regTrib")
    private ConfigRegTribDTO regTrib;

    @JsonProperty("rps")
    private ConfigRpsDTO rps;

    @JsonProperty("prefeitura")
    private ConfigPrefeituraDTO prefeitura;

    @JsonProperty("incentivo_fiscal")
    private boolean incentivoFiscal;

    @JsonProperty("ambiente")
    private String ambiente;
}
