package com.simplificacontabil.tomcat;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
public class EarlyBlockValve extends ValveBase {

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String userAgent = request.getHeader("User-Agent");
        String apiKey = request.getHeader("SIMPLIFICA-API-KEY");
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");
        String ip = request.getRemoteAddr();

        boolean isTrustedLocal =
                "127.0.0.1".equals(ip) || "::1".equals(ip) ||
                                (origin != null &&
                                   (origin.contains("localhost:3000")
                                || origin.contains("https://simplific-contabil.vercel.app")
                                || origin.contains(".biazinsistemas.com"))) ||
                                (referer != null &&
                                  (referer.contains("localhost:3000") ||
                                 referer.contains("https://simplific-contabil.vercel.app") ||
                                 referer.contains(".biazinsistemas.com")));

        if (isTrustedLocal) {
            getNext().invoke(request, response);
            return;
        }

        boolean isMissingHeaders = userAgent == null || origin == null || apiKey == null;

        boolean isBlocked = isMissingHeaders ||
                !apiKey.equals("biaza") ||
                RequestBlockPatterns.UA_FAKE_OR_OLD_PATTERN.matcher(userAgent).find() ||
                RequestBlockPatterns.MALICIOUS_USER_AGENT_PATTERN.matcher(userAgent).find() ||
                RequestBlockPatterns.MALICIOUS_PATH_PATTERN.matcher(uri).find() ||
                (query != null && RequestBlockPatterns.MALICIOUS_PARAM_PATTERN.matcher(query).find());

        if (isBlocked) {
            log.warn("🚫 Acesso bloqueado no Valve - IP: {} | UA: {} | URI: {} | query: {}",
                    ip, userAgent != null ? userAgent : "N/A", uri, query);

            response.setStatus(403);
            response.setContentType("text/plain;charset=UTF-8");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            try (PrintWriter writer = response.getWriter()) {
                writer.write("🔒 Acesso bloqueado pelo Valve :)");
                writer.flush();
            }
            return;
        }

        getNext().invoke(request, response);
    }
}
