package com.simplificacontabil.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.simplificacontabil.dto.ItemMesaDTO;
import com.simplificacontabil.dto.PedidoMesaDTO;
import com.simplificacontabil.enums.StatusPedido;
import com.simplificacontabil.model.ItemPedido;
import com.simplificacontabil.model.Mesa;
import com.simplificacontabil.model.Pedido;
import com.simplificacontabil.model.Produto;
import com.simplificacontabil.repository.MesaRepository;
import com.simplificacontabil.repository.PedidoRepository;
import com.simplificacontabil.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MesaService {

    private final MesaRepository mesaRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    public Mesa criarOuAbrirMesa(Integer numeroMesa) {
        return mesaRepository.findByNumero(numeroMesa)
                .orElseGet(() -> {
                    Mesa nova = new Mesa();
                    nova.setNumero(numeroMesa);
                    nova.setAberta(true);
                    return mesaRepository.save(nova);
                });
    }

    public Pedido adicionarPedidoNaMesa(PedidoMesaDTO dto) {
        Mesa mesa = criarOuAbrirMesa(dto.getNumeroMesa());

        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
        pedido.setHoraPedido(LocalDateTime.now());
        pedido.setPago(false);
        pedido.setStatus(StatusPedido.EM_PREPARO);

        List<ItemPedido> itens = dto.getItens().stream().map(itemDTO -> {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDTO.getProdutoId()));

            BigDecimal total = produto.getPrecoUnitario().multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));

            return ItemPedido.builder()
                    .pedido(pedido)
                    .produtoId(produto.getId())
                    .quantidade(itemDTO.getQuantidade())
                    .precoUnitario(produto.getPrecoUnitario())
                    .total(total)
                    .build();
        }).toList();

        pedido.setItens(itens);

        return pedidoRepository.save(pedido);
    }

    public BigDecimal fecharMesa(Integer numeroMesa) {
        Mesa mesa = mesaRepository.findByNumero(numeroMesa)
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada"));

        List<Pedido> pedidos = mesa.getPedidos().stream()
                .filter(p -> !p.isPago())
                .toList();

        BigDecimal total = pedidos.stream()
                .flatMap(p -> p.getItens().stream())
                .map(ItemPedido::getTotal) // ⚠️ usa o nome correto do campo!
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedidos.forEach(p -> p.setPago(true));
        mesa.setAberta(false);

        pedidoRepository.saveAll(pedidos);
        mesaRepository.save(mesa);

        byte[] pdf = gerarCupomFiscal(mesa.getNumero()); // aqui o cupom é gerado


        System.out.println("✅ Cupom fiscal gerado com " + pdf.length + " bytes");

        return total;
    }


    public List<Mesa> listarMesasAbertas() {
        return mesaRepository.findByAbertaTrue();
    }

    public byte[] gerarCupomFiscal(Integer numeroMesa) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Mesa mesa = mesaRepository.findByNumero(numeroMesa)
                    .orElseThrow(() -> new RuntimeException("Mesa não encontrada"));

            Document document = new Document(PageSize.A4, 50, 50, 30, 30);
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font contentFont = new Font(Font.FontFamily.HELVETICA, 10);

            Paragraph title = new Paragraph("PIZZARIA DO ZÉ", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("Mesa: " + mesa.getNumero(), contentFont));
            document.add(new Paragraph("Data: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), contentFont));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 1, 2, 2});
            table.addCell(createCell("Produto", PdfPCell.ALIGN_CENTER, true));
            table.addCell(createCell("Qtd", PdfPCell.ALIGN_CENTER, true));
            table.addCell(createCell("V. Unit", PdfPCell.ALIGN_CENTER, true));
            table.addCell(createCell("Total", PdfPCell.ALIGN_CENTER, true));

            List<Pedido> pedidos = mesa.getPedidos().stream().filter(p -> !p.isPago()).toList();

            BigDecimal totalGeral = BigDecimal.ZERO;
            for (Pedido pedido : pedidos) {
                for (ItemPedido item : pedido.getItens()) {
                    table.addCell(createCell(item.getProdutoId().toString(), PdfPCell.ALIGN_LEFT, false));
                    table.addCell(createCell(String.valueOf(item.getQuantidade()), PdfPCell.ALIGN_CENTER, false));
                    table.addCell(createCell("R$ " + item.getPrecoUnitario(), PdfPCell.ALIGN_RIGHT, false));
                    table.addCell(createCell("R$ " + item.getTotal(), PdfPCell.ALIGN_RIGHT, false));
                    totalGeral = totalGeral.add(item.getTotal());
                }
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            Paragraph total = new Paragraph("TOTAL: R$ " + totalGeral.setScale(2, RoundingMode.HALF_UP).toPlainString(), contentFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.add(Chunk.NEWLINE);
            Paragraph obrigado = new Paragraph("Obrigado pela preferência!", contentFont);
            obrigado.setAlignment(Element.ALIGN_CENTER);
            document.add(obrigado);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    private PdfPCell createCell(String content, int alignment, boolean header) {
        Font font = header ? new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD) : new Font(Font.FontFamily.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        if (header) cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        return cell;
    } // fim do método auxiliar

    public BigDecimal calcularTotalMesa(Integer numeroMesa) {
        Mesa mesa = mesaRepository.findByNumero(numeroMesa)
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada"));

        return mesa.getPedidos().stream()
                .filter(p -> !p.isPago())
                .flatMap(p -> p.getItens().stream())
                .map(ItemPedido::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public byte[] finalizarPagamentoEMesa(Integer numeroMesa) {
        Mesa mesa = mesaRepository.findByNumero(numeroMesa)
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada"));

        List<Pedido> pedidosNaoPagos = mesa.getPedidos().stream()
                .filter(p -> !p.isPago())
                .toList();

        // Marca os pedidos como pagos
        pedidosNaoPagos.forEach(p -> p.setPago(true));

        // Fecha a mesa
        mesa.setAberta(true);
        limparItensDaMesa(numeroMesa);

        // Persiste tudo
        pedidoRepository.saveAll(pedidosNaoPagos);
        mesaRepository.save(mesa);

        // Gera e retorna o cupom
        return gerarCupomFiscal(mesa.getNumero());
    }

    public List<ItemMesaDTO> listarItensDaMesa(Integer numeroMesa) {
        Mesa mesa = mesaRepository.findByNumero(numeroMesa)
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada"));

        return mesa.getPedidos().stream()
                .filter(p -> !p.isPago())
                .flatMap(p -> p.getItens().stream())
                .map(item -> ItemMesaDTO.builder()
                        .produtoID(item.getProdutoId())
                        .idItemproduto(item.getId())
                        .nomeProduto(String.valueOf(item.getProdutoId()))
                        .quantidade(item.getQuantidade())
                        .precoUnitario(item.getPrecoUnitario())
                        .totalItem(item.getTotal())
                        .build())
                .collect(Collectors.toList());
    }
    @Transactional
    public void limparItensDaMesa(Integer numeroMesa) {
        Mesa mesa = mesaRepository.findByNumero(numeroMesa)
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada"));

        List<Pedido> todosOsPedidos = mesa.getPedidos();

        pedidoRepository.deleteAll(todosOsPedidos);
    }
}