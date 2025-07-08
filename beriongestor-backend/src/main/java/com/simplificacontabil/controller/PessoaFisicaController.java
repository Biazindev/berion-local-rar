package com.simplificacontabil.controller;

import com.simplificacontabil.model.PessoaFisica;
import com.simplificacontabil.service.PessoaFisicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pessoas-fisicas")
public class PessoaFisicaController {

    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    @PostMapping
    public PessoaFisica salvar(@RequestBody PessoaFisica pessoaFisica) {
        return pessoaFisicaService.salvar(pessoaFisica);
    }

    @GetMapping("/{cpf}")
    public PessoaFisica buscarPorCpf(@PathVariable String cpf) {
        return pessoaFisicaService.buscarPorCpf(cpf);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        pessoaFisicaService.excluir(id);
    }
}
