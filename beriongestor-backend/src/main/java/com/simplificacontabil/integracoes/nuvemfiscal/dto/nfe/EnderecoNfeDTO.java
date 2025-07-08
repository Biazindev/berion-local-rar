package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnderecoNfeDTO {

    @JsonProperty("xLgr")
    private String xLgr; // Logradouro

    @JsonProperty("nro")
    private String nro; // Número

    @JsonProperty("xCpl")
    private String xCpl; // Complemento (opcional)

    @JsonProperty("xBairro")
    private String xBairro; // Bairro

    @JsonProperty("cMun")
    private String cMun; // Código do município (IBGE)

    @JsonProperty("xMun")
    private String xMun; // Nome do município

    @JsonProperty("UF")
    private String UF; // Unidade Federativa

    @JsonProperty("CEP")
    private String CEP; // Código de Endereçamento Postal

    @JsonProperty("cPais")
    private String cPais; // Código do país (1058 = Brasil)

    @JsonProperty("xPais")
    private String xPais; // Nome do país

    @JsonProperty("fone")
    private String fone; // Telefone (somente números)
}
