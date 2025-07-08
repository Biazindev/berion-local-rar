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
public class MovimentacaoEstoque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // ou @JoinColumn(name="produto_id")
    private Produto produto;

    private Integer quantidade;
    private TipoMovimentacao tipo;
    private LocalDateTime dataMovimentacao;
    private String descricao;
}
