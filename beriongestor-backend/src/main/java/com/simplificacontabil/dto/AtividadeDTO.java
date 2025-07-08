package com.simplificacontabil.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simplificacontabil.model.PessoaJuridica;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtividadeDTO {

    @JsonProperty("codigo")
    private String codigo;

    @JsonProperty("descricao")
    private String descricao;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pessoa_juridica_cnpj", nullable = false)
    private PessoaJuridica pessoaJuridica;
}
