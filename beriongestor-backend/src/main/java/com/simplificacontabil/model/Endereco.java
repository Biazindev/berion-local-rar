package com.simplificacontabil.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {
    private String cep;
    private String bairro;
    private String logradouro;
    private String numero;
    private String uf;
    private String complemento;
    private String codigoIbge;
    private String municipio;

}

