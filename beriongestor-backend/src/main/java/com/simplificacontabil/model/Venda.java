package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.simplificacontabil.enums.FormaPagamento;
import com.simplificacontabil.enums.ModeloNota;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataVenda;
    @Column(nullable = false)
    private BigDecimal totalVenda;
    private String documentoCliente;  // CNPJ ou CPF cliente (emitente)
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // Esta anotação evita o loop infinito
    private List<ItemVenda> itens = new ArrayList<>(); // Inicialize a lista aqui
    private String status;  // Adicionando o campo status
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;  // Associando um cliente à venda
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = true) // ✅ correto
    private Empresa emitente;
    /** Chave de acesso / protocolo retornado pela SEFAZ */
    private String chaveAcessoNfe;
    private String numeroProtocoloNfe;
    @Enumerated(EnumType.STRING)
    private ModeloNota modelo;
    @Builder.Default
    @Column(nullable = false)
    private boolean vendaAnonima = false;
    @Column(nullable = false)
    private boolean emitirNotaFiscal;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Pagamento pagamento;
    private Integer modFrete; // 0, 1, 2, 9
    private LocalDateTime ultimaAtualizacao;
    private String observacao;
    private String infAdFisco;
    @Embedded
    private NfeFrete frete;



}
