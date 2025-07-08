package com.simplificacontabil.controller;

import com.simplificacontabil.dto.PedidoEntregaDTO;
import com.simplificacontabil.model.PedidoEntrega;
import com.simplificacontabil.model.VendaPedido;
import com.simplificacontabil.service.PedidoEntregaService;
import com.simplificacontabil.mapper.PedidoEntregaMapper;
import com.simplificacontabil.service.VendaPedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos-entrega")
@RequiredArgsConstructor
public class PedidoEntregaController {

    private final PedidoEntregaService pedidoEntregaService;
    private final PedidoEntregaMapper mapper;
    private final VendaPedidoService vendaPedidoService;

    @PostMapping
    public ResponseEntity<PedidoEntregaDTO> criar(@RequestBody PedidoEntregaDTO dto) {
        PedidoEntrega salvo = pedidoEntregaService.salvar(mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDto(salvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoEntregaDTO> buscarPorId(@PathVariable Long id) {
        PedidoEntrega pedido = pedidoEntregaService.buscarPorId(id);
        return ResponseEntity.ok(mapper.toDto(pedido));
    }

    @GetMapping
    public ResponseEntity<List<PedidoEntregaDTO>> listarTodos() {
        List<PedidoEntrega> lista = pedidoEntregaService.buscarTodos();
        return ResponseEntity.ok(lista.stream().map(mapper::toDto).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoEntregaDTO> atualizar(@PathVariable Long id, @RequestBody PedidoEntregaDTO dto) {
        PedidoEntrega atualizado = pedidoEntregaService.atualizar(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoEntregaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id}/venda")
    public ResponseEntity<?> gerarVenda(@PathVariable("id") Long pedidoId) {
        VendaPedido venda = vendaPedidoService.criarVendaDePedido(pedidoId);
        return ResponseEntity.ok(venda); // pode retornar DTO se quiser
    }


}

