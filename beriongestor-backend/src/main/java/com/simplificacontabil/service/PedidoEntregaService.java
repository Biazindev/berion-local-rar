package com.simplificacontabil.service;

import com.simplificacontabil.model.PedidoEntrega;
import com.simplificacontabil.repository.PedidoEntregaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoEntregaService {

    private final PedidoEntregaRepository pedidoEntregaRepository;

    public PedidoEntrega salvar(PedidoEntrega pedidoEntrega) {
        return pedidoEntregaRepository.save(pedidoEntrega);
    }

    public List<PedidoEntrega> buscarTodos() {
        return pedidoEntregaRepository.findAll();
    }

    public PedidoEntrega buscarPorId(Long id) {
        return pedidoEntregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido de entrega não encontrado"));
    }

    public void deletar(Long id) {
        pedidoEntregaRepository.deleteById(id);
    }

    public PedidoEntrega atualizar(Long id, PedidoEntrega pedidoEntrega) {
        PedidoEntrega existente = buscarPorId(id);
        pedidoEntrega.setId(existente.getId());
        return pedidoEntregaRepository.save(pedidoEntrega);
    }


}
