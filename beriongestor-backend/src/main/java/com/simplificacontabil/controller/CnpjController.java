package com.simplificacontabil.controller;

import com.simplificacontabil.dto.PessoaJuridicaDTO;
import com.simplificacontabil.service.ReceitaWSService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cnpj")
@RequiredArgsConstructor
public class CnpjController {

    private final ReceitaWSService receitaWsService;

    @GetMapping("/{cnpj}")
    public ResponseEntity<?> buscarCnpj(@PathVariable String cnpj) {
        try {
            PessoaJuridicaDTO dados = receitaWsService.buscarDadosCnpjDTO(cnpj);
            return ResponseEntity.ok(dados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao consultar CNPJ: " + e.getMessage());
        }
    }
}
