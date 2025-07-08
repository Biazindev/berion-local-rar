package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotaFiscalResponseDTO {
    private String idNota;
    private String status;
    private String mensagem;
    private String chaveAcesso;
    private Integer numeroNota;
    private Integer serie;
    private String modelo;
    private String dataAutorizacao;
    private String danfeUrl;
    private String xmlUrl;
}
