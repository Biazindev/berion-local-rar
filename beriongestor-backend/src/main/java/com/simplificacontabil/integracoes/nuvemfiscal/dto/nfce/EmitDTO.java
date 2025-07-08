package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmitDTO {
    @JsonProperty("CNPJ")
    private String cnpj;
    private String xNome;
    private String xFant;
    private EnderEmitDTO enderEmit;
    @JsonProperty("IE")
    private String ie;
    @JsonProperty("CRT")
    private Integer crt;
}