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
public class PiscofinsDTO {
    @JsonProperty("CST")
    private String CST;

    @JsonProperty("vBCPisCofins")
    private Double vBCPisCofins;

    @JsonProperty("pAliqPis")
    private Double pAliqPis;

    @JsonProperty("pAliqCofins")
    private Double pAliqCofins;

    @JsonProperty("vPis")
    private Double vPis;

    @JsonProperty("vCofins")
    private Double vCofins;

    @JsonProperty("tpRetPisCofins")
    private Integer tpRetPisCofins;
}
