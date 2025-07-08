package com.simplificacontabil.integracoes.meli.repository;

import com.simplificacontabil.integracoes.meli.model.MercadoLivreToken;
import com.simplificacontabil.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MercadoLivreTokenRepository extends JpaRepository<MercadoLivreToken, Long> {
    Optional<MercadoLivreToken> findByUsuarioDoERP(Usuario usuario);
}

