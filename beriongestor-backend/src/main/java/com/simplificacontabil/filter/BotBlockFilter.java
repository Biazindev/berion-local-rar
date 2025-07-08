package com.simplificacontabil.filter;

import com.simplificacontabil.service.CloudflareService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Component
public class BotBlockFilter extends OncePerRequestFilter {

    @Autowired
    private CloudflareService cloudflareService;

    @Value("${bot.filter.level:MEDIO}")
    private String nivelAgressividade; // LEVE, MEDIO, AGRESSIVO

    public Set<String> getIpsBloqueados() {
        return ipBloqueados;
    }

    public Map<String, Long> getIpExpiracoes() {
        return ipExpiracao;
    }

    private static final Logger logger = LoggerFactory.getLogger(BotBlockFilter.class);

    private static final String HEADER_NAME = "SIMPLIFICA-API-KEY";
    private static final String APP_KEY = "biaza";

    private static final Set<String> WHITELISTED_IPS = Set.of(
            "127.0.0.1", "::1", "0:0:0:0:0:0:0:1"
    );

    private static final Pattern WHITELISTED_USER_AGENT = Pattern.compile(
            ".*(PostmanRuntime|Insomnia|curl|Chrome|Opera|Brave|Safari|Edge|).*"
            );

    private static final Pattern MALICIOUS_PARAM_PATTERN = Pattern.compile(
            ".*(redirect_uri=|template=|post_logout_redirect_uri=|client_id=|scope=|id=|auth=|channel=|ref=|" +
                    "config=|\\.env|\\.git|sql=|script=|%3Cscript|base64,|eval\\(|php\\().*",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern UA_FAKE_OR_OLD_PATTERN = Pattern.compile(
            ".*(curl/|zgrab|assetnote|netcraft|masscan|hydra|nmap|sqlmap|nikto|crawler|" +
                    "Chrome/(\\d{1,2}|1[01][0-9])\\." +
                    "Android 4\\.|Android 5\\.|BNTV400|iPhone OS 13_|Mac OS X 10_13_|Knoppix|custom-asynchttpclient|" +
                    "Windows NT 6\\.|Firefox/[1-9][0-9]?\\.|Safari/53\\.|X11; Linux i686|" +
                    "Mozilla/5.0 zgrab|Mozilla/5.0 \\(compatible; NetcraftSurveyAgent).*",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern MALICIOUS_PATH_PATTERN = Pattern.compile(
            ".*(\\.(env|git)|/\\.(env|git|aws/credentials|envrc)|docker-compose|/wp-admin|/wp-login|/phpmyadmin|/admin|/cgi-bin|" +
                    "/setup|/install|/robots\\.txt|/sitemap\\.xml|/backup\\.zip|/env\\.backup|/debug\\.log|/phpinfo\\.php|/RDWeb/Pages|" +
                    "/\\.well-known|/config\\.json|/config\\.php|/env\\.bak|/\\.ssh|/\\.aws|/composer\\.json|/package\\.json|" +
                    "/server-status|/id_rsa|/db\\.sql|/backup|/old|/test|/tmp|/logs|/error\\.log|/debug|/core\\.dump|" +
                    "/index\\.php|/\\.DS_Store|/webui/|/geoserver/web/|\\.aspx|/get\\.php" +
                    ").*",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern MALICIOUS_USER_AGENT_PATTERN = Pattern.compile(
            ".*(sqlmap|crawler|scan|nmap|nikto|masscan|hydra).*", Pattern.CASE_INSENSITIVE
    );

    private final Set<String> ipBloqueados = ConcurrentHashMap.newKeySet();
    private final Map<String, Long> ipExpiracao = new ConcurrentHashMap<>();
    private static final long TEMPO_BLOQUEIO_MS = 10 * 60 * 1000;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = getClientIp(request);
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String userAgent = request.getHeader("User-Agent");
        String fullRequest = uri + (query != null ? ("?" + query) : "");

        String pais = request.getHeader("CF-IPCountry");
        boolean isSuspiciousCountry = pais != null && !"BR".equalsIgnoreCase(pais);

        String token = request.getHeader("Authorization");
        String tokenFinal = token != null && token.length() > 20
                ? token.substring(0, 10) + "..." + token.substring(token.length() - 10)
                : token;

        logger.info("🔍 Headers recebidos no BotBlockFilter: Authorization={}, SIMPLIFICA-API-KEY={}}",
                tokenFinal,
                request.getHeader("SIMPLIFICA-API-KEY")
        );

        if (request.getRequestURI().contains("/nfe/teste") || request.getRequestURI().contains("/api/nfe") || request.getRequestURI().contains("/api/nfse")) {
            logger.info("🔓 Liberando acesso ao endpoint teste: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        if (userAgent == null || userAgent.trim().isEmpty() || userAgent.toLowerCase().contains("custom-asynchttpclient")) {
            logger.warn("🕵️ User-Agent suspeito ou ausente - IP: {}", ip);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            return;
        }

        boolean possuiTokenJwt = token != null && token.startsWith("Bearer ");
        String apiKey = request.getHeader(HEADER_NAME);
        boolean possuiApiKeyValida = APP_KEY.equals(apiKey);

        if (possuiTokenJwt || possuiApiKeyValida) {
            logger.info("🔓 Acesso autenticado liberado - JWT ou API Key válida");
            filterChain.doFilter(request, response);
            return;
        }

        if (isIpBloqueado(ip)) {
            logger.warn("🚫 IP já bloqueado temporariamente: {}", ip);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "IP bloqueado temporariamente.");
            return;
        }

        boolean missingHeaders = request.getHeader("Accept") == null
                || request.getHeader("Accept-Language") == null
                || request.getHeader("Referer") == null;

        boolean isMalicious = (query != null && MALICIOUS_PARAM_PATTERN.matcher(query).find())
                || MALICIOUS_PATH_PATTERN.matcher(uri).find()
                || MALICIOUS_USER_AGENT_PATTERN.matcher(userAgent).find()
                || UA_FAKE_OR_OLD_PATTERN.matcher(userAgent).find();

        boolean bloquear = switch (nivelAgressividade.toUpperCase()) {
            case "LEVE" -> isMalicious;
            case "MEDIO" -> isMalicious || isSuspiciousCountry;
            case "AGRESSIVO" -> isMalicious || isSuspiciousCountry || missingHeaders;
            default -> isMalicious;
        };

        if (bloquear) {
            bloquearIp(ip);
            if (isSuspiciousCountry) {
                logger.warn("🌍 Tentativa bloqueada de país fora do BR: {} | IP: {}", pais, ip);
            }
            logger.warn("🔒 Bloqueio de tentativa suspeita [{}] - IP: {} | País: {} | REQ: {} | UA: {}",
                    nivelAgressividade.toUpperCase(), ip, pais, fullRequest, userAgent);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Requisição bloqueada.");
            return;
        }

        logger.info("🔍 Nova requisição recebida - IP: {} | URI: {} | UA: {}", ip, uri, userAgent);

        if (!WHITELISTED_IPS.contains(ip)) {
            if (!WHITELISTED_USER_AGENT.matcher(userAgent).matches()) {
                logger.warn("🚫 User-Agent não permitido: {} | IP: {}", userAgent, ip);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User-Agent não permitido");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String cfIp = request.getHeader("CF-Connecting-IP");
        if (cfIp != null && !cfIp.isEmpty()) {
            return cfIp;
        }
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private boolean isIpBloqueado(String ip) {
        Long expiracao = ipExpiracao.get(ip);
        if (expiracao != null && expiracao > System.currentTimeMillis()) {
            return true;
        } else {
            ipExpiracao.remove(ip);
            ipBloqueados.remove(ip);
            return false;
        }
    }

    private void bloquearIp(String ip) {
        ipBloqueados.add(ip);
        ipExpiracao.put(ip, System.currentTimeMillis() + TEMPO_BLOQUEIO_MS);
        try {
            cloudflareService.banIp(ip, "IP detectado com padrão malicioso");
        } catch (Exception e) {
            logger.error("❌ Erro ao enviar IP para Cloudflare: {}", e.getMessage());
        }
    }

    public boolean desbloquearIp(String ip) {
        boolean existia = ipBloqueados.remove(ip);
        ipExpiracao.remove(ip);
        return existia;
    }
}
