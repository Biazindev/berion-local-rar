package com.simplificacontabil.service;

import com.simplificacontabil.dto.MovimentacaoDTO;
import com.simplificacontabil.dto.ProducaoResponse;
import com.simplificacontabil.enums.TipoMovimentacao;
import com.simplificacontabil.mapper.MovimentacaoMapper;
import com.simplificacontabil.model.Estoque;
import com.simplificacontabil.model.MovimentacaoEstoque;
import com.simplificacontabil.model.Produto;
import com.simplificacontabil.model.Receita;
import com.simplificacontabil.repository.EstoqueRepository;
import com.simplificacontabil.repository.MovimentacaoEstoqueRepository;
import com.simplificacontabil.repository.ReceitaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProducaoService {

    @Autowired
    private MovimentacaoEstoqueRepository movRepo;

    @Autowired
    private EstoqueRepository estoqueRepo;

    @Autowired
    private ReceitaRepository receitaRepo;

    @Autowired
    private MovimentacaoMapper movMapper;

    /**
     * Produz uma dada quantidade de um produto final seguindo a receita:
     * 1) Debita (SAÍDA) do estoque todos os insumos necessários
     * 2) Credita (ENTRADA) a quantidade produzida do produto final
     * 3) Recalcula o saldo de todos os produtos impactados
     */
    @Transactional
    public ProducaoResponse produzir(Long receitaId, int qtd) {
        // 1) Busca a receita
        Receita receita = receitaRepo.findById(receitaId)
                .orElseThrow(() -> new EntityNotFoundException("Receita não encontrada: id=" + receitaId));

        // 2) Calcula o saldo ANTES da produção para o produto final
        Long produtoFinalId = receita.getProdutoFinal().getId();
        int saldoAntes = movRepo.calculaSaldo(produtoFinalId);

        // 3) Registra SAÍDAS de cada insumo
        receita.getItens().forEach(item -> {
            // total de insumo = quantidade por unidade de receita × qtd produzida
            int totalInsumo = item.getQuantidade() * qtd;

            MovimentacaoEstoque movSaida = MovimentacaoEstoque.builder()
                    .produto(item.getInsumo())
                    .quantidade(totalInsumo)
                    .tipo(TipoMovimentacao.SAIDA)
                    .dataMovimentacao(LocalDateTime.now())
                    .descricao("Produção de " + qtd + " × " + receita.getNome())
                    .build();

            movRepo.save(movSaida);
        });

        // 4) Registra ENTRADA do produto final
        MovimentacaoEstoque movEntrada = MovimentacaoEstoque.builder()
                .produto(receita.getProdutoFinal())
                .quantidade(qtd)
                .tipo(TipoMovimentacao.ENTRADA)
                .dataMovimentacao(LocalDateTime.now())
                .descricao("Produção de " + qtd + " × " + receita.getNome())
                .build();

        movRepo.save(movEntrada);

        // 5) Calcula o saldo DEPOIS da produção
        int saldoDepois = movRepo.calculaSaldo(produtoFinalId);

        // 6) Atualiza o agregador de Estoque para produto final e insumos
        atualizarSaldo(receita.getProdutoFinal());
        receita.getItens().forEach(item ->
                atualizarSaldo(item.getInsumo())
        );

        // 7) Retorna DTO com quantidade produzida e saldos antes/depois
        return ProducaoResponse.builder()
                .quantidadeProduzida(qtd)
                .saldoAntesFinal(saldoAntes)
                .saldoDepoisFinal(saldoDepois)
                .build();
    }

    /**
     * Recalcula o saldo agregado de um produto a partir das movimentações
     * e persiste no agregador Estoque.
     */
    private void atualizarSaldo(Produto produto) {
        int saldo = movRepo.calculaSaldo(produto.getId());

        Estoque estoque = estoqueRepo.findByProduto(produto)
                .orElseGet(() -> Estoque.builder()
                        .produto(produto)
                        .quantidade(0)
                        .dataAtualizacao(LocalDateTime.now())
                        .build()
                );

        estoque.setQuantidade(saldo);
        estoque.setDataAtualizacao(LocalDateTime.now());
        estoqueRepo.save(estoque);
    }

    /**
     * Lista todas as movimentações de um dado produto, do mais recente ao mais antigo.
     */
    @Transactional
    public List<MovimentacaoDTO> listarMovimentacoes(Long produtoId) {
        return movRepo
                .findByProdutoIdOrderByDataMovimentacaoDesc(produtoId)
                .stream()
                .map(movMapper::toDTO)
                .collect(Collectors.toList());
}
}
