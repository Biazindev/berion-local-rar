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
public class EnderecoExterior {

    /** Código do país (tsCodigoPaisBacen) */
    @JsonProperty("cPais")
    private String cPais;

    /** Código de endereçamento postal (CEP) */
    @JsonProperty("cEndPost")
    private String cEndPost;

    /** Nome da cidade */
    @JsonProperty("xCidade")
    private String xCidade;

    /** Sigla da unidade federativa / estado */
    @JsonProperty("xEstProvReg")
    private String xEstProvReg;
}