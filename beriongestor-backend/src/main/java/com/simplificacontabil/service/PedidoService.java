package com.simplificacontabil.service;

import com.simplificacontabil.dto.EnderecoDTO;
import com.simplificacontabil.dto.PedidoDTO;
import com.simplificacontabil.dto.PedidoEntregaDTO;
import com.simplificacontabil.enums.StatusPedido;
import com.simplificacontabil.enums.TipoPessoa;
import com.simplificacontabil.mapper.EnderecoMapper;
import com.simplificacontabil.model.*;
import com.simplificacontabil.repository.ClienteRepository;
import com.simplificacontabil.repository.MesaRepository;
import com.simplificacontabil.repository.PedidoRepository;
import com.simplificacontabil.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.simplificacontabil.mapper.EnderecoMapper;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final MesaRepository mesaRepository;
    private final ClienteRepository clienteRepository;
    private final EnderecoMapper enderecoMapper;

    public void salvar(PedidoDTO dto) {
        Pedido pedido = new Pedido();

        if (dto.getMesaId() != null) {
            mesaRepository.findById(dto.getMesaId()).ifPresent(pedido::setMesa);
        }

        pedido.setHoraPedido(LocalDateTime.now());
        pedido.setPago(dto.isPago());
        pedido.setStatus(StatusPedido.EM_PREPARO);
        pedido.setObservacao(dto.getObservacao());
        pedido.setNomeMotoboy(dto.getNomeMotoboy());
        pedido.setTrocoPara(dto.getTrocoPara());
        pedido.setPrecisaTroco(dto.getPrecisaTroco());

        // ✅ SEMPRE vincula o cliente
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        pedido.setCliente(cliente);

        // 🔁 Endereço: do cliente ou personalizado
        EnderecoDTO enderecoEntregaDTO = dto.getEndereco();
        if (enderecoEntregaDTO == null) {
            Endereco enderecoEntidade = cliente.getPessoaFisica() != null
                    ? cliente.getPessoaFisica().getEndereco()
                    : cliente.getPessoaJuridica().getEndereco();
            pedido.setEnderecoEntrega(enderecoEntidade);
        } else {
            pedido.setEnderecoEntrega(enderecoMapper.toEntity(enderecoEntregaDTO));
        }

        // 🔁 Itens
        List<ItemPedido> itens = dto.getItens().stream().map(item -> {
            Produto produto = produtoRepository.findById(item.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + item.getProdutoId()));

            BigDecimal total = produto.getPrecoUnitario()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()));

            return ItemPedido.builder()
                    .produtoId(produto.getId())
                    .quantidade(item.getQuantidade())
                    .precoUnitario(produto.getPrecoUnitario())
                    .total(total)
                    .observacao(item.getObservacao())
                    .build();
        }).toList();

        pedido.setItens(itens);
        itens.forEach(i -> i.setPedido(pedido));

        pedidoRepository.save(pedido);
    }


    public void editarPedido(Long id, PedidoDTO dto) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setMesa(mesaRepository.findById(dto.getMesaId()).orElseThrow());
        pedido.setPago(dto.isPago());
        pedido.setObservacao(dto.getObservacao());
        pedido.setNomeMotoboy(dto.getNomeMotoboy());
        pedido.setTrocoPara(dto.getTrocoPara());
        pedido.setPrecisaTroco(dto.getPrecisaTroco());

        // Atualiza endereço (se veio novo)
        if (dto.getEndereco() != null) {
            pedido.setEnderecoEntrega(enderecoMapper.toEntity(dto.getEndereco()));
        }

        pedido.getItens().clear();

        List<ItemPedido> novosItens = dto.getItens().stream().map(item -> {
            Produto produto = produtoRepository.findById(item.getProdutoId())
                    .orElseThrow();

            BigDecimal total = produto.getPrecoUnitario()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()));

            return ItemPedido.builder()
                    .pedido(pedido)
                    .produtoId(produto.getId())
                    .quantidade(item.getQuantidade())
                    .precoUnitario(produto.getPrecoUnitario())
                    .total(total)
                    .observacao(item.getObservacao())
                    .build();
        }).toList();

        pedido.getItens().addAll(novosItens);
        pedidoRepository.save(pedido);
    }

    public void cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    public void finalizarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setStatus(StatusPedido.CONCLUIDO);
        pedidoRepository.save(pedido);
    }

    public void sairParaEntrega(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setStatus(StatusPedido.SAIU_PARA_ENTREGA);
        pedidoRepository.save(pedido);
    }
    public Pedido criarPedidoEntrega(PedidoEntregaDTO dto) {
        Cliente cliente;

        if (dto.getCliente_id() != null) {
            cliente = clienteRepository.findById(dto.getCliente_id())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        } else if (dto.getFone() != null && !dto.getFone().isBlank()) {
            cliente = clienteRepository.findByPessoaFisica_Telefone(dto.getFone())
                    .orElseGet(() -> {
                        PessoaFisica pf = PessoaFisica.builder()
                                .nome("Cliente WhatsApp")
                                .telefone(dto.getFone())
                                .build();

                        return clienteRepository.save(
                                Cliente.builder()
                                        .tipoPessoa(TipoPessoa.FISICA)
                                        .pessoaFisica(pf)
                                        .dataCadastro(LocalDateTime.now())
                                        .build()
                        );
                    });
        } else {
            throw new RuntimeException("É necessário informar cliente_id ou telefone.");
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEnderecoEntrega(enderecoMapper.toEntity(dto.getEnderecoEntrega()));
        pedido.setPago(dto.isPago());
        pedido.setHoraPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.valueOf(dto.getStatus()));
        pedido.setObservacao(dto.getObservacao());
        pedido.setNomeMotoboy(dto.getNomeMotoboy());

        List<ItemPedido> itens = dto.getProdutos().stream()
                .map(produtoDTO -> {
                    Produto produto = produtoRepository.findById(produtoDTO.getId())
                            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

                    BigDecimal total = produto.getPrecoUnitario()
                            .multiply(BigDecimal.valueOf(produtoDTO.getQuantidade()));

                    return ItemPedido.builder()
                            .pedido(pedido)
                            .produtoId(produto.getId())
                            .quantidade(produtoDTO.getQuantidade())
                            .precoUnitario(produto.getPrecoUnitario())
                            .total(total)
                            .observacao(produtoDTO.getObservacao())
                            .build();
                }).toList();

        pedido.setItens(itens);

        return pedidoRepository.save(pedido);
    }


}
