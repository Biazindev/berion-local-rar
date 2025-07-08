package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransportaDTO {
    @JsonProperty("CNPJ")
    private String cnpj;
    @JsonProperty("CPF")
    private String cpf;
    @JsonProperty("xNome")
    private String xNome;
    @JsonProperty("xEnder")
    private String xEnder;
    @JsonProperty("xMun")
    private String xMun;
    @JsonProperty("UF")
    private String UF;
    @JsonProperty("IE")
    private String IE;
}
