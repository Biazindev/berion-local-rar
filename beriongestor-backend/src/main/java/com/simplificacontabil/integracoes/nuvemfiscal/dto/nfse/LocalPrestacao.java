package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocalPrestacao {

    @JsonProperty("cLocPrestacao")
    private String cLocPrestacao;

    @JsonProperty("cPaisPrestacao")
    private String cPaisPrestacao;
}
