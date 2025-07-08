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
public class CodigoServico {
    @JsonProperty("cTribNac")
    private String cTribNac;

    @JsonProperty("cTribMun")
    private String cTribMun;

    @JsonProperty("CNAE")
    private String CNAE;

    // força o JSON a sair como "xDescServ"
    @JsonProperty("xDescServ")
    private String xDescServ;

    @JsonProperty("cNBS")
    private String cNBS;
}
