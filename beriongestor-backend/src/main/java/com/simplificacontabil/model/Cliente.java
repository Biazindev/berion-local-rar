package com.simplificacontabil.model;

import com.simplificacontabil.enums.TipoPessoa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa")
    private TipoPessoa tipoPessoa;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    @JoinColumn(name = "pessoa_fisica_id")
    private PessoaFisica pessoaFisica;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
    @JoinColumn(name = "pessoa_juridica_id")
    private PessoaJuridica pessoaJuridica;

    @OneToMany(mappedBy = "cliente",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Lancamento> lancamentos;
    @OneToMany(
            mappedBy = "cliente",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Pedido> pedidos;

    private LocalDateTime dataCadastro;

}

