package com.simplificacontabil.controller;

import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce.*;
import com.simplificacontabil.integracoes.nuvemfiscal.service.NfceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nfce")
@RequiredArgsConstructor
public class NfceController {

//    private final NfceService nfceService;
//
//    @GetMapping
//    public ResponseEntity<ListarNfceResponseDTO> listar(
//            @RequestParam String cpfCnpj,
//            @RequestParam String ambiente,
//            @RequestParam(required = false) Integer top,
//            @RequestParam(required = false) Integer skip,
//            @RequestParam(required = false) Boolean inlinecount,
//            @RequestParam(required = false) String referencia,
//            @RequestParam(required = false) String chave,
//            @RequestParam(required = false) Integer serie
//    ) {
//        return ResponseEntity.ok(
//                nfceService.listar(cpfCnpj, ambiente, top, skip, inlinecount, referencia, chave, serie)
//        );
//    }
//
//    @PostMapping
//    public ResponseEntity<EmitirNfceResponseDTO> emitir(
//            @RequestBody EmitirNfceRequestDTO dto
//    ) {
//        return ResponseEntity.ok(nfceService.emitir(dto));
//    }
//
//    @PostMapping("/{id}/cancelamento")
//    public ResponseEntity<CancelarNfceResponseDTO> cancelar(
//            @PathVariable String id,
//            @RequestBody CancelarNfceRequestDTO dto
//    ) {
//        return ResponseEntity.ok(nfceService.cancelar(id, dto.getJustificativa()));
//    }
}
