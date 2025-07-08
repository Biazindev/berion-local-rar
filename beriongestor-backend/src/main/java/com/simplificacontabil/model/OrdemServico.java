package com.simplificacontabil.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.simplificacontabil.enums.StatusOrdemServico;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private String descricao;
    private LocalDate dataAbertura;
    private LocalDate dataConclusao;
    @Enumerated(EnumType.STRING)
    private StatusOrdemServico status;

    private BigDecimal valor;
}
