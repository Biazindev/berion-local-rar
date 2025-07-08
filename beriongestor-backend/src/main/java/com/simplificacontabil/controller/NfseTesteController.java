package com.simplificacontabil.controller;

import com.simplificacontabil.dto.EmitirNfseRequestDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.builder.nfse.NfsePayloadBuilder;
import com.simplificacontabil.integracoes.nuvemfiscal.client.nfse.NuvemFiscalNfseClient;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse.ConfigRegimeTributarioRequest;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse.EmitirNfseDpsRequest;
import com.simplificacontabil.integracoes.nuvemfiscal.service.NfseWorkflowService;
import com.simplificacontabil.integracoes.nuvemfiscal.service.NuvemFiscalNfseService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/nfse")
@RequiredArgsConstructor
public class NfseTesteController {

    private final NuvemFiscalNfseService nfseService;
    private final NfsePayloadBuilder builder;
    private final NuvemFiscalNfseClient fiscalClient;
    private final NfseWorkflowService nfseWorkflowService;

    @GetMapping("/dps/{id}/pdf")
    public ResponseEntity<byte[]> baixarDanfe(@PathVariable String id) {
        byte[] pdf = fiscalClient.baixarDanfePdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=DANFSe-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }


    @PostMapping("/dps/emitir")
    public ResponseEntity<String> emitirNotaFiscal(
            @RequestParam Long empresaId,
            @RequestBody EmitirNfseRequestDTO dto) {
        EmitirNfseDpsRequest request = builder.montarPayload(empresaId, dto);
        String resultado = builder.emitirNfseCompletaDps(request);
        return ResponseEntity.ok(resultado);
    }



    /**
     * Consulta o status de processamento de um DPS já emitido.
     * @param id o identificador retornado pelo POST /dps
     */
    @GetMapping("/dps/{id}")
    public ResponseEntity<String> consultarDps(@PathVariable String id) {
        log.info("Iniciando consulta de DPS com cnpj: {}", id);
        try {
            String consultaJson = fiscalClient.consultarNfse(id);
            if (consultaJson == null || consultaJson.isEmpty()) {
                log.warn("Consulta de DPS {} retornou resultado vazio", id);
                return ResponseEntity.noContent().build();
            }
            log.info("Consulta de DPS {} realizada com sucesso", id);
            log.debug("Resultado da consulta: {}", consultaJson);
            return ResponseEntity.ok(consultaJson);
        } catch (Exception e) {
            log.error("Erro ao consultar DPS {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().body("Erro ao consultar DPS: " + e.getMessage());
        }
    }
    // … seus endpoints de emitirDps e consultarDps …

    /**
     * Consulta a configuração de NFS-e (regime tributário, RPS etc.) para a empresa.
     */
    @GetMapping("/config/{cpfCnpj}")
    public ResponseEntity<String> consultarConfig(@PathVariable("cpfCnpj") String cpfCnpj) {
        String json = nfseWorkflowService.consultarConfiguracao(cpfCnpj);
        log.info("Configuração NFS-e para {}: {}", cpfCnpj, json);
        return ResponseEntity.ok(json);
    }


    /**
     * Atualiza o token da prefeitura na configuração de NFS-e,
     * informando também rps (lote/serie/numero) e ambiente.
     */
    @PutMapping("/config/{cpfCnpj}")
    public ResponseEntity<String> atualizarToken(
            @PathVariable String cpfCnpj,
            @RequestBody ConfigUpdateRequest body) {

        String resposta = nfseWorkflowService.atualizarTokenPrefeitura(
                cpfCnpj,
                body.getToken(),
                body.getLote(),
                body.getSerie(),
                body.getNumero(),
                body.getAmbiente()
        );
        log.info("Configuração NFS-e atualizada para {} → {}", cpfCnpj, resposta);
        return ResponseEntity.ok(resposta);
    }
    @Data
    static class ConfigUpdateRequest {
        // campos de RPS obrigatórios
        private int    lote;
        private String serie;
        private int    numero;

        // o novo token da prefeitura
        private String token;

        // ambiente: "homologacao" ou "producao"
        private String ambiente;
    }

    @PutMapping("/config/regime/{cpfCnpj}")
    public ResponseEntity<String> atualizarRegTrib(
            @PathVariable String cpfCnpj,
            @RequestBody ConfigRegimeTributarioRequest body
    ) {
        String resposta = nfseWorkflowService.atualizarRegTribPrefeitura(
                cpfCnpj,
                body.getRegTrib().getOpSimpNac(),
                body.getRegTrib().getRegApTribSN(),
                body.getRegTrib().getRegEspTrib(),
                body.getRps().getLote(),
                body.getRps().getSerie(),
                body.getRps().getNumero(),
                body.getAmbiente()
        );
        return ResponseEntity.ok(resposta);
    }
}