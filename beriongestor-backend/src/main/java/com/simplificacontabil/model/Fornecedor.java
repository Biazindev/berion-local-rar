package com.simplificacontabil.model;

import com.simplificacontabil.enums.TipoPessoa;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fornecedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa")
    private TipoPessoa tipoPessoa;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pessoa_fisica_id")
    private PessoaFisica pessoaFisica;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pessoa_juridica_id")
    private PessoaJuridica pessoaJuridica;

    private String telefone;
    private String email;

    public boolean isValid() {
        return (pessoaFisica != null && pessoaJuridica == null) ||
                (pessoaJuridica != null && pessoaFisica == null);
    }
}

