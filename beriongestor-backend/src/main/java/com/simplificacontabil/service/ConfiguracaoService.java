package com.simplificacontabil.service;

import com.simplificacontabil.repository.ConfiguracaoRepository;
import com.simplificacontabil.model.Configuracao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfiguracaoService {

    private final ConfiguracaoRepository configuracaoRepository;

    public boolean precisaCodigoAcesso() {
        // Verifica no banco se a chave 'precisaCodigoAcesso' existe e retorna o valor
        Configuracao configuracao = configuracaoRepository.findByChave("precisaCodigoAcesso");
        if (configuracao != null) {
            return "sim".equals(configuracao.getValor());  // Retorna true se o valor for 'sim'
        }
        return false;  // Se a chave não for encontrada ou o valor não for 'sim', não precisa de código
    }
}
