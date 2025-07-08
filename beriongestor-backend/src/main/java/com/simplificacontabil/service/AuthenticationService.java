package com.simplificacontabil.service;

import com.simplificacontabil.enums.Perfil;
import com.simplificacontabil.repository.ClienteRepository;
import com.simplificacontabil.repository.UsuarioRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class AuthenticationService {


    private final UsuarioRepository usuarioRepository;


    public AuthenticationService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    private Key getSigningKey() {
        String secretKey = "KnzbiH5M46W2BqWBBOn4IVd3eqI2Qxx3jM8gf3J1iAH5w8nGupcqswYtcbFKj5KEM4M5KZlf68iIZHo2OCDq0g==";
        byte[] keyBytes = Base64.getDecoder().decode(secretKey.trim());
        System.out.println("Tamanho da chave em bytes: " + keyBytes.length);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, List<String> roles) {
        long now = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date(now));
        if (!roles.contains("ADMIN")) {
            builder.setExpiration(new Date(now + 3_600_000));
        }
        return builder
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }


    // Método para carregar detalhes do usuário
    public UserDetails loadUserByUsername(String username) {
        usuarioRepository.findByUsername(username);
        return new User(username, "senha", new ArrayList<>());
    }
    // Método para validar o token
    public boolean isTokenValid(String token) {
        try {
            // Verifica se o token pode ser analisado e se ele é válido
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);  // Tenta analisar o token

            return true;  
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
