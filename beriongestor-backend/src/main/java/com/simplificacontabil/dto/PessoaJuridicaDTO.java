package com.simplificacontabil.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PessoaJuridicaDTO {
    private Long id;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String situacao;
    private String tipo;
    private String naturezaJuridica;
    private String porte;
    private String dataAbertura;
    private String ultimaAtualizacao;

    private List<AtividadeDTO> atividadesPrincipais;
    private List<AtividadeDTO> atividadesSecundarias;

    private List<SocioDTO> socios;
    private EnderecoDTO endereco;
    private SimplesNacDTO simples;

    private String telefone;
    private String inscricaoEstadual;
    private BigDecimal capitalSocial;
    private String email;
}
