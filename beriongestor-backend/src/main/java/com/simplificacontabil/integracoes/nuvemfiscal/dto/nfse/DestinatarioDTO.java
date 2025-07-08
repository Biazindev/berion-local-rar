package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.EnderecoNfeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DestinatarioDTO {

    @JsonProperty("CNPJ")
    private String CNPJ;

    @JsonProperty("CPF")
    private String CPF;

    @JsonProperty("idEstrangeiro")
    private String idEstrangeiro;

    @JsonProperty("xNome")
    private String xNome;

    @JsonProperty("enderDest")
    private EnderecoNfeDTO enderDest;

    @JsonProperty("indIEDest")
    private Integer indIEDest;

    @JsonProperty("IE")
    private String IE;

    @JsonProperty("ISUF")
    private String ISUF;

    @JsonProperty("IM")
    private String IM;

    @JsonProperty("email")
    private String email;
}
