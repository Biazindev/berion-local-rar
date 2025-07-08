package com.simplificacontabil.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProducaoResponse {
    private int quantidadeProduzida;
    private int saldoAntesFinal;
    private int saldoDepoisFinal;
}
