package com.simplificacontabil.controller;

import com.simplificacontabil.model.Lancamento;
import com.simplificacontabil.service.LancamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

    @Autowired
    private LancamentoService lancamentoService;

    @PostMapping
    public Lancamento salvar(@RequestBody Lancamento lancamento) {
        return lancamentoService.salvar(lancamento);
    }

    @GetMapping
    public List<Lancamento> buscarTodos() {
        return lancamentoService.buscarTodos();
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        lancamentoService.excluir(id);
    }
}
