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
public class AtvEventoDTO {
    @JsonProperty("xNome")
    private String xNome;

    @JsonProperty("desc")
    private String desc;

    @JsonProperty("dtIni")
    private String dtIni;

    @JsonProperty("dtFim")
    private String dtFim;

    @JsonProperty("idAtvEvt")
    private String idAtvEvt;

    @JsonProperty("id")
    private String id;

    @JsonProperty("end")
    private EnderecoObraDTO end;
}
