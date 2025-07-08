package com.simplificacontabil.controller;

import com.simplificacontabil.dto.CategoriaDTO;
import com.simplificacontabil.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @PostMapping
    public CategoriaDTO criar(@RequestBody CategoriaDTO dto) {
        return categoriaService.salvar(dto);
    }

    @GetMapping
    public List<CategoriaDTO> listar() {
        return categoriaService.listarTodos();
    }

    @GetMapping("/{id}")
    public CategoriaDTO buscar(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
    }
}
