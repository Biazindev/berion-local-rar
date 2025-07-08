package com.simplificacontabil.dto;

import com.simplificacontabil.enums.FormaPagamento;
import com.simplificacontabil.model.Cliente;
import com.simplificacontabil.model.Emitente;
import com.simplificacontabil.model.Pagamento;
import jakarta.persistence.JoinColumn;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendaDTO {

    private Long id;
    private String documentoCliente;  // CNPJ ou CPF do cliente (emitente)
    private List<ItemVendaDTO> itens;  // Lista de itens (Produtos) vendidos (Definindo aqui o campo itensVenda)
    private FormaPagamento formaPagamento;  // Forma de pagamento
    private LocalDateTime dataVenda;  // Data da venda
    private String status;  // Status da venda (ex: "Pendente", "Pago", etc.)
    private Long clienteId;  // ID do cliente a ser associado à venda.
    private Cliente cliente;
    private boolean emitirNotaFiscal = false;
    private Emitente emitente;
    private boolean vendaAnonima;
    private Pagamento pagamento;


    public Boolean isEmitirNotaFiscal() {
        return true;
    }
}
