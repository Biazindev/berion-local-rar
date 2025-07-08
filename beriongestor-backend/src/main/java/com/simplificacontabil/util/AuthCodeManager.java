package com.simplificacontabil.util;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthCodeManager {

    private Map<String, AuthCode> authCodes = new HashMap<>();
    private static final int CODE_EXPIRATION_MINUTES = 10;  // Código expira após 10 minutos

    // Armazena o código com a data de expiração
    public void storeAuthCode(String email, String code) {
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES);
        authCodes.put(email, new AuthCode(code, expirationTime));
    }

    // Valida o código fornecido pelo usuário
    public boolean validateAuthCode(String email, String code) {
        AuthCode storedCode = authCodes.get(email);

        if (storedCode == null || storedCode.getExpirationTime().isBefore(LocalDateTime.now())) {
            authCodes.remove(email);  // Remove o código expirado
            return false;  // Código inválido ou expirado
        }

        // Verifica se o código corresponde ao armazenado
        return storedCode.getCode().equals(code);
    }

    // Classe interna para armazenar o código e a data de expiração
    private static class AuthCode {
        private String code;
        private LocalDateTime expirationTime;

        public AuthCode(String code, LocalDateTime expirationTime) {
            this.code = code;
            this.expirationTime = expirationTime;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }
}
