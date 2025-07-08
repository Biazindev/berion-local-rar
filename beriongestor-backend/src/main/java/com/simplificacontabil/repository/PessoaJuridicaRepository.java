package com.simplificacontabil.repository;

import com.simplificacontabil.model.PessoaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long> {
    Optional<PessoaJuridica> findByCnpj(String cnpj);
}
