package com.simplificacontabil.controller;

import com.simplificacontabil.dto.PessoaJuridicaDTO;
import com.simplificacontabil.model.PessoaJuridica;
import com.simplificacontabil.service.ReceitaWSService;
import com.simplificacontabil.service.PessoaJuridicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReceitaWSController {

    @Autowired
    private ReceitaWSService receitaWsService;

    @Autowired
    private PessoaJuridicaService pessoaJuridicaService;

    // Endpoint para buscar dados do CNPJ e salvar no banco
    @GetMapping("/buscarCnpj/{cnpj}")
    public ResponseEntity<PessoaJuridicaDTO> buscarCnpj(@PathVariable String cnpj) {
        PessoaJuridicaDTO dto = receitaWsService.buscarDadosCnpjDTO(cnpj);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(dto);
    }

}
