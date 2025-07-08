package com.simplificacontabil.integracoes.nuvemfiscal.service;

import com.simplificacontabil.integracoes.nuvemfiscal.client.nfce.NuvemFiscalNfceClient;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce.CancelarNfceResponseDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce.EmitirNfceRequestDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce.EmitirNfceResponseDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce.ListarNfceResponseDTO;
import com.simplificacontabil.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NfceService {

//    private final NuvemFiscalNfceClient nfceClient;
//    private final AuthenticationService jwtUtil;
//
//    public ListarNfceResponseDTO listar(String cpfCnpj, String ambiente,
//                                        Integer top, Integer skip, Boolean inlinecount,
//                                        String referencia, String chave, Integer serie) {
//        String token = "Bearer " + jwtUtil.getToken();
//        return nfceClient.listarNfce(token, cpfCnpj, ambiente,
//                top, skip, inlinecount,
//                referencia, chave, serie);
//    }
//
//    public EmitirNfceResponseDTO emitir(EmitirNfceRequestDTO dto) {
//        String token = "Bearer " + jwtUtil.getToken();
//        return nfceClient.emitirNfce(token, dto);
//    }
//
//    public CancelarNfceResponseDTO cancelar(String id, String justificativa) {
//        String token = "Bearer " + jwtUtil.getToken();
//        var req = new CancelarNfceResponseDTO();
//        req.setJustificativa(justificativa);
//        return nfceClient.cancelarNfce(token, id, req);
//    }
}



