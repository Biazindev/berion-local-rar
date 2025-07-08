package com.simplificacontabil.dto;

import com.simplificacontabil.enums.TipoPessoa;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {
        private Long id;
        private TipoPessoa tipoPessoa;
        private PessoaFisicaDTO pessoaFisica;
        private PessoaJuridicaDTO pessoaJuridica;
}
