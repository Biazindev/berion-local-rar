package com.simplificacontabil.controller;

import com.simplificacontabil.model.Produto;
import com.simplificacontabil.model.Receita;
import com.simplificacontabil.repository.ProdutoRepository;
import com.simplificacontabil.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/receitas")
public class ReceitaController {

    private final ReceitaRepository receitaRepo;
    private final ProdutoRepository produtoRepo;

    @Autowired
    public ReceitaController(ReceitaRepository receitaRepo,
                             ProdutoRepository produtoRepo) {
        this.receitaRepo = receitaRepo;
        this.produtoRepo = produtoRepo;
    }

    // listar todas as receitas
    @GetMapping
    public List<Receita> listarTodas() {
        return receitaRepo.findAll();
    }

    // buscar receita por ID
    @GetMapping("/{id}")
    public ResponseEntity<Receita> buscarPorId(@PathVariable Long id) {
        return receitaRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // criar nova receita
    @PostMapping
    public ResponseEntity<Receita> criar(@RequestBody Receita receita) {
        // garante que o produto final existe
        Produto pf = produtoRepo.findById(receita.getProdutoFinal().getId())
                .orElseThrow(() -> new IllegalArgumentException("Produto final não encontrado"));
        receita.setProdutoFinal(pf);

        // para cada ItemReceita, vincula de volta a receita
        receita.getItens().forEach(item -> {
            item.setReceita(receita);
            // garante que o insumo (produto) exista
            Produto insumo = produtoRepo.findById(item.getInsumo().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Insumo não encontrado: " + item.getInsumo().getId()));
            item.setInsumo(insumo);
        });

        Receita salva = receitaRepo.save(receita);
        return ResponseEntity.ok(salva);
    }
}
