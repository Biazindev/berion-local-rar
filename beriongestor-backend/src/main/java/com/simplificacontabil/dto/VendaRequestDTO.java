package com.simplificacontabil.dto;

import com.simplificacontabil.enums.ModeloNota;
import com.simplificacontabil.model.NfeFrete;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendaRequestDTO {

    private boolean emitirNotaFiscal;

    private String documentoCliente;

    private ClienteDTO cliente; // opcional, obrigatório se emitirNotaFiscal = true

    private Long emitenteId; // apenas o ID da empresa, necessário se emitirNotaFiscal = true

    private ModeloNota modelo; // NFE , NFCe — só necessário se emitirNotaFiscal = true

    private List<ItemVendaDTO> itens;

    private PagamentoDTO pagamento;

    private LocalDateTime dataVenda;

    private String status;

    @Embedded
    private NfeFrete frete;

    @Builder.Default
    @Column(nullable = false)
    private boolean vendaAnonima = false;

    private Integer modFrete; // 0, 1, 2, 9



}
