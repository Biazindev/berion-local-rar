package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

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
public class RefNFDTO {

    @JsonProperty("cUF")
    private Integer cUF;

    @JsonProperty("AAMM")
    private String AAMM;

    @JsonProperty("CNPJ")
    private String CNPJ;

    @JsonProperty("mod")
    private String mod;

    @JsonProperty("serie")
    private Integer serie;

    @JsonProperty("nNF")
    private Integer nNF;
}

