package com.simplificacontabil.controller;

import com.simplificacontabil.model.AuthenticationRequest;
import com.simplificacontabil.model.CodeValidationRequest;
import com.simplificacontabil.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthenticationRequest req,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        log.info("Iniciando processo de login para o usuário: {}", req.getUsername());
        return loginService.realizarLogin(req, request, response);
    }

    @PostMapping("/validate-code")
    public ResponseEntity<?> validateCode(
            @RequestBody CodeValidationRequest req,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        log.info("Validando código de autenticação para o usuário: {}", req.getEmail());
        return loginService.validateAuthCode(req, request, response);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            @CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        return loginService.realizarRefreshToken(refreshToken, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return loginService.realizarLogout(response);
    }
}
