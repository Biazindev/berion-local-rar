package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Transportadora {
    @JsonProperty("CNPJ") private String cnpj;
    @JsonProperty("CPF")  private String cpf;
    private String xNome;
    @JsonProperty("xEnder") private String xEnder;
    @JsonProperty("xMun")   private String xMun;
    private String UF;
    private String IE;
    @Id
    @GeneratedValue
    private Long id;

}
