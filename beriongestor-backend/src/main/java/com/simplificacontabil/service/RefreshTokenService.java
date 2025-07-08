package com.simplificacontabil.service;

import com.simplificacontabil.model.RefreshToken;
import com.simplificacontabil.model.Usuario;
import com.simplificacontabil.repository.RefreshTokenRepository;
import com.simplificacontabil.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${security.jwt.refresh-token.expiration-ms:604800000}") // padrão: 7 dias
    private Long refreshTokenDurationMs;

    private final SecureRandom secureRandom = new SecureRandom();


    @Transactional
    public RefreshToken create(Usuario usuario, HttpServletRequest request) {
        Usuario managedUser = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + usuario.getId()));

        // Remove tokens antigos do usuário
        refreshTokenRepository.deleteByUsuarioId(managedUser.getId());

        // 🔥 FORÇA O DELETE A EXECUTAR AGORA
        refreshTokenRepository.flush();

        // Captura IP e User-Agent da requisição
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String token = generateSecureToken();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .usuario(managedUser)
                .ipAddress(ip)
                .userAgent(userAgent)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isValid(RefreshToken token) {
        return token.getExpiryDate().isAfter(Instant.now());
    }

    private String generateSecureToken() {
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    public void deleteByUsuario(Usuario usuario) {
        Usuario managedUser = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        refreshTokenRepository.deleteByUsuarioId(managedUser.getId());
    }

}
