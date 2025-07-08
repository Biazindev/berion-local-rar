package com.simplificacontabil.service;

import com.simplificacontabil.exception.FornecedorNotFoundException;
import com.simplificacontabil.exception.ValidationException;
import com.simplificacontabil.model.Fornecedor;
import com.simplificacontabil.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FornecedorService {
    private final FornecedorRepository fornecedorRepository;

    public Fornecedor criar(Fornecedor fornecedor) {
        if (!fornecedor.isValid()) {
            throw new ValidationException("Preencha Pessoa Física ou Jurídica corretamente.");
        }
        return fornecedorRepository.save(fornecedor);
    }

    public Fornecedor atualizar(Long id, Fornecedor atualizado) {
        Fornecedor existente = fornecedorRepository.findById(id)
                .orElseThrow(() -> new FornecedorNotFoundException("Fornecedor não encontrado com ID: " + id));

        existente.setTipoPessoa(atualizado.getTipoPessoa());
        existente.setTelefone(atualizado.getTelefone());
        existente.setEmail(atualizado.getEmail());

        if (atualizado.getPessoaFisica() != null) {
            existente.setPessoaFisica(atualizado.getPessoaFisica());
            existente.setPessoaJuridica(null);
        } else if (atualizado.getPessoaJuridica() != null) {
            existente.setPessoaJuridica(atualizado.getPessoaJuridica());
            existente.setPessoaFisica(null);
        }

        return fornecedorRepository.save(existente);
    }

    public void excluir(Long id) {
        if (!fornecedorRepository.existsById(id)) {
            throw new FornecedorNotFoundException("Fornecedor não encontrado com ID: " + id);
        }
        fornecedorRepository.deleteById(id);
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    public Optional<Fornecedor> buscarPorId(Long id) {
        return fornecedorRepository.findById(id);
    }
}
