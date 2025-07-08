package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NFSeMunDTO {
    private String cMunNFSeMun;
    private Integer nNFSeMun;
    private String cVerifNFSeMun;
}
