package com.simplificacontabil.controller;

import com.simplificacontabil.model.ContaReceber;
import com.simplificacontabil.repository.ContaReceberRepository;
import com.simplificacontabil.util.PixPayloadGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/pix")
@RequiredArgsConstructor
public class PixController {

    private final ContaReceberRepository contaReceberRepository;

    private static final String CHAVE_PIX = "lucasbiazin07@gmail.com";
    private static final String NOME_RECEBEDOR = "Biazin Sistemas";
    private static final String CIDADE = "TERRA BOA";
    private static final String TXID = "Biazin Sistemas - Softwares e desenvolvimentos";

    @GetMapping("/payload")
    public ResponseEntity<String> gerarPayloadPix(@RequestParam String valor) {
        String payload = PixPayloadGenerator.getPayload(
                CHAVE_PIX,
                NOME_RECEBEDOR,
                CIDADE,
                TXID,
                valor
        );
        return ResponseEntity.ok(payload);
    }

    @GetMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> gerarQrCodePix(@RequestParam String valor) throws Exception {
        String payload = PixPayloadGenerator.getPayload(
                CHAVE_PIX,
                NOME_RECEBEDOR,
                CIDADE,
                TXID,
                valor
        );

        return ResponseEntity.ok(gerarImagemQRCode(payload));
    }

    @GetMapping(value = "/conta/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> gerarQrCodePixDaConta(@PathVariable Long id) throws Exception {
        ContaReceber conta = contaReceberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta a receber não encontrada"));

        String payload = PixPayloadGenerator.getPayload(
                CHAVE_PIX,
                NOME_RECEBEDOR,
                CIDADE,
                "CR" + conta.getId(),
                conta.getValor().toString()
        );

        return ResponseEntity.ok(gerarImagemQRCode(payload));
    }

    private byte[] gerarImagemQRCode(String payload) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(payload, BarcodeFormat.QR_CODE, 300, 300);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
}
