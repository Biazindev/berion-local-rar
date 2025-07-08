package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal precoUnitario;
    private String ncm;
    private Boolean ativo;
    private LocalDateTime dataDeVencimento;
    private int quantidade;
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ImpostoProduto> impostos;
    private String observacao;
    @Column(unique = true)
    private String ean; // Código de barras EAN-13
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    private String imagem; // e não mais byte[]
    private BigDecimal precoCusto;
    private String unidade;
    private String cfop;
    private BigDecimal valorUnitarioComercial;
    private BigDecimal valorUnitarioDesconto;
    private BigDecimal valorUnitarioTotal;
    private BigDecimal valorUnitarioTributavel;



    public ImpostoProduto getImposto(String tipo) {
        if (impostos == null || impostos.isEmpty()) {
            return null;
        }
        return impostos.stream()
                .filter(imp -> imp.getTipoImposto().equals(tipo))
                .findFirst()
                .orElse(null);
    }
    public String getCst(String tipo) {
        ImpostoProduto imp = getImposto(tipo);
        return imp != null ? imp.getCst() : null;
    }

    public String getOrigem(String tipo) {
        ImpostoProduto imp = getImposto(tipo);
        return imp != null ? imp.getOrigem() : "0"; // default nacional
    }
    public String getCsosn(String tipo) {
        return this.impostos.stream()
                .filter(imp -> imp.getTipoImposto().equalsIgnoreCase(tipo))
                .map(ImpostoProduto::getCsosn)
                .filter(csosn -> csosn != null && !csosn.isBlank())
                .findFirst()
                .orElse(null);
    }

}
