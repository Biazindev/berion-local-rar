package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfRespTecDTO {

    @JsonProperty("CNPJ")
    private String cnpj;

    @JsonProperty("xContato")
    private String xContato;

    @JsonProperty("email")
    private String email;

    @JsonProperty("fone")
    private String fone;

    // Esses dois abaixo só se você realmente for usar NFC-e com CSRT.
    // Pode remover se for apenas NF-e (modelo 55).
    @JsonProperty("idCSRT")
    private Integer idCSRT;

    @JsonProperty("hashCSRT")
    private String hashCSRT;
}
