package com.simplificacontabil.controller;

import com.simplificacontabil.dto.PedidoDTO;
import com.simplificacontabil.dto.PedidoEntregaDTO;
import com.simplificacontabil.dto.ProdutoDTO;
import com.simplificacontabil.enums.StatusPedido;
import com.simplificacontabil.mapper.PedidoMapper;
import com.simplificacontabil.model.ItemPedido;
import com.simplificacontabil.model.Pedido;
import com.simplificacontabil.repository.PedidoRepository;
import com.simplificacontabil.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PedidoDTO dto) {
        pedidoService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody PedidoDTO dto) {
        pedidoService.editarPedido(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizar(@PathVariable Long id) {
        pedidoService.finalizarPedido(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/entregar")
    public ResponseEntity<?> sairParaEntrega(@PathVariable Long id) {
        pedidoService.sairParaEntrega(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<Pedido> listarPedidos(@RequestParam(required = false) StatusPedido status, @RequestParam(required = false) Long mesaId){
        return List.of();
    }

    @GetMapping("/entrega-em-preparo")
    public ResponseEntity<List<PedidoEntregaDTO>> listarPedidosParaEntrega() {
        List<Pedido> pedidos = pedidoRepository.findByMesaIsNullAndStatus(StatusPedido.EM_PREPARO);

        List<PedidoEntregaDTO> resultado = pedidos.stream()
                .map(pedidoMapper::toEntregaDTO)
                .toList();

        return ResponseEntity.ok(resultado);
    }
}
