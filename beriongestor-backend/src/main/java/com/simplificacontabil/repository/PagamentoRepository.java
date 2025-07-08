package com.simplificacontabil.repository;

import com.simplificacontabil.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository  extends JpaRepository<Pagamento, Long> {

    long countByStatusIgnoreCase(String status);

}
