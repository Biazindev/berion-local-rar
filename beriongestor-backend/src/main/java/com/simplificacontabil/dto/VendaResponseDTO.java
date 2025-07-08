package com.simplificacontabil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendaResponseDTO {
    private Long vendaId;
    private String mensagem;
}
