package com.simplificacontabil.controller;

import com.simplificacontabil.model.PessoaJuridica;
import com.simplificacontabil.service.PessoaJuridicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/pessoas-juridicas")
public class PessoaJuridicaController {

    @Autowired
    private PessoaJuridicaService pessoaJuridicaService;

    // Endpoint para salvar ou atualizar uma PessoaJuridica
    @PostMapping
    public PessoaJuridica salvar(@RequestBody PessoaJuridica pessoaJuridica) {
        return pessoaJuridicaService.salvar(pessoaJuridica);
    }

    // Endpoint para buscar uma PessoaJuridica por ID
    @GetMapping("/{id}")
    public Optional<PessoaJuridica> buscarPorId(@PathVariable Long id) {
        return pessoaJuridicaService.buscarPorId(id);
    }

    // Endpoint para excluir uma PessoaJuridica por ID
    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        pessoaJuridicaService.excluir(id);
    }

    // Novo endpoint para buscar uma PessoaJuridica por CNPJ
    @GetMapping("/cnpj/{cnpj}")
    public PessoaJuridica buscarPorCnpj(@PathVariable String cnpj) {
        return pessoaJuridicaService.buscarPorCnpj(cnpj);
    }
}
