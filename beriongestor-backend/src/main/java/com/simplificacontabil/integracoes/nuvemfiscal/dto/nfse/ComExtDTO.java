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
public class ComExtDTO {
    @JsonProperty("mdPrestacao")
    private Integer mdPrestacao;

    @JsonProperty("vincPrest")
    private Integer vincPrest;

    @JsonProperty("tpMoeda")
    private String tpMoeda;

    @JsonProperty("vServMoeda")
    private Double vServMoeda;

    @JsonProperty("mecAFComexP")
    private String mecAFComexP;

    @JsonProperty("mecAFComexT")
    private String mecAFComexT;

    @JsonProperty("movTempBens")
    private Integer movTempBens;

    @JsonProperty("nDI")
    private String nDI;

    @JsonProperty("nRE")
    private String nRE;

    @JsonProperty("mdic")
    private Integer mdic;
}
