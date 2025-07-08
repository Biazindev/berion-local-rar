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
public class InfDpsDTO {
    @JsonProperty("tpAmb")
    private Integer tpAmb;

    @JsonProperty("dhEmi")
    private String dhEmi;

    @JsonProperty("verAplic")
    private String verAplic;

    @JsonProperty("dCompet")
    private String dCompet;

    @JsonProperty("subst")
    private SubstituicaoDTO subst;

    @JsonProperty("prest")
    private PrestadorDTO prest;

    @JsonProperty("toma")
    private TomadorDTO toma;

    @JsonProperty("interm")
    private IntermediarioDTO interm;

    @JsonProperty("serv")
    private ServicoDTO serv;

    @JsonProperty("valores")
    private ValoresDTO valores;
}

