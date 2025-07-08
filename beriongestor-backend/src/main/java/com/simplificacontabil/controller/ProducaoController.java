package com.simplificacontabil.controller;
import com.simplificacontabil.dto.MovimentacaoDTO;
import com.simplificacontabil.dto.ProducaoRequest;
import com.simplificacontabil.dto.ProducaoResponse;
import com.simplificacontabil.service.ProducaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/producao")
public class ProducaoController {

    @Autowired
    private ProducaoService producaoService;

    @PostMapping
    public ResponseEntity<ProducaoResponse> produzir(
            @RequestBody ProducaoRequest req
    ) {
        ProducaoResponse resp = producaoService.produzir(
                req.getReceitaId(),
                req.getQuantidade()
        );
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/movimentacoes")
    public ResponseEntity<List<MovimentacaoDTO>> historico(
            @RequestParam Long produtoId
    ) {
        List<MovimentacaoDTO> lista = producaoService
                .listarMovimentacoes(produtoId);
        return ResponseEntity.ok(lista);
    }
}
