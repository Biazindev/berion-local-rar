package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String descricao;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pessoa_juridica_id")
    private PessoaJuridica pessoaJuridica;
}
