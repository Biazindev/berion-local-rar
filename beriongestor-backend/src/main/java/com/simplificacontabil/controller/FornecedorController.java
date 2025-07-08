package com.simplificacontabil.controller;

import com.simplificacontabil.dto.FornecedorDTO;
import com.simplificacontabil.exception.ValidationException;
import com.simplificacontabil.mapper.FornecedorMapper;
import com.simplificacontabil.model.Fornecedor;
import com.simplificacontabil.service.FornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {
    private final FornecedorService service;
    private final FornecedorMapper mapper;

    @PostMapping
    public ResponseEntity<String> criar(@Valid @RequestBody FornecedorDTO dto) {
        // validação básica
        if (dto.getTipoPessoa() == null ||
                (dto.getTipoPessoa().name().equals("FISICA") && dto.getPessoaFisica() == null) ||
                (dto.getTipoPessoa().name().equals("JURIDICA") && dto.getPessoaJuridica() == null)) {
            throw new ValidationException("Dados de fornecedor inconsistentes.");
        }
        Fornecedor f = mapper.toEntity(dto);
        service.criar(f);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Fornecedor criado com sucesso!");
    }

    @GetMapping
    public List<FornecedorDTO> listar() {
        return service.listarTodos()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorDTO> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(mapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ValidationException("Fornecedor não encontrado: " + id));
    }

    @PutMapping("/{id}")
    public FornecedorDTO atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FornecedorDTO dto) {
        Fornecedor updated = service.atualizar(id, mapper.toEntity(dto));
        return mapper.toDTO(updated);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
