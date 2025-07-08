package com.simplificacontabil.controller;

import com.simplificacontabil.dto.EmpresaDTO;
import com.simplificacontabil.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService service;

    @PostMapping
    public ResponseEntity<EmpresaDTO> salvar(@RequestBody EmpresaDTO dto) {
        EmpresaDTO empresa = service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(empresa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> buscarPorId(@PathVariable Long id) {
        EmpresaDTO empresa = service.buscarPorId(id);
        return ResponseEntity.ok(empresa);
    }

    @GetMapping
    public ResponseEntity<List<EmpresaDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDTO> atualizar(@PathVariable Long id, @RequestBody EmpresaDTO dto) {
        EmpresaDTO empresa = service.atualizar(id, dto);
        return ResponseEntity.ok(empresa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
