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
public class EnderecoObraDTO {
    @JsonProperty("CEP")
    private String CEP;

    @JsonProperty("xLgr")
    private String xLgr;

    @JsonProperty("tpLgr")
    private String tpLgr;

    @JsonProperty("nro")
    private String nro;

    @JsonProperty("xCpl")
    private String xCpl;

    @JsonProperty("xBairro")
    private String xBairro;

    @JsonProperty("endExt")
    private EnderecoExterior endExt;
}
