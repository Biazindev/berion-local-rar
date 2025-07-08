package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmitirNfceRequestDTO {
    @JsonProperty("infNFe")
    private NfeSefazInfNFe infNFe;
    @JsonProperty("infNFeSupl")
    private NfeSefazInfNFeSupl infNFeSupl;
    private String ambiente;
    private String referencia;
}
