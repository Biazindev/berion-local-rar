package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PessoaJuridica extends Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cnpj;

    @JsonProperty("abertura")
    private String dataAbertura;

    private String situacao;
    private String tipo;
    private String nomeFantasia;
    private String porte;
    private String razaoSocial;
    private String inscricaoEstadual;

    @JsonProperty("natureza_juridica")
    private String naturezaJuridica;

    @OneToMany(mappedBy = "pessoaJuridica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atividade> atividadesPrincipais;

    @OneToMany(mappedBy = "pessoaJuridica", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("atividades_secundarias")
    private List<Atividade> atividadesSecundarias;

    @OneToMany(mappedBy = "pessoaJuridica", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("qsa")
    private List<Socio> socios;

    private String capitalSocial;

    private String telefone;

    private SimplesNac simples;
    private String email;

    @Embedded
    private Endereco endereco;
}
