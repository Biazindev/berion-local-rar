package com.simplificacontabil.controller;

import com.simplificacontabil.dto.PagamentoTefDTO;
import com.simplificacontabil.service.PaygoTefService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamento/tef")
@RequiredArgsConstructor
public class PagamentoTefController {

    private final PaygoTefService tefService;

    @PostMapping
    public ResponseEntity<String> iniciarPagamento(@RequestBody PagamentoTefDTO dto) {
        String resposta = tefService.iniciarPagamento(dto);
        return ResponseEntity.ok(resposta);
    }
}
