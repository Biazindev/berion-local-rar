package com.simplificacontabil.controller;

import com.simplificacontabil.dto.EstoqueDTO;
import com.simplificacontabil.model.Produto;
import com.simplificacontabil.repository.ProdutoRepository;
import com.simplificacontabil.service.EstoqueService;
import com.simplificacontabil.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
public class EstoqueController {

    @Autowired
    private final EstoqueService estoqueService;

    @Autowired
    private final ProdutoRepository produtoRepository;

    @Autowired
    private final ProdutoService produtoService;

    @PostMapping
    public EstoqueDTO criar(@RequestBody EstoqueDTO dto) {
        return estoqueService.salvar(dto);
    }

    @GetMapping
    public List<EstoqueDTO> listar() {
        return estoqueService.listarTodos();
    }

    @GetMapping("/{id}")
    public EstoqueDTO buscar(@PathVariable Long id) {
        return estoqueService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        estoqueService.deletar(id);
    }

    @GetMapping("/estoque-baixo")
    public List<Produto> listarProdutosComEstoqueBaixo() {
        return produtoRepository.findByQuantidadeLessThanEqual(5); // ou outro valor limite
    }

    @GetMapping("/mais-vendidos")
    public List<Map<String, Object>> listarMaisVendidos() {
        return produtoService.listarMaisVendidos();
    }


    @GetMapping("/total")
    public ResponseEntity<Long> getTotalProdutosEstoque() {
        return ResponseEntity.ok(estoqueService.getTotalProdutosEmEstoque());
    }



}
