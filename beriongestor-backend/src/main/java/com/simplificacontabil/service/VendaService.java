package com.simplificacontabil.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.simplificacontabil.dto.*;
import com.simplificacontabil.enums.ModeloNota;
import com.simplificacontabil.enums.TipoPessoa;
import com.simplificacontabil.integracoes.nuvemfiscal.builder.nfe.NfeBuilderService;
import com.simplificacontabil.integracoes.nuvemfiscal.client.nfe.NuvemFiscalNfeClient;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.EmitirNfeRequestDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.NotaFiscalResponseDTO;
import com.simplificacontabil.mapper.ClienteMapper;
import com.simplificacontabil.mapper.VendaMapper;
import com.simplificacontabil.model.*;
import com.simplificacontabil.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.simplificacontabil.util.MoedaUtil;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class VendaService {


    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private VendaMapper vendaMapper;

    @Autowired
    private ClienteMapper clienteMapper;


    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private final NuvemFiscalNfeClient nfeClient;          // seu serviço que chama a API

    @Autowired
    private final NfeBuilderService builder;    // monta o DTO de emissão

    public VendaService(NuvemFiscalNfeClient nfeClient, NfeBuilderService builder) {
        this.nfeClient = nfeClient;
        this.builder = builder;
    }


//    @Transactional
//    public Venda finalizarVendaComPagamento(VendaDTO vendaDTO, PagamentoDTO pagamentoDTO) {
//        Venda venda = criarVenda(vendaDTO, pagamentoDTO);
//        Cliente cliente = associarCliente(vendaDTO.getDocumentoCliente());
//        if (cliente != null) {
//            venda.setCliente(cliente);
//        }
//        List<ItemVenda> itens = criarItensVenda(vendaDTO.getItens(), venda);
//        venda.setItens(itens);
//        Pagamento pagamento = criarPagamento(venda, pagamentoDTO);
//        venda = vendaRepository.save(venda);
//        pagamentoRepository.save(pagamento);
//
//        if (vendaDTO.isEmitirNotaFiscal()) {
//            try {
//                // salva novamente as infos de NF-e
//                vendaRepository.save(venda);
//            } catch (RuntimeException e) {
//                throw new RuntimeException("Falha ao emitir nota fiscal. A venda foi salva mas a NF-e não foi gerada.", e);
//            }
//        }
//
//        return venda;
//    }


    private Venda criarVenda(VendaDTO dto, PagamentoDTO pagamentoDTO) {
        Venda venda = new Venda();
        venda.setDataVenda(dto.getDataVenda() != null ? dto.getDataVenda() : LocalDateTime.now());
        venda.setDocumentoCliente(dto.getDocumentoCliente());
        venda.setTotalVenda(dto.getPagamento().getTotalVenda());
        venda.setStatus(dto.getStatus());


        Pagamento pagamento = new Pagamento();
        pagamento.setTotalPagamento(dto.getPagamento().getTotalPagamento());
        pagamento.setTotalDesconto(dto.getPagamento().getTotalDesconto());
        pagamento.setFormaPagamento(dto.getPagamento().getFormaPagamento());
        pagamento.setNumeroParcelas(dto.getPagamento().getNumeroParcelas());

        // Seta o pagamento na venda
        venda.setPagamento(pagamento);

        return venda;
    }

    private int definirNumeroParcelas(PagamentoDTO pagamentoDTO) {
        return switch (pagamentoDTO.getFormaPagamento()) {
            case PIX ,CARTAO_DEBITO, DINHEIRO -> 1;
            case CARTAO_CREDITO, PARCELADO_LOJA, CARTAO -> pagamentoDTO.getNumeroParcelas();

        };
    }

    private Cliente associarCliente(String documentoCliente) {
        if (documentoCliente != null && !documentoCliente.isEmpty()) {
            return buscarClientePorDocumento(documentoCliente);
        }
        return null;
    }

    private List<ItemVenda> criarItensVenda(List<ItemVendaDTO> itemVendaDTOs, Venda venda) {
        if (itemVendaDTOs == null || itemVendaDTOs.isEmpty()) {

            System.out.println("ItemVendaDTOs recebidos: " + itemVendaDTOs);
            System.out.println("Tamanho da lista: " + (itemVendaDTOs != null ? itemVendaDTOs.size() : "null"));

            throw new IllegalArgumentException("A lista de itens de venda não pode ser nula ou vazia.");
        }

        itemVendaDTOs.forEach(item -> {
            System.out.println("Item recebido: " + item);
        });

        return itemVendaDTOs.stream().map(itemVendaDTO -> {
            if (itemVendaDTO.getProdutoId() == null) {
                throw new IllegalArgumentException("O ID do produto não pode ser nulo.");
            }

            // Busca o produto pelo ID
            Produto produto = produtoRepository.findById(itemVendaDTO.getProdutoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto com ID " + itemVendaDTO.getProdutoId() + " não foi encontrado."));

            // Cria o item de venda
            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setNomeProduto(itemVendaDTO.getNomeProduto());
            item.setQuantidade(itemVendaDTO.getQuantidade());
            item.setPrecoUnitario(itemVendaDTO.getPrecoUnitarioAsBigDecimal());
            item.setTotalItem(itemVendaDTO.getPrecoUnitarioAsBigDecimal().multiply(new BigDecimal(itemVendaDTO.getQuantidade())));
            item.setVenda(venda);

            return item;
        }).collect(Collectors.toList());
    }


    private Pagamento criarPagamento(Venda venda, PagamentoDTO pagamentoDTO) {
        Pagamento pagamento = new Pagamento();
        pagamento.setVenda(venda);
        pagamento.setFormaPagamento(pagamentoDTO.getFormaPagamento());
        pagamento.setValorPago(MoedaUtil.converterParaBigDecimal(pagamentoDTO.getValorPago()));
        pagamento.setValorRestante(pagamentoDTO.getValorRestante());
        pagamento.setDataPagamento(pagamentoDTO.getDataPagamento());
        pagamento.setStatus(pagamentoDTO.getStatus());
        pagamento.setNumeroParcelas(pagamentoDTO.getNumeroParcelas());
        return pagamento;
    }

    public List<Venda> buscarVendas(String dataInicio, String dataFim, String status) {
        Optional<LocalDateTime> inicio = (dataInicio != null && !dataInicio.isBlank())
                ? Optional.of(LocalDateTime.parse(dataInicio, DateTimeFormatter.ISO_DATE_TIME))
                : Optional.empty();

        Optional<LocalDateTime> fim = (dataFim != null && !dataFim.isBlank())
                ? Optional.of(LocalDateTime.parse(dataFim, DateTimeFormatter.ISO_DATE_TIME))
                : Optional.empty();

        boolean hasStatus = status != null && !status.isBlank(); // Verifica se o status foi informado

        if (inicio.isPresent() && fim.isPresent()) {
            return hasStatus
                    ? vendaRepository.findByDataVendaBetweenAndStatus(inicio.get(), fim.get(), status)
                    : vendaRepository.findByDataVendaBetween(inicio.get(), fim.get());
        }

        if (hasStatus) {
            return vendaRepository.findByStatus(status);
        }

        if (inicio.isPresent()) {
            return vendaRepository.findByDataVendaAfter(inicio.get());
        }

        if (fim.isPresent()) {
            return vendaRepository.findByDataVendaBefore(fim.get());
        }

        return vendaRepository.findAll();
    }

    public String gerarXML() throws JAXBException {
        // Criando o XML da NFE a partir dos dados do DTO
        NfeDTO nfeDTO = new NfeDTO();


        // Criando os produtos
        List<ProdutoDTO> produtosDTO = nfeDTO.getItens().stream().map(item -> {
            ProdutoDTO dto = new ProdutoDTO();
            dto.setDescricao(item.getNomeProduto()); // ou item.getProduto().getDescricao()
            dto.setPrecoUnitario(item.getPrecoUnitario());
            dto.setNcm(item.getProduto().getNcm()); // se você tiver isso
            dto.setQuantidade(item.getQuantidade());
            return dto;
        }).toList();
        List<Produto> produtos = produtosDTO.stream().map(produtoDTO -> {
            Produto produto = new Produto();
            produto.setDescricao(produtoDTO.getDescricao());
            produto.setPrecoUnitario(produtoDTO.getPrecoUnitario());
            produto.setNcm(produtoDTO.getNcm());
            produto.setQuantidade(produtoDTO.getQuantidade());
            return produto;
        }).toList();


        StringWriter writer = new StringWriter();

        // Retorna o XML gerado
        return writer.toString();
    }
    public String obterNomeCliente(Cliente cliente) {
        if (cliente == null || cliente.getTipoPessoa() == null) return "";

        return cliente.getTipoPessoa() == TipoPessoa.FISICA
                ? (cliente.getPessoaFisica() != null ? cliente.getPessoaFisica().getNome() : "")
                : (cliente.getPessoaJuridica() != null ? cliente.getPessoaJuridica().getRazaoSocial() : "");
    }
    public static String formatarDocumentoCliente(Cliente cliente) {
        if (cliente == null || cliente.getTipoPessoa() == null) return "";

        String documento = "";
        if (cliente.getTipoPessoa() == TipoPessoa.FISICA && cliente.getPessoaFisica() != null) {
            documento = cliente.getPessoaFisica().getCpf();
            return documento.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        } else if (cliente.getTipoPessoa() == TipoPessoa.JURIDICA && cliente.getPessoaJuridica() != null) {
            documento = cliente.getPessoaJuridica().getCnpj();
            return documento.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }

        return documento;
    }

    public static String formatarCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != 14) return cnpj;
        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }


    public byte[] gerarReciboPDF(Venda venda) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 50, 50, 40, 30);
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font contentFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);

            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 3});
            headerTable.setSpacingAfter(10f);

            try {
                Image logo = Image.getInstance(Objects.requireNonNull(getClass().getResource("/assets/logo/logo.png")));
                logo.setAbsolutePosition(document.left(), document.top() - 40);
                logo.scaleToFit(140, 140);
                document.add(logo);
            } catch (Exception e) {
                log.warn("Logo não encontrado", e);
            }

            PdfPCell infoCell = new PdfPCell();
            infoCell.setBorder(Rectangle.NO_BORDER);
            infoCell.setVerticalAlignment(Element.ALIGN_CENTER);

            String razaoSocialStr;
            String dadosEmitenteStr;

            if (venda.getEmitente() != null && venda.getEmitente().getEndereco() != null) {
                razaoSocialStr = venda.getEmitente().getRazaoSocial();
                dadosEmitenteStr = String.format("CNPJ: %s%nEndereço: %s %s%nBairro: %s%nMunicípio: %s",
                        formatarCnpj(venda.getEmitente().getCnpj()),
                        venda.getEmitente().getEndereco().getLogradouro(),
                        venda.getEmitente().getEndereco().getNumero(),
                        venda.getEmitente().getEndereco().getBairro(),
                        venda.getEmitente().getEndereco().getMunicipio());
            } else {
                razaoSocialStr = "Biazin Sistemas - LTDA";
                dadosEmitenteStr = "Emitido em ponto de venda local.";
            }

            Paragraph razaoSocial = new Paragraph(razaoSocialStr, new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD));
            razaoSocial.setSpacingAfter(5f);
            Paragraph dadosEmitente = new Paragraph(dadosEmitenteStr, new Font(Font.FontFamily.HELVETICA, 9));

            infoCell.addElement(razaoSocial);
            infoCell.addElement(dadosEmitente);
            headerTable.addCell(infoCell);

            document.add(headerTable);

            Paragraph title = new Paragraph("RECIBO DE VENDA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10f);
            document.add(title);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String formattedDate = venda.getDataVenda().format(formatter);

            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1, 3});
            infoTable.setSpacingAfter(15);

            infoTable.addCell(createInfoCell("Venda Nº:", true));
            infoTable.addCell(createInfoCell(String.valueOf(venda.getId()), false));
            infoTable.addCell(createInfoCell("Data:", true));
            infoTable.addCell(createInfoCell(formattedDate, false));

            if (venda.getCliente() != null) {
                infoTable.addCell(createInfoCell("Cliente:", true));
                infoTable.addCell(createInfoCell(obterNomeCliente(venda.getCliente()), false));
                infoTable.addCell(createInfoCell("Documento do Cliente:", true));
                infoTable.addCell(createInfoCell(formatarDocumentoCliente(venda.getCliente()), false));
            }

            document.add(infoTable);

            if (venda.getItens() != null && !venda.getItens().isEmpty()) {
                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{4, 2, 2, 2});
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);

                String[] headers = {"Produto", "Quantidade", "Preço Unitário", "Total"};
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header, tableHeaderFont));
                    cell.setBackgroundColor(BaseColor.DARK_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(6f);
                    table.addCell(cell);
                }

                for (ItemVenda item : venda.getItens()) {
                    table.addCell(createTableCell(item.getNomeProduto(), Element.ALIGN_LEFT));
                    table.addCell(createTableCell(String.valueOf(item.getQuantidade()), Element.ALIGN_CENTER));
                    table.addCell(createTableCell(String.format("R$ %.2f", item.getPrecoUnitario()), Element.ALIGN_RIGHT));
                    table.addCell(createTableCell(String.format("R$ %.2f", item.getTotalItem()), Element.ALIGN_RIGHT));
                }

                document.add(table);
            }

            Paragraph total = new Paragraph("TOTAL: R$ " + String.format("%.2f", venda.getTotalVenda()), sectionFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            total.setSpacingBefore(10f);
            document.add(total);

            LineSeparator separator = new LineSeparator();
            separator.setOffset(-5);
            document.add(new Chunk(separator));

            Paragraph footer = new Paragraph("Berion Gestor - Biazin Sistemas", contentFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(15f);
            document.add(footer);

            document.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            log.error("Erro ao gerar recibo PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao gerar recibo PDF", e);
        }
    }


    private PdfPCell createInfoCell(String text, boolean bold) {
        Font font = bold ? new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD) : new Font(Font.FontFamily.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        return cell;
    }

    private PdfPCell createTableCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, new Font(Font.FontFamily.HELVETICA, 10)));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5f);
        return cell;
    }


    private PdfPCell createCell(String content, int alignment, boolean isHeader) {
        Font font = isHeader ? new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD) : new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        if (isHeader) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }
        return cell;
    }

    public Cliente buscarClientePorDocumento(String documentoCliente) {
        if (documentoCliente.length() == 14) {
            // Considera como CNPJ
            return clienteRepository.findByPessoaJuridica_Cnpj(documentoCliente)
                    .stream().findFirst().orElse(null); // Ou você pode lançar uma exceção caso não encontre
        } else if (documentoCliente.length() == 11) {
            // Considera como CPF
            return clienteRepository.findByPessoaFisica_Telefone(documentoCliente)
                    .stream().findFirst().orElse(null); // Ou você pode lançar uma exceção caso não encontre
        }
        return null; // Caso o documento não tenha o tamanho esperado
    }

    // Método para gerar uma venda sem salvar no banco (somente para recibo)
    public Venda gerarVendaParaRecibo(VendaDTO vendaDTO, PagamentoDTO pagamentoDTO) {
        // Criação dos itens da venda
        List<ItemVenda> itens = vendaDTO.getItens().stream()
                .map(produtoDTO -> {
                    ItemVenda item = new ItemVenda();
                    item.setNomeProduto(produtoDTO.getNomeProduto());
                    item.setQuantidade(produtoDTO.getQuantidade());
//                    item.setPrecoUnitario(produtoDTO.getPrecoUnitario());
//                    item.setTotalItem(produtoDTO.getPrecoUnitario().multiply(BigDecimal.valueOf(produtoDTO.getQuantidade())));
                    return item;
                })
                .toList();

        // Criar a venda com os dados passados
        Venda venda = new Venda();
        venda.setDataVenda(vendaDTO.getDataVenda() != null ? vendaDTO.getDataVenda() : LocalDateTime.now());
        venda.setDocumentoCliente(vendaDTO.getDocumentoCliente());
        venda.setTotalVenda(vendaDTO.getPagamento().getTotalVenda());
        venda.setStatus(vendaDTO.getStatus());

        Pagamento pagamento = new Pagamento();
        pagamento.setTotalPagamento(vendaDTO.getPagamento().getTotalPagamento());
        pagamento.setTotalDesconto(vendaDTO.getPagamento().getTotalDesconto());
        pagamento.setFormaPagamento(vendaDTO.getPagamento().getFormaPagamento());
        pagamento.setNumeroParcelas(vendaDTO.getPagamento().getNumeroParcelas());

        // Seta o pagamento na venda
        venda.setPagamento(pagamento);

        return venda;
    }

    public List<VendaDTO> listarTodas() {
        List<Venda> vendas = vendaRepository.findAll();
        return vendas.stream()
                .map(vendaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalDoDia() {
        LocalDate hoje = LocalDate.now();
        return vendaRepository.sumByDataBetween(hoje.atStartOfDay(), hoje.plusDays(1).atStartOfDay());
    }

    public BigDecimal getTotalDaSemana() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.with(DayOfWeek.MONDAY);
        return vendaRepository.sumByDataBetween(inicioSemana.atStartOfDay(), hoje.plusDays(1).atStartOfDay());
    }

    public BigDecimal getTotalDoMes() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        return vendaRepository.sumByDataBetween(inicioMes.atStartOfDay(), hoje.plusDays(1).atStartOfDay());
    }

    public BigDecimal getTotalDoAno() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioAno = hoje.with(TemporalAdjusters.firstDayOfYear());
        return vendaRepository.sumByDataBetween(inicioAno.atStartOfDay(), hoje.plusDays(1).atStartOfDay());
    }

    public BigDecimal getTotalPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vendaRepository.sumByDataBetween(inicio, fim);
    }

    public List<Venda> getTodasAsVendasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return vendaRepository.findByDataVendaBetween(inicio, fim);
    }

    public Long getQuantidadeDoDia() {
        LocalDate hoje = LocalDate.now();
        return vendaRepository.countByDataBetween(
                hoje.atStartOfDay(),
                hoje.plusDays(1).atStartOfDay()
        );
    }

    public Map<DayOfWeek, BigDecimal> getTotaisDaSemanaPorDia() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.with(DayOfWeek.MONDAY);
        Map<DayOfWeek, BigDecimal> totais = new LinkedHashMap<>();

        for (int i = 0; i < 7; i++) {
            LocalDate dia = inicioSemana.plusDays(i);
            if (dia.isAfter(hoje)) break; // só até hoje

            LocalDateTime inicio = dia.atStartOfDay();
            LocalDateTime fim = dia.plusDays(1).atStartOfDay();

            BigDecimal total = vendaRepository.sumByDataBetween(inicio, fim);
            totais.put(dia.getDayOfWeek(), total != null ? total : BigDecimal.ZERO);
        }

        return totais;
    }

    public Map<String, BigDecimal> getTotaisPorSemanaDoAno() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioAno = hoje.with(TemporalAdjusters.firstDayOfYear());

        Map<String, BigDecimal> totaisPorSemana = new LinkedHashMap<>();
        LocalDate data = inicioAno;

        while (data.isBefore(hoje)) {
            LocalDate semanaInicio = data;
            LocalDate semanaFim = data.plusDays(6);
            if (semanaFim.isAfter(hoje)) semanaFim = hoje;

            BigDecimal total = vendaRepository.sumByDataBetween(
                    semanaInicio.atStartOfDay(), semanaFim.plusDays(1).atStartOfDay()
            );

            String nomeSemana = String.format("Semana %d (%s - %s)",
                    semanaInicio.get(WeekFields.ISO.weekOfYear()),
                    semanaInicio.format(DateTimeFormatter.ofPattern("dd/MM")),
                    semanaFim.format(DateTimeFormatter.ofPattern("dd/MM"))
            );

            totaisPorSemana.put(nomeSemana, total != null ? total : BigDecimal.ZERO);
            data = semanaFim.plusDays(1); // avança para a próxima semana
        }

        return totaisPorSemana;
    }

    public Map<String, BigDecimal> getTotaisPorMesDoAno() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioAno = hoje.with(TemporalAdjusters.firstDayOfYear());

        Map<String, BigDecimal> totaisPorMes = new LinkedHashMap<>();

        for (int mes = 1; mes <= hoje.getMonthValue(); mes++) {
            LocalDate inicioMes = LocalDate.of(hoje.getYear(), mes, 1);
            LocalDate fimMes = inicioMes.with(TemporalAdjusters.lastDayOfMonth());

            BigDecimal total = vendaRepository.sumByDataBetween(
                    inicioMes.atStartOfDay(), fimMes.plusDays(1).atStartOfDay()
            );

            String nomeMes = inicioMes.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
            totaisPorMes.put(nomeMes.substring(0, 1).toUpperCase() + nomeMes.substring(1), total != null ? total : BigDecimal.ZERO);
        }

        return totaisPorMes;
    }

    public Map<Integer, BigDecimal> getTotaisPorAnos() {
        Map<Integer, BigDecimal> totaisPorAno = new LinkedHashMap<>();
        LocalDate hoje = LocalDate.now();
        int anoAtual = hoje.getYear();

        // Calculando totais para os últimos 6 anos (2020-2025)
        for (int ano = 2020; ano <= 2025; ano++) {
            LocalDate inicioAno = LocalDate.of(ano, 1, 1);
            LocalDate fimAno = LocalDate.of(ano, 12, 31);

            // Se for ano futuro, retorna zero
            if (ano > anoAtual) {
                totaisPorAno.put(ano, BigDecimal.ZERO);
                continue;
            }

            BigDecimal total = vendaRepository.sumByDataBetween(
                    inicioAno.atStartOfDay(),
                    fimAno.plusDays(1).atStartOfDay()
            );

            totaisPorAno.put(ano, total != null ? total : BigDecimal.ZERO);
        }

        return totaisPorAno;
    }


    private Venda criarVendaRequest(VendaRequestDTO dto) {
        Venda venda = new Venda();
        venda.setDataVenda(dto.getDataVenda() != null ? dto.getDataVenda() : LocalDateTime.now());
        venda.setDocumentoCliente(dto.getDocumentoCliente());
        venda.setTotalVenda(dto.getPagamento().getTotalVenda());
        venda.setStatus(dto.getStatus());

        Pagamento pagamento = new Pagamento();
        pagamento.setTotalPagamento(dto.getPagamento().getTotalPagamento());
        pagamento.setTotalDesconto(dto.getPagamento().getTotalDesconto());
        pagamento.setFormaPagamento(dto.getPagamento().getFormaPagamento());
        pagamento.setNumeroParcelas(dto.getPagamento().getNumeroParcelas());

        // Seta o pagamento na venda
        venda.setPagamento(pagamento);

        // Frete (verifica se veio algo no DTO)
        // Versão com builder (mais limpa)
        if (dto.getFrete() != null) {
            NfeFrete frete = NfeFrete.builder()
                    .valorFrete(dto.getFrete().getValorFrete())
                    .valorSeguro(dto.getFrete().getValorSeguro())
                    .valorDesconto(dto.getFrete().getValorDesconto())
                    .outrasDespesas(dto.getFrete().getOutrasDespesas())
                    .transportadora(dto.getFrete().getTransportadora() != null ?
                            Transporta.builder()
                                    .CNPJ(dto.getFrete().getTransportadora().getCNPJ())
                                    .CPF(dto.getFrete().getTransportadora().getCPF())
                                    .xNome(dto.getFrete().getTransportadora().getXNome())
                                    .xEnder(dto.getFrete().getTransportadora().getXEnder())
                                    .xMun(dto.getFrete().getTransportadora().getXMun())
                                    .UF(dto.getFrete().getTransportadora().getUF())
                                    .IE(dto.getFrete().getTransportadora().getIE())
                                    .build() : null)
                    .volumes(dto.getFrete().getVolumes() != null ?
                            dto.getFrete().getVolumes().stream().map(v ->
                                    Volume.builder()
                                            .qVol(v.getQVol())
                                            .esp(v.getEsp())
                                            .marca(v.getMarca())
                                            .nVol(v.getNVol())
                                            .pesoL(v.getPesoL())
                                            .pesoB(v.getPesoB())
                                            .build()
                            ).collect(Collectors.toList()) : null)
                    .build();

            venda.setFrete(frete);
        }


        return venda;
    }

    private Pagamento criarPagamentoRequest(Venda venda, PagamentoDTO pagamentoDTO) throws IllegalArgumentException {
        if (pagamentoDTO == null) {
            throw new IllegalArgumentException("Informações de pagamento são obrigatórias.");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setVenda(venda);
        pagamento.setFormaPagamento(pagamentoDTO.getFormaPagamento());
        pagamento.setValorPago(MoedaUtil.converterParaBigDecimal(pagamentoDTO.getValorPago()));
        pagamento.setValorRestante(pagamentoDTO.getValorRestante());
        pagamento.setDataPagamento(pagamentoDTO.getDataPagamento());
        pagamento.setStatus(pagamentoDTO.getStatus());
        pagamento.setNumeroParcelas(definirNumeroParcelas(pagamentoDTO));
        return pagamento;
    }

    public Cliente buscarOuCriarCliente(Cliente clienteRequest) {
        if (clienteRequest.getTipoPessoa() == TipoPessoa.FISICA && clienteRequest.getPessoaFisica() != null) {
            String cpf = clienteRequest.getPessoaFisica().getCpf();
            return clienteRepository.findByDocumento(cpf)
                    .orElseGet(() -> clienteRepository.save(clienteRequest));

        } else if (clienteRequest.getTipoPessoa() == TipoPessoa.JURIDICA && clienteRequest.getPessoaJuridica() != null) {
            String cnpj = clienteRequest.getPessoaJuridica().getCnpj();
            return clienteRepository.findByDocumento(cnpj)
                    .orElseGet(() -> clienteRepository.save(clienteRequest));

        } else {
            throw new RuntimeException("Tipo de pessoa ou dados inválidos.");
        }
    }

    @Transactional
    public Venda finalizarVendaComPagamentoRequest(VendaRequestDTO dto) {
        Venda venda = criarVendaRequest(dto);
        venda.setEmitirNotaFiscal(dto.isEmitirNotaFiscal());
        venda.setVendaAnonima(dto.isVendaAnonima());
        venda.setModFrete(dto.getModFrete());

        Cliente cliente = null;

        // Cenário: Emissão de Nota Fiscal
        if (dto.isEmitirNotaFiscal()) {
            if (dto.getEmitenteId() == null || dto.getModelo() == null) {
                throw new IllegalArgumentException("Emitente e modelo fiscal são obrigatórios para emissão da NF.");
            }

            if (dto.getCliente() == null) {
                throw new RuntimeException("Cliente obrigatório para emissão de nota fiscal.");
            }

            String doc;
            if (dto.getCliente().getTipoPessoa() == TipoPessoa.FISICA && dto.getCliente().getPessoaFisica() != null) {
                doc = dto.getCliente().getPessoaFisica().getCpf();
            } else if (dto.getCliente().getTipoPessoa() == TipoPessoa.JURIDICA && dto.getCliente().getPessoaJuridica() != null) {
                doc = dto.getCliente().getPessoaJuridica().getCnpj();
            } else {
                throw new RuntimeException("Dados do cliente incompletos ou inválidos.");
            }

            cliente = clienteRepository.findByDocumento(doc)
                    .orElseGet(() -> clienteRepository.save(clienteMapper.toEntity(dto.getCliente())));

            if(!dto.isVendaAnonima()){
                Empresa emitente = empresaRepository.findById(dto.getEmitenteId())
                        .orElseThrow(() -> new IllegalArgumentException("Emitente não encontrado com ID: " + dto.getEmitenteId()));
                venda.setEmitente(emitente);
            }

            venda.setModelo(dto.getModelo());
        }
        // Cenário: Não emitir nota e não é anônima → tenta associar cliente se informado
        else if (!dto.isVendaAnonima()) {
            if (dto.getDocumentoCliente() != null && !dto.getDocumentoCliente().isBlank()) {
                cliente = associarCliente(dto.getDocumentoCliente());
                cliente = buscarOuSalvarCliente(cliente, dto.getDocumentoCliente());
            }
        }

        venda.setCliente(cliente);

        // Itens - Garante que a lista é mutável
        List<ItemVenda> itens = new ArrayList<>(criarItensVenda(dto.getItens(), venda));
        venda.getItens().clear();
        venda.getItens().addAll(itens);

        // Pagamento
        Pagamento pagamento = criarPagamentoRequest(venda, dto.getPagamento());

        if (cliente != null) {
            buscarOuCriarCliente(cliente);
        }

        // Salva primeiro a venda para obter o ID
        venda = vendaRepository.save(venda);

        // Associa o pagamento à venda salva
        pagamento.setVenda(venda);
        pagamentoRepository.save(pagamento);

        // Emissão de NF-e se solicitado
        if (dto.isEmitirNotaFiscal()) {
            try {
                // Se precisar adicionar mais dados à venda antes de salvar novamente
                vendaRepository.save(venda);
            } catch (RuntimeException e) {
                log.error("Erro ao emitir NF para Venda ID {}: {}", venda.getId(), e.getMessage(), e);
                throw new RuntimeException("Falha ao emitir nota fiscal. A venda foi salva mas a NF-e não foi gerada.", e);
            }
        }

        return venda;
    }

    private Cliente buscarOuSalvarCliente(Cliente cliente, String documento) {
        return clienteRepository.findByDocumento(documento)
                .orElseGet(() -> clienteRepository.save(cliente));
    }

    public List<AgrupamentoPeriodoDasVendaDTO> listarVendasPorDias() {
        List<AgrupamentoPeriodoDasVendaDTO> resultado = new ArrayList<>();
        LocalDate hoje = LocalDate.now();

        Map<String, BigDecimal> totais = new HashMap<>();
        List<Venda> vendas = vendaRepository.findComPagamentoByDataVendaBetween(
                hoje.atStartOfDay(), hoje.plusDays(1).atStartOfDay());

        for (Venda v : vendas) {
            String forma = v.getPagamento().getFormaPagamento().name();
            totais.put(forma, totais.getOrDefault(forma, BigDecimal.ZERO).add(v.getTotalVenda()));
        }

        resultado.add(new AgrupamentoPeriodoDasVendaDTO("Hoje", totais));
        return resultado;
    }

    public List<AgrupamentoPeriodoDasVendaDTO> listarVendasPorSemana() {
        List<AgrupamentoPeriodoDasVendaDTO> resultado = new ArrayList<>();
        LocalDate hoje = LocalDate.now();
        LocalDate data = hoje.with(TemporalAdjusters.firstDayOfYear());

        while (data.isBefore(hoje)) {
            LocalDate inicio = data;
            LocalDate fim = data.plusDays(6);
            if (fim.isAfter(hoje)) fim = hoje;

            List<Venda> vendas = vendaRepository.findComPagamentoByDataVendaBetween(
                    inicio.atStartOfDay(), fim.plusDays(1).atStartOfDay());

            Map<String, BigDecimal> totais = new HashMap<>();
            for (Venda v : vendas) {
                String forma = v.getPagamento().getFormaPagamento().name();
                totais.put(forma, totais.getOrDefault(forma, BigDecimal.ZERO).add(v.getTotalVenda()));
            }

            String label = String.format("Semana %d (%s - %s)",
                    inicio.get(WeekFields.ISO.weekOfYear()),
                    inicio.format(DateTimeFormatter.ofPattern("dd/MM")),
                    fim.format(DateTimeFormatter.ofPattern("dd/MM")));

            resultado.add(new AgrupamentoPeriodoDasVendaDTO(label, totais));
            data = fim.plusDays(1);
        }

        return resultado;
    }

    public List<AgrupamentoPeriodoDasVendaDTO> listarVendasPorMeses() {
        LocalDate hoje = LocalDate.now();
        List<AgrupamentoPeriodoDasVendaDTO> resultado = new ArrayList<>();

        for (int mes = 1; mes <= hoje.getMonthValue(); mes++) {
            LocalDate inicioMes = LocalDate.of(hoje.getYear(), mes, 1);
            LocalDate fimMes = inicioMes.with(TemporalAdjusters.lastDayOfMonth());

            List<Venda> vendasMes = vendaRepository.findComPagamentoByDataVendaBetween(
                    inicioMes.atStartOfDay(), fimMes.plusDays(1).atStartOfDay()
            );

            Map<String, BigDecimal> agrupadoPorForma = new HashMap<>();

            for (Venda v : vendasMes) {
                String forma = v.getPagamento().getFormaPagamento().name();
                agrupadoPorForma.put(
                        forma,
                        agrupadoPorForma.getOrDefault(forma, BigDecimal.ZERO).add(v.getTotalVenda())
                );
            }

            String nomeMes = inicioMes.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
            String label = nomeMes.substring(0, 1).toUpperCase() + nomeMes.substring(1);

            AgrupamentoPeriodoDasVendaDTO dto = new AgrupamentoPeriodoDasVendaDTO();
            dto.setLabel(label);
            dto.setTotais(agrupadoPorForma);

            resultado.add(dto);
        }

        return resultado;
    }

    public List<AgrupamentoPeriodoDasVendaDTO> listarVendasPorAnos() {
        List<AgrupamentoPeriodoDasVendaDTO> resultado = new ArrayList<>();
        int anoAtual = LocalDate.now().getYear();

        for (int ano = 2020; ano <= anoAtual; ano++) {
            LocalDate inicio = LocalDate.of(ano, 1, 1);
            LocalDate fim = LocalDate.of(ano, 12, 31);

            List<Venda> vendas = vendaRepository.findComPagamentoByDataVendaBetween(
                    inicio.atStartOfDay(), fim.plusDays(1).atStartOfDay());

            Map<String, BigDecimal> totais = new HashMap<>();
            for (Venda v : vendas) {
                String forma = v.getPagamento().getFormaPagamento().name();
                totais.put(forma, totais.getOrDefault(forma, BigDecimal.ZERO).add(v.getTotalVenda()));
            }

            resultado.add(new AgrupamentoPeriodoDasVendaDTO(String.valueOf(ano), totais));
        }

        return resultado;
    }
    public List<Venda> buscarVendasPorClienteOuDocumento(String busca) {
        if (busca == null || busca.isBlank()) {
            return Collections.emptyList();
        }
        return vendaRepository.buscarPorClienteOuDocumento(busca);
    }
    @Transactional
    public NotaFiscalResponseDTO emitirNotaFiscal(EmitirNotaRequestDTO dto) {
        // 1) Carrega venda e emitente
        Venda venda = vendaRepository.findById(dto.getVendaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Venda não encontrada"));
        Empresa emitente = empresaRepository.findById(dto.getEmitenteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Emitente não encontrado"));

        // 2) Associa o emitente à venda
        venda.setEmitente(emitente);


        // 3) Monta o payload e chama a API da Nuvem Fiscal
        EmitirNfeRequestDTO request = builder.montarNfeCompleta(venda);
        String jsonResposta = nfeClient.emitirNfe(request);

        // 4) Desserializa o retorno
        NotaFiscalResponseDTO resp = builder.montarResposta(jsonResposta);


        // 5) Preenche e persiste na venda
        venda.setChaveAcessoNfe(      resp.getChaveAcesso() );
        venda.setNumeroProtocoloNfe(  resp.getIdNota());
        venda.setStatus(              resp.getStatus() );
        venda.setUltimaAtualizacao(LocalDateTime.now());
        venda.setItens(venda.getItens());

        vendaRepository.save(venda);

        return resp;
    }

}


