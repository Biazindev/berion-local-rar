package com.simplificacontabil.repository;

import com.simplificacontabil.model.PasswordResetToken;
import com.simplificacontabil.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    @Modifying                // <-- informa ao Spring Data que é um DELETE/UPDATE
    @Transactional            // <-- garante tx para o delete
    @Query("delete from PasswordResetToken t where t.usuario = :usuario")
    void deleteByUsuario(Usuario usuario);
}
