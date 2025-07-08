package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import lombok.*;
import com.fasterxml.jackson.annotation.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponsavelTecnicoDTO {

    @JsonProperty("CNPJ")
    private String cnpj;

    @JsonProperty("xContato")
    private String xContato;

    @JsonProperty("email")
    private String email;

    @JsonProperty("fone")
    private String fone;

    @JsonProperty("idCSRT")
    private Integer idCSRT;

    @JsonProperty("hashCSRT")
    private String hashCSRT;

    @JsonProperty("CSRT")
    private String CSRT;
}
