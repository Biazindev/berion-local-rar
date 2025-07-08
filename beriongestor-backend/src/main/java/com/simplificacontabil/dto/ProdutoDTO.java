package com.simplificacontabil.dto;

import com.simplificacontabil.model.ImpostoProduto;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProdutoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal precoUnitario;
    private BigDecimal cfop;
    private String ncm;
    private Boolean ativo;
    private String dataVencimento;
    private String imagem;
    private int quantidade;
    private String observacao;
    private List<ImpostoProduto> impostos;
    private String ean; // Código de barras EAN-13
    private BigDecimal precoCusto;
    private String unidade;
    private Long categoriaId;
    private BigDecimal valorUnitarioComercial;
    private BigDecimal valorUnitarioDesconto;
    private BigDecimal valorUnitarioTotal;
    private BigDecimal valorUnitarioTributavel;
}