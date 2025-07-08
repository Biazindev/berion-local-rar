package com.simplificacontabil.service;

import com.simplificacontabil.model.PessoaJuridica;
import com.simplificacontabil.repository.PessoaJuridicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PessoaJuridicaService {

    private final PessoaJuridicaRepository repository;

    // Método para salvar ou atualizar PessoaJuridica
    public PessoaJuridica salvar(PessoaJuridica cliente) {
        return repository.save(cliente);
    }

    // Método para buscar a PessoaJuridica por CNPJ
    public PessoaJuridica buscarPorCnpj(String cnpj) {
        return repository.findByCnpj(cnpj)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    // Método para buscar por ID (caso precise de buscas mais genéricas)
    public Optional<PessoaJuridica> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // Método para excluir um cliente por ID
    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
