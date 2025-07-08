package com.simplificacontabil.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.EnderecoNfeDTO;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmitenteDTO {

    @JsonProperty("CNPJ")
    private String cnpj;

    @JsonProperty("CPF")
    private String cpf; // caso use certificado de PF

    @JsonProperty("xNome")
    private String razaoSocial;

    @JsonProperty("xFant")
    private String nomeFantasia;

    @JsonProperty("IE")
    private String inscricaoEstadual;

    @JsonProperty("enderEmit")
    private EnderecoNfeDTO endereco;

    @JsonProperty("regimeTributario")
    private String regimeTributario; // Se for customizado, pode remover ou adequar

    @JsonProperty("IM")
    private String inscricaoMunicipal;

    @JsonProperty("CNAE")
    private String CNAE;

    @JsonProperty("CRT")
    private Integer crt;

    @JsonProperty("FONE")
    private String fone;
}
