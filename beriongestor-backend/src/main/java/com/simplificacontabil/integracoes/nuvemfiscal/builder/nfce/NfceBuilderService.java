package com.simplificacontabil.integracoes.nuvemfiscal.builder.nfce;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce.*;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfce.TranspDTO;
import com.simplificacontabil.integracoes.nuvemfiscal.dto.nfe.*;
import com.simplificacontabil.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NfceBuilderService {

    private final ObjectMapper objectMapper;

    public EmitirNfceRequestDTO montarNfceCompleta(Venda venda) {
        ChaveAcessoInfo chave = gerarChaveAcesso(venda, venda.getEmitente().getCnpj(), "65");

        NfeSefazInfNFe infNFe = NfeSefazInfNFe.builder()
                .Id(chave.id)
                .versao("4.00")
                .ide(montarIde(venda, chave))
                .emit(montarEmitente(venda))
                .dest(montarDestinatario(venda))
                .det(montarItens(venda))
                .total(montarTotais(venda))
                .transp(TranspDTO.builder()
                        .modFrete(Optional.ofNullable(venda.getModFrete()).orElse(9))
                        .build())
                .cobr(CobrDTO.builder()
                        .dup(Collections.emptyList())
                        .build())
                .infAdic(InfAdicDTO.builder()
                        .infCpl(venda.getItens().toString())
                        .infAdFisco("")
                        .build())
                .pag(PagDTO.builder()
                        .detPag(List.of(DetPagDTO.builder()
                                .tPag("01")
                                .vPag(venda.getPagamento().getTotalPagamento())
                                .build()))
                        .vTroco(BigDecimal.ZERO)
                        .build())
                .build();

        return EmitirNfceRequestDTO.builder()
                .infNFe(infNFe)
                .infNFeSupl(NfeSefazInfNFeSupl.builder()
                        .qrCode(buildQrCodeUrl(chave.id))
                        .urlChave(buildConsultaUrl(chave.id))
                        .build())
                .ambiente("2")
                .referencia("NFCE-" + venda.getId() + "-" + UUID.randomUUID())
                .build();
    }

    private IdeDTO montarIde(Venda venda, ChaveAcessoInfo chave) {
        Empresa em = venda.getEmitente();
        return IdeDTO.builder()
                .cUF(ufParaCodigo(em.getEndereco().getUf()))
                .cNF(chave.cNF)
                .natOp("VENDA")
                .mod(65)
                .serie(1)
                .nNF(venda.getId().intValue())
                .dhEmi(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .idDest(1)
                .cMunFG((em.getEndereco().getCodigoIbge()))
                .tpImp(4)
                .tpEmis(1)
                .cDV(chave.cDV)
                .tpAmb(2)
                .finNFe(1)
                .indFinal(1)
                .indPres(1)
                .build();
    }

    private EmitDTO montarEmitente(Venda venda) {
        Empresa em = venda.getEmitente();
        EnderEmitDTO end = EnderEmitDTO.builder()
                .xLgr(em.getEndereco().getLogradouro())
                .nro(em.getEndereco().getNumero())
                .xBairro(em.getEndereco().getBairro())
                .cMun(em.getEndereco().getCodigoIbge())
                .xMun(em.getEndereco().getMunicipio())
                .UF(em.getEndereco().getUf())
                .CEP(em.getEndereco().getCep())
                .cPais("1058")
                .xPais("Brasil")
                .fone(Optional.ofNullable(em.getFone()).map(f -> f.replaceAll("\\D", "")).orElse(null))
                .build();
        return EmitDTO.builder()
                .cnpj(em.getCnpj())
                .xNome(em.getRazaoSocial())
                .xFant(em.getNomeFantasia())
                .enderEmit(end)
                .ie(em.getInscricaoEstadual())
                .crt(em.getCrt())
                .build();
    }

    private DestDTO montarDestinatario(Venda venda) {
        PessoaFisica pf = venda.getCliente().getPessoaFisica();
        EnderDestDTO end = EnderDestDTO.builder()
                .xLgr(pf.getEndereco().getLogradouro())
                .nro(pf.getEndereco().getNumero())
                .xBairro(pf.getEndereco().getBairro())
                .cMun(pf.getEndereco().getCodigoIbge())
                .xMun(pf.getEndereco().getMunicipio())
                .UF(pf.getEndereco().getUf())
                .CEP(pf.getEndereco().getCep())
                .fone(pf.getTelefone().replaceAll("\\D", ""))
                .build();
        return DestDTO.builder()
                .cpf(pf.getCpf())
                .xNome(pf.getNome())
                .enderDest(end)
                .indIEDest("9")
                .email(pf.getEmail())
                .build();
    }

    private List<DetDTO> montarItens(Venda venda) {
        AtomicInteger idx = new AtomicInteger(1);
        return venda.getItens().stream().map(item -> {
            Produto p = item.getProduto();
            String preco = p.getValorUnitarioComercial()
                    .setScale(2, RoundingMode.HALF_UP)
                    .toPlainString();
            return DetDTO.builder()
                    .nItem(idx.getAndIncrement())
                    .prod(ProdutoNfeDTO.builder()
                            .cProd(p.getId().toString())
                            .cEAN(validarOuSemGTIN(p.getEan()))
                            .xProd(p.getNome())
                            .NCM(p.getNcm())
                            .CFOP(p.getCfop())
                            .uCom(p.getUnidade())
                            .qCom(new BigDecimal(item.getQuantidade()))
                            .vUnCom(new BigDecimal(preco))
                            .vProd(item.getTotalItem())
                            .uTrib(p.getUnidade())
                            .qTrib(new BigDecimal(item.getQuantidade()))
                            .vUnTrib(new BigDecimal(preco))
                            .indTot(1)
                            .build())
                    .imposto(ImpostoDTO.builder()
                            .ICMS(montarMapaIcms(item))
                            .PIS(montarMapaPis(item))
                            .COFINS(montarMapaCofins(item))
                            .build())
                    .build();
        }).collect(Collectors.toList());
    }

    private TotalDTO montarTotais(Venda venda) {
        BigDecimal vProd = BigDecimal.ZERO, vBC = BigDecimal.ZERO;
        BigDecimal vICMS = BigDecimal.ZERO, vPIS = BigDecimal.ZERO, vCOFINS = BigDecimal.ZERO;
        for (ItemVenda it : venda.getItens()) {
            BigDecimal tot = it.getTotalItem();
            vProd = vProd.add(tot);
            ImpostoProduto icm = it.getProduto().getImposto("ICMS");
            if (icm != null) {
                vBC = vBC.add(tot);
                vICMS = vICMS.add(tot.multiply(icm.getAliquota())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
            }
            ImpostoProduto pis = it.getProduto().getImposto("PIS");
            if (pis != null) vPIS = vPIS.add(tot.multiply(pis.getAliquota())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
            ImpostoProduto cof = it.getProduto().getImposto("COFINS");
            if (cof != null) vCOFINS = vCOFINS.add(tot.multiply(cof.getAliquota())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        }
        ICMSTotDTO icmTot = ICMSTotDTO.builder()
                .vBC(vBC.setScale(2, RoundingMode.HALF_UP))
                .vICMS(vICMS.setScale(2, RoundingMode.HALF_UP))
                .vProd(vProd.setScale(2, RoundingMode.HALF_UP))
                .vNF(vProd.setScale(2, RoundingMode.HALF_UP))
                .vPIS(vPIS.setScale(2, RoundingMode.HALF_UP))
                .vCOFINS(vCOFINS.setScale(2, RoundingMode.HALF_UP))
                .build();
        return TotalDTO.builder().ICMSTot(icmTot).build();
    }

    private Map<String,Object> montarMapaIcms(ItemVenda it) {
        ImpostoProduto ip = it.getProduto().getImposto("ICMS");
        if (ip == null) return Map.of();
        BigDecimal base = it.getTotalItem();
        BigDecimal vICMS = base.multiply(ip.getAliquota())
                .divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP);
        return Map.of("ICMS00", Map.of(
                "orig", Integer.parseInt(it.getProduto().getOrigem("ICMS")),
                "CST", it.getProduto().getCst("ICMS"),
                "modBC",3,
                "vBC", base,
                "pICMS", ip.getAliquota(),
                "vICMS", vICMS
        ));
    }

    private Map<String,Object> montarMapaPis(ItemVenda it) {
        ImpostoProduto ip = it.getProduto().getImposto("PIS");
        if (ip == null) return Map.of();
        BigDecimal base = it.getTotalItem();
        BigDecimal v = base.multiply(ip.getAliquota())
                .divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP);
        return Map.of("PISAliq", Map.of(
                "CST", it.getProduto().getCst("PIS"),
                "vBC", base,
                "pPIS", ip.getAliquota(),
                "vPIS", v
        ));
    }

    private Map<String,Object> montarMapaCofins(ItemVenda it) {
        ImpostoProduto ip = it.getProduto().getImposto("COFINS");
        if (ip == null) return Map.of();
        BigDecimal base = it.getTotalItem();
        BigDecimal v = base.multiply(ip.getAliquota())
                .divide(BigDecimal.valueOf(100),2,RoundingMode.HALF_UP);
        return Map.of("COFINSAliq", Map.of(
                "CST", it.getProduto().getCst("COFINS"),
                "vBC", base,
                "pCOFINS", ip.getAliquota(),
                "vCOFINS", v
        ));
    }

    private String validarOuSemGTIN(String ean) {
        if (ean == null || !ean.matches("\\d{8,14}")) return "SEM GTIN";
        return ean;
    }

    private ChaveAcessoInfo gerarChaveAcesso(Venda venda, String cnpj, String modelo) {
        String uf = String.valueOf(ufParaCodigo(venda.getEmitente().getEndereco().getUf()));
        String dt = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyMM"));
        String serie = String.format("%03d", 1);
        String numero = String.format("%09d", venda.getId());
        String cNF = String.format("%08d", new Random().nextInt(90000000)+10000000);
        String base = uf + dt + cnpj.replaceAll("\\D","") + modelo + serie + numero + "1" + cNF;
        int dv = calcularDV(base);
        return new ChaveAcessoInfo("NFe"+base+dv, cNF, dv);
    }

    private int calcularDV(String chave) {
        int soma=0, peso=2;
        for(int i=chave.length()-1;i>=0;i--) {
            soma += Character.getNumericValue(chave.charAt(i))*peso;
            peso = peso==9?2:peso+1;
        }
        int r = soma%11; return (r<2?0:11-r);
    }

    private int ufParaCodigo(String uf) {
        return switch(uf.toUpperCase()) {
            case "PR" -> 41; case "SP" ->35; case "RJ" ->33; // ... demais UFs
            default -> throw new IllegalArgumentException("UF inválido");
        };
    }

    private String buildQrCodeUrl(String chave) {
        return String.format("https://www.sefa.pr.gov.br/QRCode?chNFe=%s", chave);
    }

    private String buildConsultaUrl(String chave) {
        return String.format("https://consulta.nfce.fazenda.pr.gov.br/consulta?chNFe=%s", chave);
    }

    private record ChaveAcessoInfo(String id, String cNF, int cDV) {}
}
