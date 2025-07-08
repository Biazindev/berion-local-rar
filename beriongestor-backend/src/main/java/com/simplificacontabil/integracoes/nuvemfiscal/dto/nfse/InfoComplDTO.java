package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoComplDTO {

    @JsonProperty("idDocTec")
    private String idDocTec;

    @JsonProperty("docRef")
    private String docRef;

    @JsonProperty("xInfComp")
    private String xInfComp;
}
