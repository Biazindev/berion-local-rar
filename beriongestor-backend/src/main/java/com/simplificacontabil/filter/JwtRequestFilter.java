package com.simplificacontabil.filter;

import com.simplificacontabil.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    public JwtRequestFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod(); // 👈 agora tá resolvido
        return path.equals("/auth/login") ||
                 path.equals("/auth/refresh-token") ||
                (path.equals("/usuario") && method.equals("POST")); // só ignora o filtro no POST de criação

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String redirectUri = request.getParameter("redirect_uri");
        if (redirectUri != null &&
                           !(redirectUri.startsWith("http://localhost:3000")
                        || redirectUri.startsWith("https://localhost:3000"))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Redirecionamento não autorizado");
            return;
        }

        String token = null;
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

        // Somente pega do cookie se o header não estiver presente
        if (!StringUtils.hasText(token)) {
            Cookie cookie = WebUtils.getCookie(request, "ACCESS_TOKEN");
            if (cookie != null && StringUtils.hasText(cookie.getValue())) {
                token = cookie.getValue();
            }
        }

        // 🔐 Log token parcialmente mascarado
        if (StringUtils.hasText(token)) {
            String tokenFinal = token.length() > 20
                    ? token.substring(0, 10) + "..." + token.substring(token.length() - 10)
                    : token;
            log.info("🔐 Token final usado: {}", tokenFinal);
        } else {
            log.info("🔐 Nenhum token JWT presente");
        }

        if (StringUtils.hasText(token)) {
            try {
                if (jwtTokenService.isValid(token)) {
                    String username = jwtTokenService.extractUsername(token);
                    List<SimpleGrantedAuthority> authorities = jwtTokenService.extractRoles(token);
                    var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info("🔐 User authenticated: {}", username);
                }
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
                log.error("❌ Erro ao validar JWT token", ex);
            }
        }

        chain.doFilter(request, response);
    }

}
