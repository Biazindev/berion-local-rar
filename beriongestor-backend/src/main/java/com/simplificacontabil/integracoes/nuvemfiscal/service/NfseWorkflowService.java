package com.simplificacontabil.integracoes.nuvemfiscal.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplificacontabil.dto.EmitirNfseRequestDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.builder.nfse.NfsePayloadBuilder;
import com.simplificacontabil.integracoes.nuvemfiscal.client.empresa.NuvemFIscalEmpresaClient;
import com.simplificacontabil.integracoes.nuvemfiscal.client.nfse.NuvemFiscalNfseClient;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse.EmitirNfseDpsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NfseWorkflowService {

    private final NuvemFiscalNfseClient client;
    private final NuvemFIscalEmpresaClient empresaClient;
    private final NfsePayloadBuilder builder;
    private final ObjectMapper mapper = new ObjectMapper();

    public void emitirEConsultar(Long empresaId, EmitirNfseRequestDTO emitirNfseRequestDTO) throws Exception {
        // 1) Monta e emite o DPS
        EmitirNfseDpsRequest request = builder.montarPayload(empresaId, emitirNfseRequestDTO);
        String postResponse = client.emitirNfseDps(request);

        // 2) Extrai o "id" do JSON de resposta
        JsonNode root = mapper.readTree(postResponse);
        String idDoDps = root.get("id").asText();
        System.out.println("DPS enviado, id = " + idDoDps);

        // 3) Aguarda um pouco e consulta o status
        //    (você pode colocar retry ou loop com Thread.sleep)
        Thread.sleep(5000);

        String getResponse = client.consultarNfse(idDoDps);
        System.out.println("Resposta da consulta →\n" + getResponse);

        // 4) Por fim, parseia o JSON de retorno se quiser extrair detalhes
        JsonNode retorno = mapper.readTree(getResponse);
        String status = retorno.get("status").asText();
        if ("processado".equals(status)) {
            JsonNode dps  = retorno.get("DPS");
            String numero     = dps.get("numero").asText();
            String chave      = dps.get("codigo_verificacao").asText();
            String xmlNfse    = dps.get("xml").asText();
            System.out.printf("NFSe %s gerada! nº %s, chave %s%n", numero, numero, chave);
            // salve ou use o xmlNfse...
        } else if ("erro".equals(status)) {
            JsonNode msgs = retorno.get("mensagens");
            System.err.println("Erro no processamento: " + msgs.toString());
        } else {
            System.out.println("Ainda processando...");
        }
    }
    public String consultarConfiguracao(String cpfCnpj) {
        return empresaClient.consultarConfigNfse(cpfCnpj);
    }
    public String atualizarTokenPrefeitura(
            String cpfCnpj,
            String novoToken,
            int lote,
            String serie,
            int numero,
            String ambiente
    ) {
        return empresaClient.atualizarConfigNfse(cpfCnpj, novoToken, lote, serie, numero, ambiente);
    }
    public String atualizarRegTribPrefeitura(
            String cpfCnpj,
            int opSimpNac,
            int regApTribSN,
            int regEspTrib,
            int lote,
            String serie,
            int numero,
            String ambiente
    ) {
        return empresaClient.atualizarRegimeTributarioNfse(
                cpfCnpj,
                opSimpNac,
                regApTribSN,
                regEspTrib,
                lote,
                serie,
                numero,
                ambiente
        );
    }


}
