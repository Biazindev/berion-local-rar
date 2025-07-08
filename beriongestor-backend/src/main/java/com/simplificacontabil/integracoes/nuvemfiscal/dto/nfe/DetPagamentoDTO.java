package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import lombok.*;
import com.fasterxml.jackson.annotation.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetPagamentoDTO {

    @JsonProperty("indPag")
    private Integer indPag;

    @JsonProperty("tPag")
    private String tPag;

    @JsonProperty("xPag")
    private String xPag;

    @JsonProperty("vPag")
    private BigDecimal vPag;

    @JsonProperty("dPag")
    private String dPag;

    @JsonProperty("CNPJPag")
    private String CNPJPag;

    @JsonProperty("UFPag")
    private String UFPag;
}
