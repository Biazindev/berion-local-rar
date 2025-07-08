package com.simplificacontabil.service;

import com.simplificacontabil.model.ItemPedidoEntrega;
import com.simplificacontabil.model.PedidoEntrega;
import com.simplificacontabil.model.VendaPedido;
import com.simplificacontabil.repository.PedidoEntregaRepository;
import com.simplificacontabil.repository.VendaPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class VendaPedidoService {

    private final PedidoEntregaRepository pedidoEntregaRepository;
    private final VendaPedidoRepository vendaRepository;

    @Autowired
    public VendaPedidoService(PedidoEntregaRepository pedidoEntregaRepository, VendaPedidoRepository vendaRepository) {
        this.pedidoEntregaRepository = pedidoEntregaRepository;
        this.vendaRepository = vendaRepository;
    }

    public VendaPedido criarVendaDePedido(Long pedidoId) {
        PedidoEntrega pedido = pedidoEntregaRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (!pedido.isPago()) {
            throw new RuntimeException("Pedido ainda não foi pago");
        }

        BigDecimal total = pedido.getProdutos().stream()
                .map(p -> p.getPrecoUnitario().multiply(BigDecimal.valueOf(p.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        VendaPedido venda = VendaPedido.builder()
                .clienteId(pedido.getClienteId())
                .anonima(pedido.getClienteId() == null)
                .dataVenda(LocalDateTime.now())
                .valorTotal(total)
                .observacao(pedido.getObservacao())
                .build();

        var itens = pedido.getProdutos().stream()
                .map(prod -> ItemPedidoEntrega.builder()
                        .produtoId(prod.getProdutoId())
                        .nomeProduto(prod.getNomeProduto())
                        .quantidade(prod.getQuantidade())
                        .precoUnitario(prod.getPrecoUnitario())
                        .totalItem(prod.getPrecoUnitario().multiply(BigDecimal.valueOf(prod.getQuantidade())))
                        .vendaPedido(venda)
                        .build())
                .collect(Collectors.toList());

        venda.setItens(itens);
        return vendaRepository.save(venda);
    }
}
