package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagDTO {
    private List<DetPagDTO> detPag;
    private BigDecimal vTroco;
}

