package com.simplificacontabil.service;

import com.simplificacontabil.filter.LoginRateLimiterService;
import com.simplificacontabil.model.AuthenticationRequest;
import com.simplificacontabil.model.CodeValidationRequest;
import com.simplificacontabil.model.RefreshToken;
import com.simplificacontabil.model.Usuario;
import com.simplificacontabil.repository.UsuarioRepository;
import com.simplificacontabil.service.ConfiguracaoService;
import com.simplificacontabil.util.AuthCodeManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService userDetailsService;
    private final UsuarioRepository usuarioRepository;
    private final LoginEventService loginEventService;
    private final JavaMailSender javaMailSender;
    private final AuthCodeManager authCodeManager;
    private final ConfiguracaoService configuracaoService;

    public ResponseEntity<?> realizarLogin(AuthenticationRequest req, HttpServletRequest request, HttpServletResponse response) {
        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        if (req.getUsername() == null || req.getUsername().isBlank() ||
                req.getPassword() == null || req.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário e senha são obrigatórios");
        }

        try {
            // Busca por username ou email
            Usuario usuario = usuarioRepository.findByUsername(req.getUsername());
            if (usuario == null) {
                usuario = usuarioRepository.findByEmail(req.getUsername())
                        .orElse(null);
            }
            if (usuario == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado");
            }

            // Autentica com o username real
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario.getUsername(), req.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getUsername());
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(r -> r.replace("ROLE_", ""))
                    .collect(Collectors.toList());

            // Verifica se precisa de código de acesso
            boolean precisaCodigoAcesso = configuracaoService.precisaCodigoAcesso();

            if (precisaCodigoAcesso) {
                String authCode = generateAuthCode();
                sendAuthCodeByEmail(usuario.getEmail(), authCode);
                authCodeManager.storeAuthCode(usuario.getEmail(), authCode);

                return ResponseEntity.ok(Map.of(
                        "message", "Código de autenticação enviado por e-mail. Insira o código para completar o login."
                ));
            }

            return completeLogin(usuario, roles, response, request);

        } catch (BadCredentialsException e) {
            loginEventService.registrarLogin(req.getUsername(), ip, userAgent, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuário ou senha inválidos"));
        } catch (Exception e) {
            log.error("Erro durante autenticação:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }




    public ResponseEntity<?> realizarRefreshToken(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token ausente"));
        }

        RefreshToken rt = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token Invalido"));

        if (!refreshTokenService.isValid(rt)) {
            throw new RuntimeException("Expired refresh token");
        }

        String username = rt.getUsuario().getUsername();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(r -> r.replace("ROLE_", ""))
                .collect(Collectors.toList());

        String newJwt = authenticationService.generateToken(username, roles);

        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from("ACCESS_TOKEN", newJwt)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("None");

        if (!roles.contains("ADMIN")) {
            cookieBuilder.maxAge(Duration.ofMinutes(30));
        }

        response.addHeader(HttpHeaders.SET_COOKIE, cookieBuilder.build().toString());

        return ResponseEntity.ok(Map.of("accessToken", newJwt));
    }

    public ResponseEntity<?> realizarLogout(HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            Usuario usuario = usuarioRepository.findByUsername(username);
            refreshTokenService.deleteByUsuario(usuario);
        }

        // Invalida os cookies
        ResponseCookie deleteAccess = ResponseCookie.from("ACCESS_TOKEN", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        ResponseCookie deleteRefresh = ResponseCookie.from("REFRESH_TOKEN", "")
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh-token")
                .maxAge(0)
                .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefresh.toString());

        // Limpa o contexto de autenticação
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Logout realizado com sucesso.");
    }


    private String getClientIp(HttpServletRequest request) {
        String cfIp = request.getHeader("CF-Connecting-IP");
        if (cfIp != null && !cfIp.isEmpty()) {
            return cfIp;
        }
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isEmpty()) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String generateAuthCode() {
        Random rand = new Random();
        int code = rand.nextInt(999999); // Gera um número aleatório entre 0 e 999999
        return String.format("%06d", code);  // Formata para 6 dígitos
    }
    private void sendAuthCodeByEmail(String toEmail, String authCode) {
        String subject = "Código de Autenticação";
        String text = "Seu código de autenticação é: " + authCode + "\nEste código expira em 10 minutos.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("help@biazinsistemas.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
    }


    public ResponseEntity<?> validateAuthCode(CodeValidationRequest req, HttpServletRequest request, HttpServletResponse response) {
        // Valida o código de autenticação
        boolean isValid = authCodeManager.validateAuthCode(req.getEmail(), req.getCode());

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Código inválido ou expirado"));
        }

        // Se o código for válido, você pode continuar o processo de login
        Usuario usuario = usuarioRepository.findByUsername(req.getEmail());
        List<String> roles = extractRoles(userDetailsService.loadUserByUsername(req.getEmail()));

        return completeLogin(usuario, roles, response, request);
    }


    private List<String> extractRoles(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(r -> r.replace("ROLE_", ""))
                .collect(Collectors.toList());
    }

    private ResponseEntity<?> completeLogin(Usuario usuario, List<String> roles, HttpServletResponse response, HttpServletRequest request) {
        try {
            // Gera o token de acesso
            String accessToken = authenticationService.generateToken(usuario.getUsername(), roles);

            // Cria o refresh token
            RefreshToken rt = refreshTokenService.create(usuario, request);

            // Adiciona os cookies de tokens à resposta
            addTokensToResponse(response, accessToken, rt, roles);

            loginEventService.registrarLogin(usuario.getUsername(), getClientIp(request), request.getHeader("User-Agent"), true);

            return ResponseEntity.ok(Map.of(
                    "id", usuario.getId(),
                    "accessToken", accessToken,
                    "username", usuario.getUsername(),
                    "roles", roles
            ));
        } catch (Exception e) {
            log.error("Erro durante o login:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erro interno ao processar login"));
        }
    }

    private void addTokensToResponse(HttpServletResponse response, String accessToken, RefreshToken rt, List<String> roles) {
        // Cria o cookie para o refresh token
        ResponseCookie refreshCookie = ResponseCookie.from("REFRESH_TOKEN", rt.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh-token") // Caminho onde o cookie estará disponível
                .maxAge(Duration.ofDays(7)) // O refresh token é válido por 7 dias
                .sameSite("None")  // Para permitir o cookie em requisições cross-site
                .build();

        // Cria o cookie para o access token
        ResponseCookie.ResponseCookieBuilder accessCookieBuilder = ResponseCookie.from("ACCESS_TOKEN", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")  // O access token estará disponível para todas as rotas
                .sameSite("None"); // Para permitir o cookie em requisições cross-site

        // Ajusta a duração do cookie de access token com base na role do usuário
        if (!roles.contains("ADMIN")) {
            // Se o usuário não for admin, o access token terá uma duração de 15 minutos
            accessCookieBuilder.maxAge(Duration.ofMinutes(15));
        } else {
            // Se for admin, o access token terá uma duração de 1 dia
            accessCookieBuilder.maxAge(Duration.ofDays(1));
        }

        // Adiciona os cookies de refresh token e access token à resposta
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookieBuilder.build().toString());
    }



}
