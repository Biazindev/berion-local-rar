package com.simplificacontabil.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoDTO {
    private String cep;
    private String bairro;
    private String municipio;
    private String logradouro;
    private String numero;
    private String uf;
    private String complemento;
    private String codigoIbge;
}
