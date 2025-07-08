package com.simplificacontabil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemReceitaDTO {
    private Long insumoId;
    private String insumoNome;
    private Integer quantidade;
}
