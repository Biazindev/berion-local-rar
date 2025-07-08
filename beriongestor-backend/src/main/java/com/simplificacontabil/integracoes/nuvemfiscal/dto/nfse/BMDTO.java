package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BMDTO {
    private Integer tpBM;
    private String nBM;
    private Double vRedBCBM;
    private Double pRedBCBM;
}
