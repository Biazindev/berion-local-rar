package com.simplificacontabil.repository;

import com.simplificacontabil.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // ======== BUSCAS POR TELEFONE ========
    Optional<Cliente> findByPessoaFisica_Telefone(String telefone);

    Optional<Cliente> findByPessoaJuridica_Telefone(String telefone);

    // ======== BUSCAS POR DOCUMENTO (CPF/CNPJ) ========
    Optional<Cliente> findByPessoaFisica_Cpf(String cpf);

    Optional<Cliente> findByPessoaJuridica_Cnpj(String cnpj);

    // ======== BUSCAS POR DOCUMENTO========
    @Query("""

            select c
  from Cliente c
  left join c.pessoaFisica pf
  left join c.pessoaJuridica pj
  where 
    (pf.cpf is not null and 
     replace(replace(replace(pf.cpf, '.', ''), '-', ''), '/', '') = :doc)
    or 
    (pj.cnpj is not null and 
     replace(replace(replace(pj.cnpj, '.', ''), '-', ''), '/', '') = :doc)
    """)
    Optional<Cliente> findByDocumento(@Param("doc") String documento);

    Long countByDataCadastroBetween(LocalDateTime inicio, LocalDateTime fim);


//    // ======== BUSCAS POR TELEFONE========
//    @Query("""
//    SELECT c
//    FROM Cliente c
//    LEFT JOIN c.pessoaFisica pf
//    LEFT JOIN c.pessoaJuridica pj
//    WHERE
//        (pf.telefone IS NOT NULL AND
//         REPLACE(REPLACE(REPLACE(pf.telefone, '(', ''), ')', ''), '-', '') = :telefone)
//        OR
//        (pj.telefone IS NOT NULL AND
//         REPLACE(REPLACE(REPLACE(pj.telefone, '(', ''), ')', ''), '-', '') = :telefone)
//""")
//    Optional<Cliente> findByPessoaFisicaOrJuridicaTelefone(@Param("telefone") String telefone);

}