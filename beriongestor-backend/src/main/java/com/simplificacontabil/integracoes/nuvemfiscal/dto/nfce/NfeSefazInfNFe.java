package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.DetDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.IdeDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.TotalDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NfeSefazInfNFe {
    /**
     * Identificador da tag e versão do layout
     */
    @JsonProperty("@Id")
    private String Id;
    @JsonProperty("@versao")
    private String versao;

    /**
     * Seção de identificação da NF-e/NFC-e
     */
    private IdeDTO ide;

    /**
     * Dados do emitente
     */
    private EmitDTO emit;

    /**
     * Dados do destinatário (para NFC-e pode ser omitido ou conter CPF)
     */
    private DestDTO dest;

    /**
     * Lista de itens de produto/serviço
     */
    private List<DetDTO> det;

    /**
     * Total da NF-e/NFC-e (valores, tributos agregados)
     */
    private TotalDTO total;

    /**
     * Informações de transporte (geralmente nulas em NFC-e)
     */
    private TranspDTO transp;

    /**
     * Cobrança (faturas e duplicatas) — opcional para NFC-e
     */
    private CobrDTO cobr;

    /**
     * Informações adicionais
     */
    @JsonProperty("infAdic")
    private InfAdicDTO infAdic;

    /**
     * Detalhamento de pagamento (NFC-e precisa dessa seção)
     */
    private PagDTO pag;
}