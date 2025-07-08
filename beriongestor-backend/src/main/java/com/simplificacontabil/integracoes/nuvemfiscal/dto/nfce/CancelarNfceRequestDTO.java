package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelarNfceRequestDTO {
    private String justificativa;
}
