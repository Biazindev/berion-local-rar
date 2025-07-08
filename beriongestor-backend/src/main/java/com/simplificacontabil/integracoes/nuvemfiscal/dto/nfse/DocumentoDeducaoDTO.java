package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.simplificacontabil.dto.FornecedorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoDeducaoDTO {
    private String chNFSe;
    private String chNFe;
    private NFSeMunDTO NFSeMun;
    private NFNFS_DTO NFNFS;
    private String nDocFisc;
    private String nDoc;
    private Integer tpDedRed;
    private String xDescOutDed;
    private String dtEmiDoc;
    private Double vDedutivelRedutivel;
    private Double vDeducaoReducao;
    private FornecedorDTO fornec;
}

