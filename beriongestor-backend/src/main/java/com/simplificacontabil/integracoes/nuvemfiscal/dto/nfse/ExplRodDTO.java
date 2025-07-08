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
public class ExplRodDTO {
    @JsonProperty("categVeic")
    private String categVeic;

    @JsonProperty("nEixos")
    private String nEixos;

    @JsonProperty("rodagem")
    private Integer rodagem;

    @JsonProperty("sentido")
    private String sentido;

    @JsonProperty("placa")
    private String placa;

    @JsonProperty("codAcessoPed")
    private String codAcessoPed;

    @JsonProperty("codContrato")
    private String codContrato;
}