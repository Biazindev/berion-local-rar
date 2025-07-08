package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class VolDTO {
    @JsonProperty("qVol") private Integer quantidade;
    @JsonProperty("esp")  private String especie;
    @JsonProperty("marca") private String marca;
    @JsonProperty("nVol")  private String numeracao;
    @JsonProperty("pesoL") private BigDecimal pesoLiquido;
    @JsonProperty("pesoB") private BigDecimal pesoBruto;
}
