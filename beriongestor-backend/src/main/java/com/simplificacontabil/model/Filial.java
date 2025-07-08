package com.simplificacontabil.model;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Filial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cnpj;
    private String endereco;
    private String telefone;
    private String email;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    @Nullable
    private Empresa empresa; // Referência para saber de qual empresa SaaS é essa filial
}
