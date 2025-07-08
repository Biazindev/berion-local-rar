package com.simplificacontabil.service;

import com.simplificacontabil.dto.EstoqueDTO;
import com.simplificacontabil.dto.HistoricoMovimentacaoDTO;
import com.simplificacontabil.dto.ProdutoDTO;
import com.simplificacontabil.enums.TipoMovimentacao;
import com.simplificacontabil.mapper.EstoqueMapper;
import com.simplificacontabil.model.Estoque;
import com.simplificacontabil.model.Produto;
import com.simplificacontabil.repository.EstoqueRepository;
import com.simplificacontabil.repository.ProdutoRepository;
import com.simplificacontabil.helper.HistoricoHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final HistoricoHelper historicoHelper;
    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueMapper mapper;



    public List<EstoqueDTO> listarTodos() {
        return estoqueRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public EstoqueDTO buscarPorId(Long id) {
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));
        return mapper.toDTO(estoque);
    }

    public void deletar(Long id) {
        estoqueRepository.deleteById(id);
    }

    public EstoqueDTO salvar(EstoqueDTO dto) {
        Produto produto = produtoRepository.findById(dto.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        Estoque estoque = mapper.toEntity(dto);
        estoque.setDataAtualizacao(LocalDateTime.now());

        Estoque salvo = estoqueRepository.save(estoque);

        // ✅ registrar historico corretamente (com a instância injetada)
        historicoHelper.registrar(
                TipoMovimentacao.ESTOQUE,
                "Estoque atualizado para o produto " + produto.getNome()
                        + ": " + dto.getQuantidade() + " unidades.",
                "Produto",
                produto.getId(),
                "admin"
        );

        return mapper.toDTO(salvo);
    }

    public Long getTotalProdutosEmEstoque() {
        return estoqueRepository.countByQuantidadeGreaterThan(0);
    }
}
