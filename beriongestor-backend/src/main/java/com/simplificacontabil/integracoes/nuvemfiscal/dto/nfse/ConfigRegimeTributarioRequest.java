package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigRegimeTributarioRequest {

    private ConfigRegimeTributarioDTO regTrib;
    private ConfigRpsDTO rps;
    private String ambiente;
}
