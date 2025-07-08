package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; // ex: "Pão Francês"

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_final_id")
    private Produto produtoFinal; // o item que será gerado

    @JsonManagedReference
    @OneToMany(mappedBy = "receita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemReceita> itens = new ArrayList<>();
}