package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemReceita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="receita_id")
    @JsonIgnore               // <<< evita voltar para Receita
    private Receita receita;

    @ManyToOne(optional = false)
    @JoinColumn(name = "insumo_id")
    private Produto insumo; // ex: Farinha, Ovo, etc.

    private Integer quantidade; // quantidade de insumo por 1 unidade de produtoFinal
}
