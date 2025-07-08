package com.simplificacontabil.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimplesNacDTO {
    private boolean optante;
    @Nullable
    private String dataExclusao;
    private String ultimaAtualizacao;
}
