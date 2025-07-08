package com.simplificacontabil.repository;

import com.simplificacontabil.model.Mesa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    Optional<Mesa> findByNumero(Integer numero);

    List<Mesa> findByAbertaTrue(); // <-- esse aqui é o truque do JPA

    Optional<Mesa> findByNumeroAndAbertaTrue(Integer numero);

    List<Mesa> findAllByNumeroAndAbertaTrue(Integer numero);

    List<Mesa> findAllByNumeroOrderByIdDesc(Integer numero);

}

