package com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class AutorizacaoDTO {
    @JsonProperty("digest_value")
    private String digestValue;
    private String id;
    private String ambiente;
    private String status;
    private AutorDTO autor;
    @JsonProperty("chave_acesso")
    private String chaveAcesso;
    @JsonProperty("data_evento")
    private OffsetDateTime dataEvento;
    @JsonProperty("numero_sequencial")
    private Integer numeroSequencial;
    @JsonProperty("data_recebimento")
    private OffsetDateTime dataRecebimento;
    @JsonProperty("codigo_status")
    private Integer codigoStatus;
    @JsonProperty("motivo_status")
    private String motivoStatus;
    @JsonProperty("numero_protocolo")
    private String numeroProtocolo;
    @JsonProperty("codigo_mensagem")
    private Integer codigoMensagem;
    private String mensagem;
    @JsonProperty("tipo_evento")
    private String tipoEvento;
}