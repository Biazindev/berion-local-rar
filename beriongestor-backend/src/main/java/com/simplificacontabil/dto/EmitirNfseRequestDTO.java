package com.simplificacontabil.dto;

import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse.ServicoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmitirNfseRequestDTO {

    private String cpfCnpjTomador;
    private String nomeTomador;
    private String telefone;
    private String email;

    private EnderecoDTO endereco;
    private ServicoDTO servico;
}
