package com.simplificacontabil.repository;


import com.simplificacontabil.enums.StatusPedido;
import com.simplificacontabil.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
        List<Pedido> findByStatus(StatusPedido status);
        List<Pedido> findByMesaId(Long mesaId);
        List<Pedido> findByStatusAndMesaId(StatusPedido status, Long mesaId);
        List<Pedido> findByMesaIsNullAndStatus(StatusPedido status);
}


