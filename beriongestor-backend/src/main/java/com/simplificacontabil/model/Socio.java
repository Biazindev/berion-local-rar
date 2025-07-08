package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Socio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    @JsonProperty("qual")  // Mapear o campo "qual" para "qualificacao"
    private String qualificacao;
    // Relacionamento com a PessoaJuridica
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "pessoa_juridica_id")
    private PessoaJuridica pessoaJuridica;
}
