package com.simplificacontabil.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.simplificacontabil.integracoes.cosmos.client.CosmosApiClient;
import com.simplificacontabil.model.Estoque;
import com.simplificacontabil.model.Filial;
import com.simplificacontabil.model.ImpostoProduto;
import com.simplificacontabil.model.Produto;
import com.simplificacontabil.repository.EstoqueRepository;
import com.simplificacontabil.repository.FilialRepository;
import com.simplificacontabil.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ProdutoImportService {

    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private EstoqueRepository estoqueRepository;
    @Autowired
    private CosmosApiClient cosmosApiClient;

    @Transactional
    public Map<Produto, Integer> importarProdutos(MultipartFile doc, Long filialId)
            throws IOException, ParserConfigurationException, SAXException {

        Map<Produto, Integer> produtosImportados = new HashMap<>();
        Document document = parseXml(doc.getInputStream());
        NodeList detList = document.getElementsByTagNameNS("*", "det");

        for (int i = 0; i < detList.getLength(); i++) {
            Element det = (Element) detList.item(i);
            Element prod = (Element) det.getElementsByTagNameNS("*", "prod").item(0);

            String nome = getTagValue(prod, "xProd");
            String ncm = getTagValue(prod, "NCM");
            BigDecimal preco = new BigDecimal(getTagValue(prod, "vUnCom"));
            int quantidade = new BigDecimal(getTagValue(prod, "qCom")).intValue();

            String ean = getTagValue(prod, "cEAN");
            if (ean != null && (ean.equalsIgnoreCase("SEM GTIN") || ean.isBlank())) {
                ean = null;
            }

            Produto produto;
            Optional<Produto> existenteOpt = Optional.empty();

            if (ean != null) {
                existenteOpt = produtoRepository.findByEan(ean);
            }

            if (existenteOpt.isEmpty()) {
                existenteOpt = produtoRepository.findByNomeIgnoreCaseAndNcm(nome, ncm);
            }

            if (existenteOpt.isPresent()) {
                produto = existenteOpt.get();
                log.info("🔁 Produto existente encontrado ({}). Atualizando...", ean != null ? "EAN" : "NOME+NCM");

                produto.setNome(nome);
                produto.setNcm(ncm);
                produto.setPrecoUnitario(preco);
                atualizarDadosCosmos(produto, ean);
                adicionarImpostosProduto(det, produto);

                criarOuAtualizarEstoque(produto, filialId, quantidade);


                produtoRepository.save(produto);



            } else {
                produto = criarProduto(prod, quantidade);
                produto.setEan(ean);
                atualizarDadosCosmos(produto, ean);
                adicionarImpostosProduto(det, produto);
                produto = produtoRepository.save(produto);
                criarEstoque(produto, filialId, quantidade);
            }

            produtosImportados.put(produto, quantidade);
            log.info("📦 Produto '{}' processado com {} unidades", produto.getNome(), quantidade);
        }

        return produtosImportados;
    }

    private void criarOuAtualizarEstoque(Produto produto, Long filialId, int quantidade) {
        Estoque estoque = estoqueRepository.findByProdutoAndFilialId(produto, filialId);
        if (estoque != null) {
            estoque.setQuantidade(estoque.getQuantidade() + quantidade);
            estoque.setDataAtualizacao(LocalDateTime.now());
        } else {
            estoque = Estoque.builder()
                    .produto(produto)
                    .quantidade(quantidade)
                    .filialId(filialId)
                    .dataAtualizacao(LocalDateTime.now())
                    .build();
        }
        estoqueRepository.save(estoque);
    }



    private Produto criarProduto(Element prod, int quantidade) {
        String nome = getTagValue(prod, "xProd");
        String ncm = getTagValue(prod, "NCM");
        BigDecimal preco = new BigDecimal(getTagValue(prod, "vUnCom"));

        return Produto.builder()
                .ativo(true)
                .descricao("Importado via NF-e")
                .nome(nome)
                .ncm(ncm)
                .precoUnitario(preco)
                .quantidade(quantidade)
                .build();
    }


    private void atualizarDadosCosmos(Produto produto, String ean) {
        if (ean == null || ean.equalsIgnoreCase("SEM GTIN") || ean.isBlank()) {
            return;
        }

        try {
            JsonNode cosmosData = cosmosApiClient.getProductByGtin(ean);
            JsonNode produtoNode = cosmosData.path("product");

            String descricaoCosmos = produtoNode.path("description").asText(null);
            String imagemUrl = produtoNode.path("thumbnail").asText(null);

            if (descricaoCosmos != null && !descricaoCosmos.isBlank()) {
                produto.setDescricao(descricaoCosmos);
            }

            if (imagemUrl != null && !imagemUrl.isBlank()) {
                produto.setImagem(imagemUrl);
            }

            log.info("🔎 Dados da Cosmos aplicados ao produto {}", ean);
        } catch (Exception ex) {
            log.error("❌ Falha ao consultar GTIN {} na Cosmos", ean, ex);
        }
    }

    private void adicionarImpostosProduto(Element det, Produto produto) {
        List<ImpostoProduto> impostos = new ArrayList<>();
        Element impostoElement = (Element) det.getElementsByTagNameNS("*", "imposto").item(0);

        if (impostoElement != null) {
            adicionarImposto(impostos, impostoElement, "pICMS", "ICMS", produto);
            adicionarImposto(impostos, impostoElement, "pPIS", "PIS", produto);
            adicionarImposto(impostos, impostoElement, "pCOFINS", "COFINS", produto);
            adicionarImposto(impostos, impostoElement, "pIPI", "IPI", produto);
        }

        if (produto.getImpostos() == null) {
            produto.setImpostos(new ArrayList<>());
        } else {
            produto.getImpostos().clear();
        }
        produto.getImpostos().addAll(impostos);
    }

    private void criarEstoque(Produto produto, Long filialId, int quantidade) {
        Estoque estoque = Estoque.builder()
                .produto(produto)
                .quantidade(quantidade)
                .dataAtualizacao(LocalDateTime.now())
                .filialId(filialId)
                .build();

        estoqueRepository.save(estoque);
    }

    private Document parseXml(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputStream);
        doc.getDocumentElement().normalize();
        return doc;
    }

//    @Transactional
//    public Map<Produto, Integer> importarProdutosXml(MultipartFile file, Long filialId) throws Exception {
//        Document doc = parseXml(file.getInputStream());
//        String numeroNota = extrairNumeroNota(file);
//        salvarXmlNoDisco(file, numeroNota);
//        return importarProdutos(doc, filialId);
//    }

    private String extrairNumeroNota(MultipartFile file) {
        try {
            String xmlText = new String(file.getBytes(), StandardCharsets.UTF_8);
            Pattern pattern = Pattern.compile("<nNF>(.*?)</nNF>");
            Matcher matcher = pattern.matcher(xmlText);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
            log.error("Erro ao extrair nNF", e);
        }
        return "desconhecido";
    }

    private void salvarXmlNoDisco(MultipartFile file, String numeroNota) {
        try {
            String home = System.getProperty("user.home");
            File pasta = new File(home + File.separator + "NotasImportadas");
            if (!pasta.exists() && !pasta.mkdirs()) {
                throw new IOException("Não foi possível criar o diretório: " + pasta.getAbsolutePath());
            }
            String nomeArquivo = "nfe-" + numeroNota + ".xml";
            File destino = new File(pasta, nomeArquivo);
            file.transferTo(destino);

            log.info("✅ XML salvo em: {}", destino.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar XML no disco", e);
        }
    }


    private void adicionarImposto(List<ImpostoProduto> impostos, Element impostoElement, String tag, String tipo, Produto produto) {
        String valor = getTagValue(impostoElement, tag);
        if (!valor.isEmpty()) {
            try {
                BigDecimal aliquota = new BigDecimal(valor);
                ImpostoProduto imposto = new ImpostoProduto();
                imposto.setTipoImposto(tipo);
                imposto.setAliquota(aliquota);
                imposto.setProduto(produto);
                impostos.add(imposto);
            } catch (Exception e) {
                log.warn("⚠️ Erro ao parsear imposto {}: {}", tipo, e.getMessage());
            }
        }
    }

    private String getTagValue(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagNameNS("*", tagName);
        if (nodes.getLength() > 0 && nodes.item(0) != null) {
            return nodes.item(0).getTextContent();
        }
        return "";
    }
}
