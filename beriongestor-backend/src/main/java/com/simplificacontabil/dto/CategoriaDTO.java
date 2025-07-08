package com.simplificacontabil.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String tipo;
    private Boolean ativa;
}
