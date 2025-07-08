package com.simplificacontabil.dto;

import com.simplificacontabil.model.Filial;
import com.simplificacontabil.model.Produto;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstoqueDTO {
    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private Integer quantidade;
    private LocalDateTime dataAtualizacao;
    private Filial filial;
    private Produto produto;

}
