package com.simplificacontabil.controller;

import com.simplificacontabil.dto.PagamentoTefDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/paygo")
public class FakePaygoMiddlewareController {

    @PostMapping
    public ResponseEntity<Map<String, Object>> simularPagamento(@RequestBody PagamentoTefDTO dto) {
        Map<String, Object> response = new HashMap<>();
        response.put("transacaoId", UUID.randomUUID().toString());
        response.put("status", "APROVADO");
        response.put("valor", dto.getValor());
        response.put("formaPagamento", dto.getFormaPagamento());
        response.put("caixa", dto.getNumeroCaixa());
        return ResponseEntity.ok(response);
    }
}
