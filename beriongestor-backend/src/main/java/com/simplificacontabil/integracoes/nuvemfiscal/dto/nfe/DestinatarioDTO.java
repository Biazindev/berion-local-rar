package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import lombok.*;
import com.fasterxml.jackson.annotation.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DestinatarioDTO {

    @JsonProperty("CNPJ")
    private String cnpj;

    @JsonProperty("CPF")
    private String cpf;

    private String idEstrangeiro;

    @JsonProperty("xNome")
    private String xNome;

    @JsonProperty("enderDest")
    private EnderecoNfeDTO enderDest;

    private Integer indIEDest;

    @JsonProperty("IE")
    private String ie;

    @JsonProperty("ISUF")
    private String isuf;

    @JsonProperty("IM")
    private String im;

    private String email;
}