package com.simplificacontabil.controller;

import com.simplificacontabil.model.Filial;
import com.simplificacontabil.repository.FilialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filial")
@RequiredArgsConstructor
@Slf4j
public class FilialController {

    private final FilialRepository filialRepository;

    @GetMapping
    public ResponseEntity<List<Filial>> listarTodas() {
        List<Filial> filiais = filialRepository.findAll();
        return ResponseEntity.ok(filiais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filial> buscarPorId(@PathVariable Long id) {
        return filialRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Filial> criar(@RequestBody Filial filial) {
        Filial salva = filialRepository.save(filial);
        return ResponseEntity.ok(salva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Filial> atualizar(@PathVariable Long id, @RequestBody Filial novaFilial) {
        return filialRepository.findById(id)
                .map(filialExistente -> {
                    filialExistente.setNome(novaFilial.getNome());
                    filialExistente.setCnpj(novaFilial.getCnpj());
                    filialExistente.setEndereco(novaFilial.getEndereco());
                    filialExistente.setTelefone(novaFilial.getTelefone());
                    filialExistente.setEmail(novaFilial.getEmail());
                    filialExistente.setEmpresa(novaFilial.getEmpresa());
                    Filial atualizada = filialRepository.save(filialExistente);
                    return ResponseEntity.ok(atualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (filialRepository.existsById(id)) {
            filialRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
