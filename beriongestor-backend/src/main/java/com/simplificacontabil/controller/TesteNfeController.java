package com.simplificacontabil.controller;


import com.simplificacontabil.enums.ModeloNota;
import com.simplificacontabil.enums.TipoPessoa;
import com.simplificacontabil.mapper.VendaMapper;
import com.simplificacontabil.model.Cliente;
import com.simplificacontabil.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ContentDisposition;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/nfe")
@RequiredArgsConstructor
public class TesteNfeController {

    private final VendaMapper vendaMapper;





    private Venda criarVendaFake() {
        return Venda.builder()
                .cliente(criarClienteFake())
                .emitente(criarEmitenteFake())
                .itens(List.of(criarItemFake()))
                .totalVenda(new BigDecimal("10.00"))
                .modelo(ModeloNota.NFE)
                .build();
    }

    private Cliente criarClienteFake() {
        PessoaFisica pf = PessoaFisica.builder()
                .nome("Lucas Cliente")
                .cpf("10153165901")
                .email("lucas@email.com")
                .telefone("44999991803")
                .endereco(Endereco.builder()
                        .logradouro("Rua Belunno - 50")
                        .numero("100")
                        .bairro("Centro")
                        .cep("87240000")
                        .codigoIbge("4127205")
                        .uf("PR")
                        .build())
                .build();

        return Cliente.builder()
                .pessoaFisica(pf)
                .tipoPessoa(TipoPessoa.FISICA)
                .build();
    }

    private Empresa criarEmitenteFake() {
        return Empresa.builder()
                .razaoSocial("Biazin Sistemas LTDA")
                .nomeFantasia("Biazin Sistemas")
                .cnpj("47397316000122") // CNPJ FICTÍCIO
                .inscricaoEstadual("ISENTO")
                .endereco(Endereco.builder()
                        .logradouro("Rua Belluno")
                        .numero("50")
                        .bairro("Tartarelli")
                        .cep("87240000")
                        .codigoIbge("4127205")
                        .uf("PR")
                        .build())
                .build();
    }

    private ItemVenda criarItemFake() {
        Produto produto = Produto.builder()
                .id(1L)
                .nome("Produto Teste")
                .descricao("Descrição do produto")
                .ncm("21069090") // NCM comum
                .precoUnitario(new BigDecimal("10.00"))
                .build();

        return ItemVenda.builder()
                .produto(produto)
                .nomeProduto(produto.getNome())
                .precoUnitario(produto.getPrecoUnitario())
                .quantidade(1)
                .totalItem(produto.getPrecoUnitario())
                .build();
    }

    @GetMapping("/emitir-e-baixar")
    public ResponseEntity<byte[]> emitirNotaEbaixar() {
        try {
            Venda venda = criarVendaFake();

            Path caminho = Paths.get("/var/recibos/recibo-" + venda.getId() + ".pdf");
            byte[] pdfBytes = Files.readAllBytes(caminho);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("DANFE-" + venda.getChaveAcessoNfe() + ".pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erro: " + e.getMessage()).getBytes());
        }
    }

}

