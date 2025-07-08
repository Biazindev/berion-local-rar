package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Builder(toBuilder = true)
@Embeddable
@Data
@NoArgsConstructor@AllArgsConstructor
public class Transporta {
    @Column(name="nfe_transp_cnpj")
    @JsonProperty("CNPJ")
    private String CNPJ;

    @Column(name="nfe_transp_cpf")
    @JsonProperty("CPF")
    private String CPF;

    @Column(name="nfe_transp_nome")
    @JsonProperty("xNome")
    private String xNome;

    @Column(name="nfe_transp_end")
    @JsonProperty("xEnder")
    private String xEnder;

    @Column(name="nfe_transp_mun")
    @JsonProperty("xMun")
    private String xMun;

    @Column(name="nfe_transp_uf")
    @JsonProperty("UF")
    private String UF;

    @Column(name="nfe_transp_ie")
    @JsonProperty("IE")
    private String IE;

}
