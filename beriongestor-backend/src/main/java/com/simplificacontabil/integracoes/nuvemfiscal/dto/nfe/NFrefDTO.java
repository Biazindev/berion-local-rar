package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NFrefDTO {

    @JsonProperty("refNFe")
    private String refNFe;

    @JsonProperty("refNFeSig")
    private String refNFeSig;

    @JsonProperty("refNF")
    private RefNFDTO refNF;

    @JsonProperty("refNFP")
    private RefNFPDTO refNFP;

    @JsonProperty("refCTe")
    private String refCTe;

    @JsonProperty("refECF")
    private RefECFDTO refECF;
}
