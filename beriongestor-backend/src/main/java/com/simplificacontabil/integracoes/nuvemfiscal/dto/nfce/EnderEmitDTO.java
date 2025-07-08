package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderEmitDTO {
    @JsonProperty("xLgr")
    private String xLgr;
    @JsonProperty("nro")
    private String nro;
    @JsonProperty("xBairro")
    private String xBairro;
    @JsonProperty("cMun")
    private String cMun;
    @JsonProperty("xMun")
    private String xMun;
    @JsonProperty("UF")
    private String UF;
    @JsonProperty("CEP")
    private String CEP;
    @JsonProperty("cPais")
    private String cPais;
    @JsonProperty("xPais")
    private String xPais;
    @JsonProperty("fone")
    private String fone;
}