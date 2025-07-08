package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Builder(toBuilder = true)
@Embeddable
@Data@NoArgsConstructor@AllArgsConstructor
public class Volume {
    @Column(name="nfe_vol_qtd")
    @JsonProperty("qVol")
    private Integer qVol;
    @Column(name="nfe_vol_esp")
    @JsonProperty("esp")
    private String esp;
    @Column(name="nfe_vol_marca")
    @JsonProperty("marca")
    private String marca;
    @Column(name="nfe_vol_num")
    @JsonProperty("nVol")
    private String nVol;
    @Column(name="nfe_vol_pL")
    @JsonProperty("pesoL")
    private BigDecimal pesoL;
    @Column(name="nfe_vol_pB")
    @JsonProperty("pesoB")
    private BigDecimal pesoB;
}
