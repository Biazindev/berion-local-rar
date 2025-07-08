package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.simplificacontabil.model.Produto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImpostoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoImposto; // ICMS, IPI, PIS, COFINS etc.
    private BigDecimal aliquota;
    private String cst;
    private String origem;
    private String csosn; // ✅ Adicionado


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "produto_id")
    private Produto produto;
}
