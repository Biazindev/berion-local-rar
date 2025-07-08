package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse;

import com.simplificacontabil.dto.EmitenteDTO;
import com.simplificacontabil.dto.PagamentoDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.InfRespTecDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmitirNFeRequest {
    private EmitenteDTO emitente;
    private DestinatarioDTO destinatario;
    private List<ItemNFeDTO> itens;
    private List<PagamentoDTO> formasPagamento;
    private InfRespTecDTO infRespTec;
}
