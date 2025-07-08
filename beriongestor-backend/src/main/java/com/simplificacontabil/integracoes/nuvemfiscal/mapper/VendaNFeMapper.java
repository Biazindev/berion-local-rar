package com.simplificacontabil.integracoes.nuvemfiscal.mapper;

import com.simplificacontabil.model.*;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.EnderecoNfeDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse.DestinatarioDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse.EmitirNFeRequest;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfse.ItemNFeDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class VendaNFeMapper {

    public EmitirNFeRequest toEmitirNFeRequest(Venda venda, com.simplificacontabil.dto.EmitenteDTO emitente) {
        Cliente cliente = venda.getCliente();

        DestinatarioDTO destinatario = DestinatarioDTO.builder()
                .CPF(cliente.getPessoaFisica() != null ? cliente.getPessoaFisica().getCpf() : null)
                .xNome(cliente.getPessoaFisica() != null
                        ? cliente.getPessoaFisica().getNome()
                        : cliente.getPessoaJuridica().getNomeFantasia())
                .enderDest(EnderecoNfeDTO.builder()
                        .xLgr((cliente.getPessoaFisica() != null
                        ? cliente.getPessoaFisica().getEndereco()
                        : cliente.getPessoaJuridica().getEndereco()).getLogradouro())
                        .nro((cliente.getPessoaFisica() != null
                        ? cliente.getPessoaFisica().getEndereco()
                        : cliente.getPessoaJuridica().getEndereco()).getNumero())
                        .xBairro((cliente.getPessoaFisica() != null
                        ? cliente.getPessoaFisica().getEndereco()
                        : cliente.getPessoaJuridica().getEndereco()).getBairro())
                        .xMun((cliente.getPessoaFisica() != null
                        ? cliente.getPessoaFisica().getEndereco()
                        : cliente.getPessoaJuridica().getEndereco()).getMunicipio())
                        .UF((cliente.getPessoaFisica() != null
                        ? cliente.getPessoaFisica().getEndereco()
                        : cliente.getPessoaJuridica().getEndereco()).getUf())
                        .CEP((cliente.getPessoaFisica() != null
                        ? cliente.getPessoaFisica().getEndereco()
                        : cliente.getPessoaJuridica().getEndereco()).getCep())
                        .build())
                .build();

        List<ItemNFeDTO> itens = venda.getItens().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());

        com.simplificacontabil.dto.PagamentoDTO pagamento = com.simplificacontabil.dto.PagamentoDTO.builder()
                .valorPago(String.valueOf(venda.getTotalVenda()))
                .build();

        return EmitirNFeRequest.builder()
                .emitente(emitente)
                .destinatario(destinatario)
                .itens(itens)
                .formasPagamento(List.of(pagamento))
                .build();
    }

    private com.simplificacontabil.dto.EnderecoDTO toEnderecoDTO(Endereco endereco) {
        return com.simplificacontabil.dto.EnderecoDTO.builder()
                .logradouro(endereco.getLogradouro())
                .numero(endereco.getNumero())
                .bairro(endereco.getBairro())
                .municipio(endereco.getMunicipio())
                .uf(endereco.getUf())
                .cep(endereco.getCep())
                .build();
    }

    private ItemNFeDTO toItemDTO(ItemVenda item) {
        return ItemNFeDTO.builder()
                .descricao(item.getNomeProduto())
                .codigoProduto(item.getProduto().getId().toString())
                .ncm(item.getProduto().getNcm() != null ? item.getProduto().getNcm() : "00000000")
                .cfop("5102")
                .unidadeComercial("UN")
                .quantidadeComercial(new BigDecimal(item.getQuantidade()))
                .valorUnitarioComercial(item.getPrecoUnitario())
                .valorBruto(item.getTotalItem())
                .build();
    }
}
