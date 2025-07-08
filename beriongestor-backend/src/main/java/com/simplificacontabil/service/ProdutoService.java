package com.simplificacontabil.service;

import com.itextpdf.text.Document;
import com.simplificacontabil.dto.ProdutoDTO;
import com.simplificacontabil.mapper.ProdutoMapper;
import com.simplificacontabil.model.Estoque;
import com.simplificacontabil.model.Produto;
import com.simplificacontabil.repository.EstoqueRepository;
import com.simplificacontabil.repository.ItemVendaRepository;
import com.simplificacontabil.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class ProdutoService {

    @Autowired
    private final ProdutoRepository produtoRepository;

    @Autowired
    private ItemVendaRepository itemVendaRepository;

    @Autowired
    private final EstoqueRepository estoqueRepository;

    @Autowired
    private final ProdutoMapper produtoMapper;

    @Transactional
    public ProdutoDTO salvar(ProdutoDTO dto) {
        Produto produto = converterParaEntidade(dto);
        try {
            Produto salvo = produtoRepository.save(produto);
            atualizarEstoque(salvo, dto.getQuantidade());
            return produtoMapper.toDTO(salvo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar produto: " + e.getMessage());
        }
    }

    private Produto converterParaEntidade(ProdutoDTO dto) {
        Produto produto = produtoMapper.toEntity(dto);
        produto.setDataDeVencimento(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toLocalDateTime());
        return produto;
    }
    private void atualizarEstoque(Produto produto, int quantidadeAdicional) {
        Estoque estoqueExistente = estoqueRepository.findByProdutoId(produto.getId())
                .orElseGet(() -> criarNovoEstoque(produto));

        int novaQuantidade = estoqueExistente.getQuantidade() + quantidadeAdicional;

        if (novaQuantidade < 0) {
            throw new IllegalArgumentException("Quantidade em estoque não pode ser negativa");
        }

        estoqueExistente.setQuantidade(novaQuantidade);
        estoqueExistente.setDataAtualizacao(LocalDateTime.now());
        estoqueRepository.save(estoqueExistente);
    }

    private Estoque criarNovoEstoque(Produto produto) {
        Estoque novoEstoque = new Estoque();
        novoEstoque.setProduto(produto);
        novoEstoque.setQuantidade(0);
        return novoEstoque;
    }

    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(produtoMapper::toDTO)
                .collect(Collectors.toList());
    }
    public List<ProdutoDTO> buscarPorNome(String nome) {
        List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase(nome);
        if (produtos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
        return produtos.stream()
                .map(produtoMapper::toDTO)
                .collect(Collectors.toList());
    }


    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return produtoMapper.toDTO(produto);
    }

    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }

    public List<Produto> listarProdutosComEstoqueBaixo() {
        return produtoRepository.findByQuantidadeLessThanEqual(5);
    }

    public List<Map<String, Object>> listarMaisVendidos() {
        return itemVendaRepository.findMaisVendidos();
    }

    public byte[] exportarProdutosParaExcel() {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Produtos");
            Row header = (Row) sheet.createRow(0);
            header.createCell(0).setCellValue("Nome");
            header.createCell(1).setCellValue("Preço");
            header.createCell(2).setCellValue("Estoque");

            List<Produto> produtos = produtoRepository.findAll();
            int rowIdx = 1;
            for (Produto p : produtos) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(p.getNome());
                row.createCell(1).setCellValue(p.getPrecoUnitario().doubleValue());
            }
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar Excel", e);
        }
    }

    public byte[] exportarProdutosParaPdf() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document();
            PdfWriter.getInstance(doc, out);
            doc.open();

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.addCell(new PdfPCell(new Phrase("Nome")));
            table.addCell(new PdfPCell(new Phrase("Preço")));
            table.addCell(new PdfPCell(new Phrase("Estoque")));

            for (Produto p : produtoRepository.findAll()) {
                table.addCell(p.getNome());
                table.addCell(String.format("R$ %.2f", p.getPrecoUnitario()));
            }

            doc.add(table);
            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }


}
