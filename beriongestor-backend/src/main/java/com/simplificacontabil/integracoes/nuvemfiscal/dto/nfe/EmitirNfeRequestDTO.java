package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmitirNfeRequestDTO {
    @JsonProperty("infNFe")
    private InfNfeDTO infNFe;

    @JsonProperty("infNFeSupl")
    private InfNfeSuplDTO infNFeSupl;

    @JsonProperty("ambiente")
    private String ambiente; // homologacao ou producao

    @JsonProperty("referencia")
    private String referencia;
}

