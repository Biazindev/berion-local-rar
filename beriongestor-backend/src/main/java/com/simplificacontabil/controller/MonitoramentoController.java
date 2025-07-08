package com.simplificacontabil.controller;

import com.simplificacontabil.dto.IpBloqueadoDTO;
import com.simplificacontabil.filter.BotBlockFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class MonitoramentoController {

    private final BotBlockFilter botBlockFilter;

    @GetMapping("/ips-block")
    public List<IpBloqueadoDTO> listarIpsBloqueados() {
        long agora = System.currentTimeMillis();
        return botBlockFilter.getIpsBloqueados().stream()
                .map(ip -> {
                    Long expira = botBlockFilter.getIpExpiracoes().get(ip);
                    long tempoRestante = (expira != null ? expira - agora : 0);
                    return new IpBloqueadoDTO(ip, Math.max(tempoRestante, 0));
                })
                .collect(Collectors.toList());
    }
    @DeleteMapping("/desbloquear-ip")
    public ResponseEntity<?> desbloquearIp(@RequestParam String ip) {
        boolean removido = botBlockFilter.desbloquearIp(ip);
        if (removido) {
            return ResponseEntity.ok("✅ IP removido da lista de bloqueados: " + ip);
        } else {
            return ResponseEntity.status(404).body("⚠️ IP não estava bloqueado: " + ip);
        }
    }
}
