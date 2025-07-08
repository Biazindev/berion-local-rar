package com.simplificacontabil.integracoes.nuvemfiscal.service;
import com.simplificacontabil.integracoes.nuvemfiscal.client.nfe.NuvemFiscalNfeClient;

import com.simplificacontabil.integracoes.nuvemfiscal.mapper.VendaNFeMapper;
import com.simplificacontabil.service.ConfiguracaoEmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NuvemFiscalService {

    private final NuvemFiscalNfeClient client;
    private final VendaNFeMapper vendaNFeMapper;
    private final ConfiguracaoEmpresaService configuracaoEmpresaService;

//    public String emitirNotaFiscal(String apiKey, Venda venda) {
//        // Busca a empresa configurada via API KEY
//        ConfiguracaoEmpresa config = configuracaoEmpresaService.buscarPorApiKey(apiKey);
//
//        EmitenteDTO emitenteDTO = EmitenteDTO.builder()
//                .cnpj(config.getDocumento())
//                .razaoSocial(config.getRazaoSocial())
//                .nomeFantasia(config.getNomeFantasia())
//                .inscricaoEstadual(config.getInscricaoEstadual())
//                .regimeTributario(config.getRegimeTributario())
//                .build();
//
//        // Corrigido: agora passando o emitenteDTO direto
//        EmitirNFeRequest request = vendaNFeMapper.toEmitirNFeRequest(venda, emitenteDTO);
//
//        return client.emitirNfe(request);
//    }

}
