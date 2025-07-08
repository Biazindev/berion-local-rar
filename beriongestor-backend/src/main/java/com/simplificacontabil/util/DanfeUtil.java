package com.simplificacontabil.util;

import com.fincatto.documentofiscal.nfe400.classes.nota.NFNotaInfoPagamento;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DanfeUtil {

    public static byte[] gerarDanfePdf(String xmlNota, boolean isNFCe, String modelo, String qrCode, String chave, boolean isHomologacao, List<NFNotaInfoPagamento> pagamentos) throws Exception {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        // Parsear XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(xmlNota.getBytes());
        org.w3c.dom.Document xml = builder.parse(is);

        // Adicionar cabeçalho
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph("DANFE " + (isNFCe ? "NFC-e" : "NF-e"), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Adicionar chave de acesso
        Paragraph chaveAcesso = new Paragraph("Chave de Acesso: " + chave);
        chaveAcesso.setAlignment(Element.ALIGN_CENTER);
        document.add(chaveAcesso);

        // Ambiente
        Paragraph ambiente = new Paragraph("AMBIENTE: " + (isHomologacao ? "HOMOLOGAÇÃO" : "PRODUÇÃO"));
        ambiente.setAlignment(Element.ALIGN_CENTER);
        document.add(ambiente);

        // Adicionar dados do XML
        addDadosXml(document, xml);

        // Adicionar QR Code se for NFCe
        if (isNFCe || "65".equals(modelo)) {
            Paragraph qrCodeText = new Paragraph("QR Code: " + qrCode);
            document.add(qrCodeText);
        }

        document.close();

        return baos.toByteArray();
    }

    private static void addDadosXml(Document document, org.w3c.dom.Document xml) throws DocumentException {
        // Extrair e adicionar dados relevantes do XML
        // Emitente
        document.add(new Paragraph("\nDADOS DO EMITENTE"));
        // Destinatário  
        document.add(new Paragraph("\nDADOS DO DESTINATÁRIO"));
        // Produtos
        document.add(new Paragraph("\nPRODUTOS"));
        // Valores
        document.add(new Paragraph("\nVALORES"));
    }

    public static void salvarDanfe(String xmlNota, Path destinoPdf, boolean isNFCe, String modelo, String qrCode, String chave, boolean isHomologacao, List<NFNotaInfoPagamento> pagamentos) throws Exception {
        byte[] pdf = gerarDanfePdf(xmlNota, isNFCe, modelo, qrCode, chave, isHomologacao, pagamentos);
        Files.write(destinoPdf, pdf);
    }
} 
