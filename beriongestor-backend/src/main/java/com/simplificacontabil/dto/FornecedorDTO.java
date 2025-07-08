package com.simplificacontabil.dto;

import com.simplificacontabil.enums.TipoPessoa;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorDTO {
    private Long id;
    private TipoPessoa tipoPessoa;
    private PessoaFisicaDTO pessoaFisica;
    private PessoaJuridicaDTO pessoaJuridica;
    private String telefone;
    private String email;
}