package com.simplificacontabil.controller;

import com.simplificacontabil.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/help")

public class PasswordResetController {

    @Autowired
    private PasswordResetService resetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgot(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        resetService.createAndSendToken(email);
        return ResponseEntity.ok(Map.of("message","E-mail enviado"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> reset(@RequestBody Map<String,String> body) {
        String token = body.get("token");
        String newPass = body.get("newPassword");
        resetService.resetPassword(token, newPass);
        return ResponseEntity.ok(Map.of("message","Senha alterada com sucesso"));
    }
}
