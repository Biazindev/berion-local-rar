package com.simplificacontabil.model;

import com.simplificacontabil.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoMovimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipoMovimentacao;

    private String descricao;
    private String entidade;
    private Long entidadeId;
    private LocalDateTime dataHora;
    private String usuarioResponsavel;
}
