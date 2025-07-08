package com.simplificacontabil.repository;

import com.simplificacontabil.model.RefreshToken;
import com.simplificacontabil.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUsuarioId(Long id);
}
