package com.simplificacontabil.controller;

import com.simplificacontabil.dto.ClienteDTO;
import com.simplificacontabil.enums.TipoPessoa;
import com.simplificacontabil.exception.ClienteNotFoundException;
import com.simplificacontabil.exception.ValidationException;
import com.simplificacontabil.model.Cliente;
import com.simplificacontabil.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<String> criarCliente(@Valid @RequestBody ClienteDTO dto) {
        boolean hasPF = dto.getPessoaFisica() != null;
        boolean hasPJ = dto.getPessoaJuridica() != null;
        if (hasPF == hasPJ) {
            throw new ValidationException("Informe exatamente Pessoa Física ou Pessoa Jurídica.");
        }

        if (hasPF) {
            String rawCpf = dto.getPessoaFisica().getCpf().replaceAll("\\D", "");
            if (rawCpf.length() != 11) {
                throw new ValidationException("CPF inválido. Deve ter 11 dígitos.");
            }
            dto.getPessoaFisica().setCpf(rawCpf);
        } else {
            String rawCnpj = dto.getPessoaJuridica().getCnpj().replaceAll("\\D", "");
            if (rawCnpj.length() != 14) {
                throw new ValidationException("CNPJ inválido. Deve ter 14 dígitos.");
            }
            dto.getPessoaJuridica().setCnpj(rawCnpj);
        }

        clienteService.criar(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Cliente criado com sucesso!");
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarTodos() {
        List<ClienteDTO> todos = clienteService.listarTodos();
        return ResponseEntity.ok(todos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteDTO dto
    ) {
        ClienteDTO atualizado = clienteService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        clienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        ClienteDTO dto = Optional.ofNullable(clienteService.buscarPorId(id))
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado com ID: " + id));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/buscar-cpf")
    public ResponseEntity<ClienteDTO> buscarPorCpf(@RequestParam String cpf) {
        ClienteDTO dto = clienteService.buscarPorCpf(cpf)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado com CPF: " + cpf));
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/buscar-documento")
    public ResponseEntity<ClienteDTO> buscarPorDocumento(@RequestParam String documento) {
        ClienteDTO dto = clienteService.buscarPorDocumento(documento)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado com documento: " + documento));
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/lote")
    public ResponseEntity<String> criarClientesEmLote(@RequestBody List<ClienteDTO> clientes) {
        clientes.forEach(clienteService::criar);
        return ResponseEntity.ok("Clientes criados com sucesso.");
    }
    @GetMapping("/quantidade-mes")
    public ResponseEntity<Long> getNovosClientesDoDia() {
        return ResponseEntity.ok(clienteService.getQuantidadeDoMes());
    }
    @GetMapping("/buscar-telefone")
    public ResponseEntity<ClienteDTO> buscarClientePorTelefone(String telefone){
        ClienteDTO dto = clienteService.buscarPorTelefone(telefone)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente não encontrado com telefone: " + telefone));
        return ResponseEntity.ok(dto);
    }
}
