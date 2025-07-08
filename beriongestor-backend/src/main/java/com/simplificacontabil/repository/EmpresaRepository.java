package com.simplificacontabil.repository;

import com.simplificacontabil.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository <Empresa, Long> {
}
