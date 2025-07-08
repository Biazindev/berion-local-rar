package com.simplificacontabil.service;

import com.simplificacontabil.model.LoginEvent;
import com.simplificacontabil.repository.LoginEventRepository;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class LoginEventService {

    private final LoginEventRepository loginEventRepository;
    private final Map<String, Bucket> bucketsPorIp = new ConcurrentHashMap<>();

    public void registrarLogin(String username, String ip, String userAgent, boolean sucesso) {
        LoginEvent evento = LoginEvent.builder()
                .username(username)
                .ip(ip)
                .userAgent(userAgent)
                .timestamp(Instant.now())
                .Issucesso(sucesso)
                .build();
        loginEventRepository.save(evento);
    }

    public boolean podeTentar(String ip) {
        Bucket bucket = bucketsPorIp.computeIfAbsent(ip, this::criarBucket);
        return bucket.tryConsume(1);
    }

    private Bucket criarBucket(String ip) {
        // Limite de 5 tentativas por 15 minutos
        Bandwidth limite = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(15)));
        return Bucket.builder().addLimit(limite).build();
    }

    public String extrairIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
