package com.simplificacontabil.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.simplificacontabil.dto.ProdutoDTO;
import com.simplificacontabil.integracoes.cosmos.client.CosmosApiClient;
import com.simplificacontabil.mapper.ProdutoMapper;
import com.simplificacontabil.model.Produto;
import com.simplificacontabil.repository.ProdutoRepository;
import com.simplificacontabil.service.ProdutoImportService;
import com.simplificacontabil.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    @Autowired
    private  ProdutoService produtoService;
    @Autowired
    ProdutoImportService produtoImportService;
    @Autowired
    private  ProdutoMapper produtoMapper;
    @Autowired
    private  ProdutoRepository produtoRepository;
    @Autowired
    private final CosmosApiClient cosmosClient;




    @PostMapping
    public ResponseEntity<?> cadastrarProduto(@RequestBody ProdutoDTO dto) {
        if (produtoRepository.existsByNomeAndPrecoUnitario(dto.getNome(), dto.getPrecoUnitario())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Produto já existe");
        }
        Produto produto = produtoMapper.toEntity(dto);
        produtoRepository.save(produto);

        ProdutoDTO responseDto = produtoMapper.toDTO(produto);
        return ResponseEntity.ok(responseDto);
    }


    @PostMapping("/lote")
    public ResponseEntity<?> cadastrarProdutoEmLote(@RequestBody List<ProdutoDTO> produtos) {
        produtos.forEach(produtoService::salvar);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<ProdutoDTO> listar() {
        return produtoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ProdutoDTO buscar(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    @GetMapping("/buscar/nome")
    public List<ProdutoDTO> buscarPorNome(@RequestParam String nome) {
        return produtoService.buscarPorNome(nome);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        produtoService.deletar(id);
    }

    @GetMapping("/exportar/excel")
    public ResponseEntity<ByteArrayResource> exportarExcel() {
        byte[] data = produtoService.exportarProdutosParaExcel();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=produtos.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(data));
    }

    @GetMapping("/exportar/pdf")
    public ResponseEntity<ByteArrayResource> exportarPdf() {
        byte[] data = produtoService.exportarProdutosParaPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=produtos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(data));
    }


    @PostMapping("/importar-xml")
    public ResponseEntity<?> importarXml(
            @RequestParam("file") MultipartFile file,
            @RequestParam("filialId") Long filialId
    ) {
        try {
            produtoImportService.importarProdutos(file, filialId);
            return ResponseEntity.ok("Importação concluída com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao importar XML", e);
            Map<String, String> erro = Map.of("erro", "Falha ao importar XML: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }


//    @PostMapping("/importar-xml")
//    public ResponseEntity<?> importarXml(@RequestParam("file") MultipartFile file, Long filialId) {
//        try {
//            produtoImportService.importarProdutosViaXml(file, filialId);
//            // sua lógica de parsing + salvar produtos
//            return ResponseEntity.ok("Importação concluída com sucesso!");
//        } catch (Exception e) {
//            log.error("Erro ao importar XML", e);
//            Map<String, String> erro = Map.of("erro", "Falha ao importar XML: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
//        }
//    }
//    @GetMapping("/produtos/{ean}")
//    public ResponseEntity<ProdutoDTO> buscarPorEan(@PathVariable String ean) {
//        Produto produto = produtoRepository.findByEan(ean)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
//        return ResponseEntity.ok(produtoMapper.toDTO(produto));
//    }

    /**
     * GET /produtos/gtins/{gtin}
     * Busca no Bluesoft Cosmos pelo código de barras e retorna o JSON cru.
     */
    @GetMapping("/gtins/{gtin}")
    public ResponseEntity<JsonNode> getByGtin(@PathVariable String gtin) {
        try {
            return ResponseEntity.ok(cosmosClient.getProductByGtin(gtin));
        } catch (IOException | InterruptedException e) {
            log.error("Falha na comunicação com a Cosmos API", e);
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Falha ao consultar Cosmos API: " + e.getMessage()
            );
        } catch (RuntimeException e) {
            log.warn("Produto não encontrado na API - GTIN: {}", gtin);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Produto não encontrado na API"
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoDTO dto) {
        return produtoRepository.findById(id)
                .map(produtoExistente -> {
                    Produto produtoAtualizado = produtoMapper.toEntity(dto);
                    produtoAtualizado.setId(id);
                    produtoAtualizado.setImpostos(produtoExistente.getImpostos());

                    return ResponseEntity.ok(produtoMapper.toDTO(
                            produtoRepository.save(produtoAtualizado)
                    ));
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Produto não encontrado com ID: " + id
                ));
    }
}
