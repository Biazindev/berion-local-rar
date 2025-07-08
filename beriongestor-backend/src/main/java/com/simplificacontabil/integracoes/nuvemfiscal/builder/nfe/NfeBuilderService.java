package com.simplificacontabil.integracoes.nuvemfiscal.builder.nfe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplificacontabil.dto.EmitenteDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.*;
import com.simplificacontabil.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NfeBuilderService {

    @Autowired
    private ObjectMapper objectMapper;

    public EmitirNfeRequestDTO montarNfeCompleta(Venda venda) {
        ChaveAcessoInfo chave = gerarChaveAcesso(venda, venda.getEmitente().getCnpj());



        return EmitirNfeRequestDTO.builder()
                .ambiente("homologacao")
                .referencia("NFE-" + venda.getId() + "-" + UUID.randomUUID())
                .infNFe(InfNfeDTO.builder()
                        .versao("4.00")
                        .id(chave.id)
                        .ide(montarIde(venda, chave))
                        .emit(montarEmitente(venda))
                        .dest(montarDestinatario(venda))
                        .det(montarItens(venda))
                        .total(montarTotais(venda, venda.getFrete()))
                        .transp(TransporteDTO.builder()
                                .modFrete(Optional.ofNullable(venda.getModFrete()).orElse(9))
                                .transporta(
                                        (venda.getModFrete() != null && (venda.getModFrete() == 0 || venda.getModFrete() == 1 || venda.getModFrete() == 2))
                                                ? (
                                                venda.getFrete() != null && venda.getFrete().getTransportadora() != null
                                                        ? (
                                                        venda.getFrete().getTransportadora().getCPF() != null
                                                                ? venda.getFrete().getTransportadora().toBuilder().CNPJ(null).build()
                                                                : venda.getFrete().getTransportadora().toBuilder().CPF(null).build()) : null) : null)

                                .vol(
                                        (venda.getModFrete() != null && (venda.getModFrete() == 0 || venda.getModFrete() == 1 || venda.getModFrete() == 2))
                                                ? (venda.getFrete() != null && venda.getFrete().getVolumes() != null
                                                ? venda.getFrete().getVolumes().stream()
                                                .map(vol -> {
                                                    // Garante que os campos obrigatórios não sejam vazios
                                                    String esp = vol.getEsp() != null && !vol.getEsp().trim().isEmpty() ? vol.getEsp() : "NA";
                                                    String marca = vol.getMarca() != null && !vol.getMarca().trim().isEmpty() ? vol.getMarca() : "NA";
                                                    String nVol = vol.getNVol() != null && !vol.getNVol().trim().isEmpty() ? vol.getNVol() : "1";

                                                    // Cria uma cópia do volume com os campos ajustados
                                                    return vol.toBuilder()
                                                            .esp(esp)
                                                            .marca(marca)
                                                            .nVol(nVol)
                                                            .build();
                                                })
                                                .collect(Collectors.toList())
                                                : null)
                                                : null
                                )
                                .build())
                        .pag(montarPagamento(venda))
                        .infRespTec(montarResponsavelTecnico())
                        .infAdic(InformacoesAdicionaisDTO.builder()
                                .infCpl(venda.getObservacao())
                                .infAdFisco(Objects.requireNonNullElse(venda.getInfAdFisco(), "-"))
                                .build())
                        .build())
                .build();
    }

    private IdeDTO montarIde(Venda venda, ChaveAcessoInfo chave) {


        return IdeDTO.builder()
                .cUF(ufParaCodigo(venda.getEmitente().getEndereco().getUf()))
                .cNF(chave.cNF)
                .natOp("VENDA")
                .mod(55)
                .serie(Optional.ofNullable(venda.getEmitente().getSerieNfe()).orElse(1))
                .nNF(venda.getId().intValue() + 1)
                .dhEmi(OffsetDateTime.now().toString())
                .dhSaiEnt(OffsetDateTime.now().toString())
                .tpNF(1)
                .idDest(1)
                .cMunFG(venda.getEmitente().getEndereco().getCodigoIbge()) // Já deve estar preenchido no cadastro
                .tpImp(1)
                .tpEmis(1)
                .cDV(chave.cDV)
                .tpAmb(2)
                .finNFe(1)
                .indFinal(1)
                .indPres(1)
                .indIntermed(0)
                .procEmi(0)
                .verProc("1.0")
                .build();
    }

    private EmitenteDTO montarEmitente(Venda venda) {
        Empresa empresa = venda.getEmitente();

        return EmitenteDTO.builder()
                .cnpj(empresa.getCnpj())
                .razaoSocial(empresa.getRazaoSocial())
                .nomeFantasia(empresa.getNomeFantasia())
                .inscricaoEstadual(empresa.getInscricaoEstadual())
                .inscricaoMunicipal(empresa.getInscricaoMunicipal() != null && !empresa.getInscricaoMunicipal().isBlank() ? empresa.getInscricaoMunicipal() : null)
                .CNAE(empresa.getCnae())
                .crt(empresa.getCrt())
                .endereco(EnderecoNfeDTO.builder()
                        .xLgr(empresa.getEndereco().getLogradouro())
                        .nro(empresa.getEndereco().getNumero())
                        .xBairro(empresa.getEndereco().getBairro())
                        .xMun(empresa.getEndereco().getMunicipio())
                        .cMun(empresa.getEndereco().getCodigoIbge())
                        .UF(empresa.getEndereco().getUf())
                        .CEP(empresa.getEndereco().getCep())
                        .cPais("1058")
                        .xPais("Brasil")
                        .fone(empresa.getFone()) // ✅ OK aqui
                        .build())
                .build();
    }


    private DestinatarioDTO montarDestinatario(Venda venda) {
        Cliente cliente = venda.getCliente();
        PessoaFisica pf = cliente.getPessoaFisica();

        return DestinatarioDTO.builder()
                .cpf(pf.getCpf())
                .xNome(pf.getNome())
                .enderDest(EnderecoNfeDTO.builder()
                        .xLgr(pf.getEndereco().getLogradouro())
                        .nro(pf.getEndereco().getNumero())
                        .xBairro(pf.getEndereco().getBairro())
                        .xMun(pf.getEndereco().getMunicipio())
                        .cMun(pf.getEndereco().getCodigoIbge())
                        .UF(pf.getEndereco().getUf())
                        .CEP(pf.getEndereco().getCep())
                        .cPais("1058")
                        .xPais("BRASIL")
                        .build())
                .indIEDest(9)
                .email(pf.getEmail())
                .build();
    }

    private List<DetDTO> montarItens(Venda venda) {
        AtomicInteger contador = new AtomicInteger(1);

        // calcula frete rateado apenas se modFrete == 0,1,2
        Map<Long, BigDecimal> freteRateado;
        if (venda.getModFrete() != null && (venda.getModFrete() == 0 || venda.getModFrete() == 1 || venda.getModFrete() == 2)) {
            BigDecimal totalFrete = Optional.ofNullable(venda.getFrete())
                    .map(NfeFrete::getValorFrete)
                    .orElse(BigDecimal.ZERO);
            freteRateado = calcularFreteProporcional(venda, totalFrete);
        } else {
            freteRateado = Collections.emptyMap();
        }

        return venda.getItens().stream().map(item -> {
            Produto produto = item.getProduto();

            String cfop = Optional.ofNullable(produto.getCfop())
                    .orElseThrow(() -> new IllegalArgumentException("CFOP não informado para o produto: " + produto.getNome()));
            String unidade = Optional.ofNullable(produto.getUnidade())
                    .orElseThrow(() -> new IllegalArgumentException("Unidade não informada para o produto: " + produto.getNome()));
            String origem = Optional.ofNullable(produto.getOrigem("ICMS"))
                    .orElseThrow(() -> new IllegalArgumentException("Origem ICMS não informada para o produto: " + produto.getNome()));

            String csosn = produto.getCsosn("ICMS");
            String cst = produto.getCst("ICMS");
            BigDecimal aliqICMS = Optional.ofNullable(produto.getImposto("ICMS"))
                    .map(ImpostoProduto::getAliquota).orElse(BigDecimal.ZERO);

            String cstPis = Optional.ofNullable(produto.getCst("PIS")).orElse("07");
            String cstCofins = Optional.ofNullable(produto.getCst("COFINS")).orElse("07");

            BigDecimal baseICMS = item.getTotalItem();
            BigDecimal valorICMS = baseICMS.multiply(aliqICMS)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            Map<String, Object> icms = venda.getEmitente().getCrt() == 1
                    ? Map.of("ICMSSN102", Map.of("orig", Integer.parseInt(origem), "CSOSN", Optional.ofNullable(csosn).orElse("102")))
                    : Map.of("ICMS00", Map.of(
                    "orig", Integer.parseInt(origem),
                    "CST", Optional.ofNullable(cst).orElse("00"),
                    "modBC", 3,
                    "vBC", baseICMS,
                    "pICMS", aliqICMS,
                    "vICMS", valorICMS
            ));

            BigDecimal aliqPis = Optional.ofNullable(produto.getImposto("PIS"))
                    .map(ImpostoProduto::getAliquota).orElse(BigDecimal.ZERO);
            BigDecimal aliqCofins = Optional.ofNullable(produto.getImposto("COFINS"))
                    .map(ImpostoProduto::getAliquota).orElse(BigDecimal.ZERO);

            BigDecimal valorPisItem = baseICMS.multiply(aliqPis)
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal valorCofinsItem = baseICMS.multiply(aliqCofins)
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);

            Map<String, Object> pis = cstPis.equals("07")
                    ? Map.of("PISNT", Map.of("CST", cstPis))
                    : Map.of("PISAliq", Map.of(
                    "CST", cstPis,
                    "vBC", baseICMS,
                    "pPIS", aliqPis,
                    "vPIS", valorPisItem
            ));

            Map<String, Object> cofins = cstCofins.equals("07")
                    ? Map.of("COFINSNT", Map.of("CST", cstCofins))
                    : Map.of("COFINSAliq", Map.of(
                    "CST", cstCofins,
                    "vBC", baseICMS,
                    "pCOFINS", aliqCofins,
                    "vCOFINS", valorCofinsItem
            ));

            return DetDTO.builder()
                    .nItem(contador.getAndIncrement())
                    .prod(ProdutoNfeDTO.builder()
                            .cProd(produto.getId().toString())
                            .cEAN(Optional.ofNullable(produto.getEan()).orElse("SEM GTIN"))
                            .xProd(produto.getNome())
                            .NCM(produto.getNcm())
                            .CFOP(cfop)
                            .uCom(unidade)
                            .qCom(new BigDecimal(item.getQuantidade()))
                            .vUnCom(produto.getValorUnitarioComercial())
                            .vProd(item.getTotalItem())
                            .vFrete(freteRateado.getOrDefault(item.getId(), BigDecimal.ZERO))
                            .cEANTrib(Optional.ofNullable(produto.getEan()).orElse("SEM GTIN"))
                            .uTrib(unidade)
                            .qTrib(new BigDecimal(item.getQuantidade()))
                            .vUnTrib(produto.getValorUnitarioTributavel())
                            .indTot(1)
                            .build())
                    .imposto(ImpostoDTO.builder()
                            .ICMS(icms)
                            .PIS(pis)
                            .COFINS(cofins)
                            .build())
                    .build();
        }).collect(Collectors.toList());
    }

    private TotalDTO montarTotais(Venda venda, NfeFrete extras) {
        BigDecimal totalProdutos = BigDecimal.ZERO;
        BigDecimal vBC = BigDecimal.ZERO;
        BigDecimal vICMS = BigDecimal.ZERO;
        BigDecimal vPIS = BigDecimal.ZERO;
        BigDecimal vCOFINS = BigDecimal.ZERO;
        BigDecimal frete = Optional.ofNullable(extras.getValorFrete()).orElse(BigDecimal.ZERO);
        BigDecimal outrasDes = Optional.ofNullable(extras.getOutrasDespesas()).orElse(BigDecimal.ZERO);
        BigDecimal seguro = Optional.ofNullable(extras.getValorSeguro()).orElse(BigDecimal.ZERO);
        BigDecimal desconto = Optional.ofNullable(extras.getValorDesconto()).orElse(BigDecimal.ZERO);

        int crt = venda.getEmitente().getCrt();

        for (ItemVenda item : venda.getItens()) {
            BigDecimal valorTotalItem = item.getTotalItem();
            totalProdutos = totalProdutos.add(valorTotalItem);

            // ICMS
            ImpostoProduto icmImp = item.getProduto().getImposto("ICMS");
            if (crt != 1 && icmImp != null && icmImp.getAliquota() != null) {
                vBC = vBC.add(valorTotalItem);
                BigDecimal icmsValor = valorTotalItem.multiply(icmImp.getAliquota())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                vICMS = vICMS.add(icmsValor);
            }

            // PIS — agora já arredondado a 2 casas
            ImpostoProduto pisImp = item.getProduto().getImposto("PIS");
            if (pisImp != null && pisImp.getAliquota() != null) {
                BigDecimal pisValor = valorTotalItem.multiply(pisImp.getAliquota())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                vPIS = vPIS.add(pisValor);
            }

            // COFINS — idem
            ImpostoProduto cofImp = item.getProduto().getImposto("COFINS");
            if (cofImp != null && cofImp.getAliquota() != null) {
                BigDecimal cofinsValor = valorTotalItem.multiply(cofImp.getAliquota())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                vCOFINS = vCOFINS.add(cofinsValor);
            }
        }

        // calcula valor total da nota
        BigDecimal valorNota = totalProdutos
                .add(frete)
                .add(seguro)
                .subtract(desconto)
                .add(outrasDes)
                .setScale(2, RoundingMode.HALF_UP);

        return TotalDTO.builder()
                .ICMSTot(ICMSTotDTO.builder()
                        .vBC(vBC.setScale(2, RoundingMode.HALF_UP))
                        .vICMS(vICMS.setScale(2, RoundingMode.HALF_UP))
                        .vProd(totalProdutos.setScale(2, RoundingMode.HALF_UP))
                        .vNF(valorNota)
                        .vPIS(vPIS.setScale(2, RoundingMode.HALF_UP))
                        .vCOFINS(vCOFINS.setScale(2, RoundingMode.HALF_UP))
                        .vICMSDeson(BigDecimal.ZERO)
                        .vFCP(BigDecimal.ZERO)
                        .vBCST(BigDecimal.ZERO)
                        .vST(BigDecimal.ZERO)
                        .vFCPST(BigDecimal.ZERO)
                        .vFCPSTRet(BigDecimal.ZERO)
                        .vSeg(seguro.setScale(2, RoundingMode.HALF_UP))
                        .vDesc(desconto.setScale(2, RoundingMode.HALF_UP))
                        .vFrete(frete.setScale(2, RoundingMode.HALF_UP))
                        .vOutro(outrasDes.setScale(2, RoundingMode.HALF_UP))
                        .vII(BigDecimal.ZERO)
                        .vIPI(BigDecimal.ZERO)
                        .vIPIDevol(BigDecimal.ZERO)
                        .build())
                .build();
    }

    private String validarOuSemGTIN(String ean) {
        if (ean == null || ean.isBlank() || ean.length() < 8 || ean.length() > 14 || !ean.matches("\\d+")) {
            return "SEM GTIN";
        }
        return ean;
    }



    private PagamentoNfeDTO montarPagamento(Venda venda) {
        return PagamentoNfeDTO.builder()
                .detPag(List.of(DetPagamentoDTO.builder()
                        .tPag("01")
                        .vPag(venda.getPagamento().getTotalPagamento())
                        .build()))
//                .vTroco(0.0)
                .build();
    }

    public NotaFiscalResponseDTO montarResposta(String respostaJson) {
        try {
            JsonNode json = objectMapper.readTree(respostaJson);
            String id = json.path("id").asText();

            return NotaFiscalResponseDTO.builder()
                    .idNota(id)
                    .status(json.path("status").asText("desconhecido"))
                    .mensagem(json.path("mensagem").asText("NF-e emitida."))
                    .chaveAcesso(json.path("chave").asText(""))
                    .numeroNota(json.path("numero").asInt())
                    .serie(json.path("serie").asInt())
                    .modelo(json.path("modelo").asText("55"))
                    .dataAutorizacao(json.path("data_autorizacao").asText())
                    .danfeUrl("https://api.nuvemfiscal.com.br/nfe/" + id + "/pdf")
                    .xmlUrl("https://api.nuvemfiscal.com.br/nfe/" + id + "/xml")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao montar resposta da NF-e: " + e.getMessage(), e);
        }
    }

    private ResponsavelTecnicoDTO montarResponsavelTecnico() {
        return ResponsavelTecnicoDTO.builder()
                .cnpj("47397316000122")
                .xContato("Lucas Vinicius Biazin")
                .email("help@biazinsistemas.com")
                .fone("44999991803")
                .idCSRT(1)
                .CSRT("6PVKF62PMBHUNFZT0XIMMP8AKI0XG34RLE6I")
                .hashCSRT("")
                .build();
    }

    private static class ChaveAcessoInfo {
        String id;
        String cNF;
        int cDV;
    }

    private ChaveAcessoInfo gerarChaveAcesso(Venda venda, String cnpjEmitente) {
        Empresa emitente = venda.getEmitente();
        String cUF = String.valueOf(ufParaCodigo(emitente.getEndereco().getUf()));
        String data = OffsetDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyMM"));
        String mod = "55";
        String serie = String.format("%03d", Optional.ofNullable(emitente.getSerieNfe()).orElse(1));
        String numero = String.format("%09d", venda.getId());
        String tpEmis = "1";
        String cNF = String.format("%08d", new Random().nextInt(90000000) + 10000000);

        String base = cUF + data + cnpjEmitente.replaceAll("\\D", "") + mod + serie + numero + tpEmis + cNF;
        int dv = calcularDV(base);

        ChaveAcessoInfo info = new ChaveAcessoInfo();
        info.id = "NFe" + base + dv;
        info.cNF = cNF;
        info.cDV = dv;
        return info;
    }

    private int calcularDV(String chave) {
        int soma = 0;
        int peso = 2;

        for (int i = chave.length() - 1; i >= 0; i--) {
            soma += Character.getNumericValue(chave.charAt(i)) * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }

        int resto = soma % 11;
        return (resto == 0 || resto == 1) ? 0 : 11 - resto;
    }

    private int ufParaCodigo(String uf) {
        return switch (uf.toUpperCase()) {
            case "RO" -> 11;
            case "AC" -> 12;
            case "AM" -> 13;
            case "RR" -> 14;
            case "PA" -> 15;
            case "AP" -> 16;
            case "TO" -> 17;
            case "MA" -> 21;
            case "PI" -> 22;
            case "CE" -> 23;
            case "RN" -> 24;
            case "PB" -> 25;
            case "PE" -> 26;
            case "AL" -> 27;
            case "SE" -> 28;
            case "BA" -> 29;
            case "MG" -> 31;
            case "ES" -> 32;
            case "RJ" -> 33;
            case "SP" -> 35;
            case "PR" -> 41;
            case "SC" -> 42;
            case "RS" -> 43;
            case "MS" -> 50;
            case "MT" -> 51;
            case "GO" -> 52;
            case "DF" -> 53;
            default -> throw new IllegalArgumentException("UF inválido: " + uf);
        };
    }

    private Map<Long, BigDecimal> calcularFreteProporcional(Venda venda, BigDecimal totalFrete) {
        Map<Long, BigDecimal> fretePorItem = new HashMap<>();

        BigDecimal totalProdutos = venda.getItens().stream()
                .map(ItemVenda::getTotalItem)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalProdutos.compareTo(BigDecimal.ZERO) == 0) {
            // evita divisão por zero
            venda.getItens().forEach(item -> fretePorItem.put(item.getId(), BigDecimal.ZERO));
            return fretePorItem;
        }

        BigDecimal acumulado = BigDecimal.ZERO;
        for (int i = 0; i < venda.getItens().size(); i++) {
            ItemVenda item = venda.getItens().get(i);

            BigDecimal percentual = item.getTotalItem().divide(totalProdutos, 8, RoundingMode.HALF_UP);
            BigDecimal freteItem = totalFrete.multiply(percentual).setScale(2, RoundingMode.HALF_UP);

            // soma ao acumulado para ajuste final
            acumulado = acumulado.add(freteItem);

            // para último item ajustar a diferença de arredondamento
            if (i == venda.getItens().size() - 1) {
                BigDecimal diff = totalFrete.subtract(acumulado);
                freteItem = freteItem.add(diff);
            }

            fretePorItem.put(item.getId(), freteItem);
        }

        return fretePorItem;
    }

}
