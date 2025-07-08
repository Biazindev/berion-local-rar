package com.simplificacontabil.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public  class ServicoDTO {
    private String descricao;
    private Double valor;
    private String codigoTributacaoMunicipal; // cTribMun
    private String codigoTributacaoNacional; // cTribNac
    private String cnae;
    private String nbs;
    private String informacoesComplementares;
}

