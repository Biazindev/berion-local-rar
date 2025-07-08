package com.simplificacontabil.service;

import com.simplificacontabil.model.PessoaFisica;
import com.simplificacontabil.repository.PessoaFisicaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PessoaFisicaService {

    private final PessoaFisicaRepository repository;

    public PessoaFisica salvar(PessoaFisica pessoaFisica) {
        return repository.save(pessoaFisica);
    }

    public PessoaFisica buscarPorCpf(String cpf) {
        return repository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Pessoa Física não encontrada"));
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}
