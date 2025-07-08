package com.simplificacontabil.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplificacontabil.dto.EmitirNotaRequestDTO;
import com.simplificacontabil.model.Empresa;
import com.simplificacontabil.model.Venda;
import com.simplificacontabil.integracoes.nuvemfiscal.builder.nfe.NfeBuilderService;
import com.simplificacontabil.integracoes.nuvemfiscal.client.nfe.NuvemFiscalNfeClient;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.EmitirNfeRequestDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.NotaFiscalResponseDTO;
import com.simplificacontabil.repository.EmpresaRepository;
import com.simplificacontabil.repository.VendaRepository;
import com.simplificacontabil.service.VendaService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/nfe")
@RequiredArgsConstructor
public class NfeNuvemController {


    private final VendaRepository vendaRepository;
    private final NuvemFiscalNfeClient nuvemFiscalNfeClient;
    private final NfeBuilderService builder;
    private final EmpresaRepository empresaRepository;
    private final VendaService vendaService;

//    @PostMapping("/emitir")
//    public ResponseEntity<?> emitirNota(@RequestBody EmitirNotaRequestDTO requestDto) {
//        try {
//            Venda venda = vendaRepository.findById(requestDto.getVendaId())
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venda não encontrada"));
//
//            Empresa emitente = empresaRepository.findById(requestDto.getEmitenteId())
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emitente não encontrado"));
//
//            // ⚠️ Altera o emitente da venda ANTES de montar a nota
//            venda.setEmitente(emitente);
//
//            EmitirNfeRequestDTO request = builder.montarNfeCompleta(venda);
//            String resposta = nuvemFiscalNfeClient.emitirNfe(request);
//
//            NotaFiscalResponseDTO response = builder.montarResposta(resposta);
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("erro", "Falha ao emitir NF-e", "detalhes", e.getMessage()));
//        }
//    }

    @PostMapping("/emitir")
    public ResponseEntity<NotaFiscalResponseDTO> emitirNota(
            @RequestBody EmitirNotaRequestDTO dto
    ) {
        NotaFiscalResponseDTO resp = vendaService.emitirNotaFiscal(dto);
        return ResponseEntity.ok(resp);
    }


    @PostMapping("/teste")
    public ResponseEntity<NotaFiscalResponseDTO> emitirNota(@RequestBody EmitirNfeRequestDTO request) throws JsonProcessingException {
        String resposta = nuvemFiscalNfeClient.emitirNfe(request);
        NotaFiscalResponseDTO response = builder.montarResposta(resposta);
        return ResponseEntity.ok(response);
    }

    /**
     * Consulta a NF-e na Nuvem Fiscal pelo ID
     */
    @GetMapping("/consultar/{id}")
    public ResponseEntity<String> consultarNfe(@PathVariable String id) {
        String resultado = nuvemFiscalNfeClient.consultarNfe(id);
        return ResponseEntity.ok(resultado);
    }
    /**
     * Baixa o DANFE da NF-e em PDF
     */
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> baixarDanfeNfe(@PathVariable("id") String idNfe) {
        byte[] pdf = nuvemFiscalNfeClient.baixarDanfeNfe(idNfe);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("danfe-nfe.pdf").build());

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/documentos")
    public ResponseEntity<String> listarComPaginacao(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        int skip = page * size;
        String json = nuvemFiscalNfeClient.listarDocumentosDistribuicaoNfe(
                null, null, null, null, null, null,
                size, skip, false
        );
        return ResponseEntity.ok(json);
    }

}
