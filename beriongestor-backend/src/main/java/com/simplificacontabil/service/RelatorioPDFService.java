package com.simplificacontabil.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.simplificacontabil.dto.ContaReceberDTO;
import com.simplificacontabil.dto.HistoricoMovimentacaoDTO;
import com.simplificacontabil.dto.OrdemServicoDTO;
import com.simplificacontabil.enums.TipoPessoa;
import com.simplificacontabil.model.Cliente;
import com.simplificacontabil.model.Emitente;
import com.simplificacontabil.model.Empresa;
import com.simplificacontabil.repository.ClienteRepository;
import com.simplificacontabil.repository.EmitenteRepository;
import com.simplificacontabil.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RelatorioPDFService {



    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;


    public byte[] gerarRelatorioHistorico(List<HistoricoMovimentacaoDTO> historicos) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Título
            Font fonteTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph titulo = new Paragraph("Relatório de Histórico de Movimentações", fonteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" "));

            Image logo = Image.getInstance(Objects.requireNonNull(getClass().getResource("/assets/logo/logo.png")));
            logo.scaleToFit(120, 60);
            logo.setAlignment(Element.ALIGN_LEFT);
            document.add(logo);

            Paragraph empresa = new Paragraph("Simplifica Contábil - Sistema de Gestão", new Font(Font.FontFamily.HELVETICA, 12));
            empresa.setAlignment(Element.ALIGN_RIGHT);
            document.add(empresa);

            document.add(new Paragraph(" "));


            // Tabela
            PdfPTable tabela = new PdfPTable(5);
            tabela.setWidthPercentage(100);
            tabela.addCell("Tipo");
            tabela.addCell("Entidade");
            tabela.addCell("ID");
            tabela.addCell("Data");
            tabela.addCell("Usuário");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (HistoricoMovimentacaoDTO h : historicos) {
                tabela.addCell(h.getTipoMovimentacao().getDescricao());
                tabela.addCell(h.getEntidade());
                tabela.addCell(String.valueOf(h.getEntidadeId()));
                tabela.addCell(h.getDataHora().format(formatter));
                tabela.addCell(h.getUsuarioResponsavel());
            }

            document.add(tabela);
        } catch (Exception e) {
            log.error("Erro ao gerar histórico: " + e);
         } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    public byte[] gerarComprovanteOrdemServico(OrdemServicoDTO os) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);


        try {
            PdfWriter.getInstance(document, baos);
            document.open();


            Empresa em = empresaRepository.findById(1L).orElseThrow(() -> new RuntimeException("Emitente não encontrado"));




            // Buscar cliente
            String nomeCliente = "-";
            if (os.getClienteId() != null) {
                Optional<Cliente> clienteOpt = clienteRepository.findById(os.getClienteId());
                if (clienteOpt.isPresent()) {
                    Cliente cliente = clienteOpt.get();
                    if (cliente.getTipoPessoa() == TipoPessoa.FISICA && cliente.getPessoaFisica() != null) {
                        nomeCliente = cliente.getPessoaFisica().getNome();
                    } else if (cliente.getTipoPessoa() == TipoPessoa.JURIDICA && cliente.getPessoaJuridica() != null) {
                        nomeCliente = cliente.getPessoaJuridica().getRazaoSocial();
                    }
                }
            }


            // Cabeçalho com logo e informações da empresa
            PdfPTable header = new PdfPTable(2);
            header.setWidthPercentage(100);

            // Logo
            Image logo = Image.getInstance(Objects.requireNonNull(getClass().getResource("/assets/logo/logo.png")));
            logo.scaleToFit(120, 60);
            PdfPCell logoCell = new PdfPCell(Image.getInstance(logo));
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            header.addCell(logoCell);

            // Informações da empresa
            PdfPCell empresaCell = new PdfPCell();
            empresaCell.setBorder(Rectangle.NO_BORDER);
            empresaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            empresaCell.addElement(new Paragraph(em.getNomeFantasia(), new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD)));
            empresaCell.addElement(new Paragraph("CNPJ: ", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
            empresaCell.addElement(new Paragraph("CIDADE: " + em.getEndereco().getMunicipio(), new Font(Font.FontFamily.HELVETICA, 10)));
            empresaCell.addElement(new Paragraph("FONE :" + em.getFone().replaceAll("(\\d{2})(\\d{4,5})(\\d{4})", "($1) $2-$3"), new Font(Font.FontFamily.HELVETICA, 10)));
            header.addCell(empresaCell);
            document.add(header);

            document.add(new Paragraph(" "));

            // Título do documento
            Font fonteTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph titulo = new Paragraph("ORDEM DE SERVIÇO Nº " + os.getId(), fonteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            // Informações principais
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            Font fonteLabel = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font fonteConteudo = new Font(Font.FontFamily.HELVETICA, 10);

            addLinhaTabelaOS(table, "Cliente:", nomeCliente, fonteLabel, fonteConteudo);
            addLinhaTabelaOS(table, "Data de Abertura:", formatar(os.getDataAbertura()), fonteLabel, fonteConteudo);
            addLinhaTabelaOS(table, "Data de Conclusão:", os.getDataConclusao() != null ? formatar(os.getDataConclusao()) : "-", fonteLabel, fonteConteudo);
            addLinhaTabelaOS(table, "Status:", os.getStatus().getDescricao(), fonteLabel, fonteConteudo);
            addLinhaTabelaOS(table, "Valor Total:", "R$ " + os.getValor(), fonteLabel, fonteConteudo);

            document.add(table);

            // Descrição do serviço
            document.add(new Paragraph(" "));
            Paragraph descTitulo = new Paragraph("DESCRIÇÃO DO SERVIÇO", new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD));
            document.add(new Paragraph(" "));
            document.add(descTitulo);
            document.add(new Paragraph(" "));

            PdfPTable tableDesc = new PdfPTable(1);
            tableDesc.setWidthPercentage(100);
            PdfPCell cellDesc = new PdfPCell(new Paragraph(os.getDescricao(), fonteConteudo));
            cellDesc.setPadding(5);
            tableDesc.addCell(cellDesc);
            document.add(tableDesc);

            // Assinaturas
            document.add(new Paragraph("\n\n"));
            PdfPTable assinaturas = new PdfPTable(2);
            assinaturas.setWidthPercentage(100);
            assinaturas.setSpacingBefore(40);

            PdfPCell cell1 = new PdfPCell(new Paragraph("_______________________________\nAssinatura do Cliente", fonteConteudo));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBorder(Rectangle.NO_BORDER);

            PdfPCell cell2 = new PdfPCell(new Paragraph("_______________________________\nAssinatura do Responsável", fonteConteudo));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.NO_BORDER);

            assinaturas.addCell(cell1);
            assinaturas.addCell(cell2);

            document.add(assinaturas);

        } catch (Exception e) {
            log.error("Erro ao gerar relatório PDF", e);
        } finally {
            document.close();
        }

        return baos.toByteArray();
    }

    private void addLinhaTabelaOS(PdfPTable table, String label, String valor, Font fonteLabel, Font fonteConteudo) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label, fonteLabel));
        cell1.setBorder(Rectangle.BOTTOM);
        cell1.setPadding(5);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Phrase(valor, fonteConteudo));
        cell2.setBorder(Rectangle.BOTTOM);
        cell2.setPadding(5);
        table.addCell(cell2);
    }

    private String formatar(LocalDate data) {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String formatar(LocalDateTime data) {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public byte[] gerarComprovanteContaReceber(ContaReceberDTO conta) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Logo
                // Substitua todas as ocorrências de Image.getInstance() por:
            Image logo = Image.getInstance(Objects.requireNonNull(getClass().getResource("/assets/logo/logo.png")));
            logo.setAlignment(Element.ALIGN_LEFT);
            document.add(logo);

            // Título
            Paragraph titulo = new Paragraph("Comprovante de Conta a Receber", new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(new Paragraph(" "));

            // Informações
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            table.addCell("Número da Conta:");
            table.addCell(String.valueOf(conta.getId()));

            table.addCell("Cliente:");
            table.addCell(conta.getCliente() != null ? conta.getCliente() : "Cliente não informado");

            table.addCell("Data Vencimento:");
            table.addCell(formatarData(conta.getVencimento()));

            table.addCell("Data Recebimento:");
            table.addCell(conta.getDataRecebimento() != null ? formatarData(conta.getDataRecebimento()) : "-");

            table.addCell("Valor:");
            table.addCell("R$ " + conta.getValor());

            table.addCell("Status:");
            table.addCell(Boolean.TRUE.equals(conta.getRecebido()) ? "Recebido" : "Pendente");

            document.add(table);
            document.add(new Paragraph(" "));

            // Descrição
            Paragraph descricao = new Paragraph("Descrição:\n" + conta.getDescricao(), new Font(Font.FontFamily.HELVETICA, 12));
            document.add(descricao);
            document.add(new Paragraph(" "));

            // Assinatura
            Paragraph assinatura = new Paragraph("\n\n_____________________________________\nAssinatura do responsável");
            assinatura.setAlignment(Element.ALIGN_CENTER);
            document.add(assinatura);

        } catch (Exception e) {
            log.error("Erro ao gerar PDF", e);
        } finally {
            document.close();
        }

        return baos.toByteArray();
    }


    private String formatarData(LocalDate data) {
        return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

}
