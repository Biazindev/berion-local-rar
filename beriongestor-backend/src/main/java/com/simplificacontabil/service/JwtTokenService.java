package com.simplificacontabil.service;

import com.simplificacontabil.enums.Perfil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {


    private SecretKey getSigningKey() {
        String secretKey = "KnzbiH5M46W2BqWBBOn4IVd3eqI2Qxx3jM8gf3J1iAH5w8nGupcqswYtcbFKj5KEM4M5KZlf68iIZHo2OCDq0g==";
        byte[] keyBytes = Base64.getDecoder().decode(secretKey.trim());
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public List<SimpleGrantedAuthority> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object rolesObject = claims.get("roles");
        List<Perfil> perfis = new ArrayList<>();
        if (rolesObject instanceof List<?>) {
            perfis = ((List<?>) rolesObject).stream()
                    .map(obj -> Perfil.valueOf(obj.toString()))
                    .toList();
        }
        return perfis.stream()
                .map(p -> new SimpleGrantedAuthority("ROLE_" + p.name()))
                .toList();
    }

    private <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.resolve(claims);
    }

    @FunctionalInterface
    public interface ClaimsResolver<T> {
        T resolve(Claims claims);
    }

    public boolean isValid(String token) {
        Claims claims = extractAllClaims(token);
        Date expiration = claims.getExpiration();

        if (expiration == null) {
            List<String> roles = claims.get("roles", List.class);
            return roles != null && roles.contains("ADMIN");
        }

        return expiration.after(new Date());
    }

}
