package com.simplificacontabil.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NfeFrete {

    @Column(name="nfe_valor_frete")
    private BigDecimal valorFrete;

    @Column(name="nfe_valor_seguro")
    private BigDecimal valorSeguro;

    @Column(name="nfe_valor_desconto")
    private BigDecimal valorDesconto;

    @Column(name="nfe_outras_despesas")
    private BigDecimal outrasDespesas;

    @Embedded
    private Transporta transportadora;

    @Builder.Default
    @ElementCollection
    @CollectionTable(
            name = "venda_volumes",
            joinColumns = @JoinColumn(name = "venda_id")
    )
    private List<Volume> volumes = new ArrayList<>();
}
