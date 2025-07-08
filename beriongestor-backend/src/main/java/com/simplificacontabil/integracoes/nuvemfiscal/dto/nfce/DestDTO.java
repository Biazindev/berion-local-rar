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
public class DestDTO {
    @JsonProperty("CPF")
    private String cpf;
    @JsonProperty("CNPJ")
    private String cnpj;
    private String xNome;
    private EnderDestDTO enderDest;
    @JsonProperty("indIEDest")
    private String indIEDest;
    @JsonProperty("IE")
    private String ie;
    private String email;
}