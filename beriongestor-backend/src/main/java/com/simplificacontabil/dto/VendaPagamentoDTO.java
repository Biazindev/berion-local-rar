package com.simplificacontabil.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VendaPagamentoDTO {
    private VendaDTO venda;
    private PagamentoDTO pagamento;


}
