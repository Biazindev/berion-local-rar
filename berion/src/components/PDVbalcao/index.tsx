import React, { useState, useEffect, useRef } from "react";
import { Link } from "react-router-dom";
import InputMask from "react-input-mask";
import Select from "react-select";
import { HiMiniMagnifyingGlass } from "react-icons/hi2";
import {
    Container,
    LeftPane,
    ProductList,
    RightPane,
    PdvButton,
    Input,
    Top,
    ImgContainer,
    Description,
    Icon,
    OrderList,
    TotaisContainer,
    ClienteInfoContainer,
    NameProduct,
    Button,
    Label,
} from "../PDVmesa/styles";
import { NotaFiscalModal } from "../NotaFiscal/NotaFiscalModal";
import SelectFrete from "../SelectFrete";
import {
    ProdutoProps,
    useGetProdutosQuery,
    useLazyGetClientesByPhoneQuery,
    ClienteProps,
    useAdicionarPedidoMutation,
    useEnviarParaEntregaMutation,
    Endereco,
    useAddVendaMutation,
    useAddNfeMutation,
    useLazyBaixarDanfeNfeQuery,
} from "../../services/api";
import { handleEmitir } from "../Utils/notaFiscal";
import { ItemVenda } from "../../types";
import { SwitchContainer, ToggleSwitch, Slider, ModalWrapper, ModalContainer, TitleModal } from "./styles";
import Loader from "../Loader";
import { NfDTO } from "../NotaFiscal/NFComponents";
import { useAddNfMutation } from "../../services/api";
import { RootState } from "../../store/reducers/index";
import { useSelector } from "react-redux";
import { EmitirNotaPayload } from "../NotaFiscal/NFSComponent";

interface EntregaData {
    pedidoId?: number;
    cliente_id?: number | null;
    fone: string;
    enderecoEntrega: Endereco;
    observacao?: string;
    nomeMotoboy?: string;
    precisaTroco?: boolean;
    trocoPara?: number;
    status?: "EM_PREPARO" | "SAIU_PARA_ENTREGA" | "ENTREGUE" | "CANCELADO";
}

interface ImpostosPayload {
    valorFrete: number;
    valorSeguro: number;
    desconto: number;
    pis: number;
    cofins: number;
}
interface EmitirNotaComImpostosPayload {
    vendaId: number;
    emitenteId: number;
    valorFrete?: number;
    valorSeguro?: number;
    desconto?: number;
    pis?: number;
    cofins?: number;
}

interface ValoresExtras {
    valorFrete?: number;
    valorSeguro?: number;
    desconto?: number;
    pis?: number;
    cofins?: number;
}

interface Transporta {
    CNPJ?: string;
    CPF?: string;
    xNome?: string;
    xEnder?: string;
    xMun?: string;
    UF?: string;
    IE?: string;
}

interface Volume {
    qVol?: number; //QUANTIDADE
    esp?: string; //ESPECIE
    marca?: string; //MARCA
    nVol?: string; //NUMERACAO
    pesoL?: number; //PESO LIQUIDO
    pesoB?: number; //PESO BRUTO
}

interface NfeFrete {
    valorFrete?: number;
    valorSeguro?: number;
    valorDesconto?: number;
    outrasDespesas?: number;
    transporta?: Transporta;
    volumes?: Volume[];
}

export type VendaData = {
    emitirNotaFiscal?: boolean;
    vendaAnonima?: boolean;
    documentoCliente?: string | null;
    cliente?: { id: number } | null;
    clienteId?: number;
    emitenteId?: number | null;
    modelo?: string | null;
    itens?: ItemVenda[];
    pagamento?: Pagamento;
    dataVenda?: string;
    status?: string;
    frete?: NfeFrete;
    modFrete?: number;
};

export interface PedidoItem {
    produto: {
        id?: number;
    };
    quantidade: number;
    observacao?: string;
}

export interface ClientePessoaFisica {
    cpf: string;
    clienteId?: number;
}

export interface ClientePessoaJuridica {
    cnpj: string;
    clienteId?: number;
}

export interface Cliente {
    tipoPessoa: "FISICA" | "JURIDICA";
    pessoaFisica?: ClientePessoaFisica;
    pessoaJuridica?: ClientePessoaJuridica;
}

export interface ProdutoSelecionado {
    produtoId: number;
    id?: number;
    nome?: string;
    nomeProduto?: string;
    preco?: string;
    precoUnitario?: string;
    quantidade: number;
    formPagamento?: string;
    numeroParcelas?: number;
    totalItem?: string;
}

export type Pagamento = {
    formaPagamento: string;
    valorPago: string;
    valorRestante: string;
    dataPagamento: string;
    status: string;
    numeroParcelas: number;
    totalVenda: string;
    totalDesconto: string;
    totalPagamento: string;
};

const tiposFrete = [
    { value: "0", label: "0 - Remetente paga frete (contrata frete)" },
    { value: "1", label: "1 - Destinatário paga frete	" },
    { value: "2", label: "2 - Terceiros pagam frete" },
    { value: "3", label: "3 - Transporte Próprio Remetente" },
    { value: "4", label: "4 - Transporte Próprio Destinatário" },
    { value: "9", label: "9 - Sem Ocorrência de Transporte" },
];

const estadosUF = [
    { value: 'AC', label: 'AC' }, { value: 'AL', label: 'AL' }, { value: 'AP', label: 'AP' }, { value: 'AM', label: 'AM' }, { value: 'BA', label: 'BA' },
    { value: 'CE', label: 'CE' }, { value: 'DF', label: 'DF' }, { value: 'ES', label: 'ES' }, { value: 'GO', label: 'GO' }, { value: 'MA', label: 'MA' }, { value: 'MT', label: 'MT' }, { value: 'MS', label: 'MS' }, { value: 'MG', label: 'MG' }, { value: 'PA', label: 'PA' }, { value: 'PB', label: 'PB' }, { value: 'PR', label: 'PR' }, { value: 'PE', label: 'PE' },
    { value: 'PI', label: 'PI' }, { value: 'RJ', label: 'RJ' }, { value: 'RN', label: 'RN' }, { value: 'RS', label: 'RS' }, { value: 'RO', label: 'RO' }, { value: 'RR', label: 'RR' }, { value: 'SC', label: 'SC' }, { value: 'SP', label: 'SP' }, { value: 'SE', label: 'SE' }, { value: 'TO', label: 'TO' },
];




const VendaBalcao: React.FC = () => {
    const [showNf, setShowNf] = useState(false);
    const [clienteBusca, setClienteBusca] = useState("");
    const [produtoBusca, setProdutoBusca] = useState("");
    const [addVenda, { isLoading: enviandoVenda }] = useAddVendaMutation();
    const [selectedValue, setSelectedValue] = useState<string | null>(null);
    const [selectedValuePag, setSelectedValuePag] = useState<string | null>(null);
    const inputRef = useRef<HTMLInputElement>(null);
    const empresa = useSelector((state: RootState) => state.empresa);
    const [produtosSelecionados, setProdutosSelecionados] = useState<
        ProdutoSelecionado[]
    >([]);
    const [adicionarPedido] = useAdicionarPedidoMutation();
    const [pedidoId, setPedidoId] = useState<number | null>(null);
    const [enviarNf] = useAddNfMutation();
    const [addNf, { isLoading: isLoadingNf }] = useAddNfMutation();
    const [addNfe, { isLoading: isLoadingNfe }] = useAddNfeMutation();
    const [modalOpen, setModalOpen] = useState(false);
    const emitente = useSelector((state: RootState) => state.empresa);
    const cliente = useSelector((state: RootState) => state.venda!.cliente);
    const produto = useSelector((state: RootState) => state.venda!.produtos);
    const [vendaId, setVendaId] = useState<number | null>(null);
    const [getDanfe] = useLazyBaixarDanfeNfeQuery();
    const [tipoFrete, setTipoFrete] = useState("");
    const [tipoNotaSelecionado, setTipoNotaSelecionado] = useState<
        "NFS" | "NFE" | null
    >(null);
    const [confirmarImpostosModal, setConfirmarImpostosModal] = useState(false);
    const [selectedTipoFrete, setSelectedTipoFrete] = useState<string | null>(null);
    const [modalFreteOpen, setModalFreteOpen] = useState(false);

    const handleDocChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const rawValue = e.target.value;
        const onlyDigits = rawValue.replace(/\D/g, ""); // remove tudo que não é número

        let formatted = "";
        const updatedTransporta: any = {};

        if (onlyDigits.length <= 11) {
            // Formatar como CPF: 000.000.000-00
            formatted = onlyDigits
                .replace(/^(\d{3})(\d)/, "$1.$2")
                .replace(/^(\d{3})\.(\d{3})(\d)/, "$1.$2.$3")
                .replace(/\.(\d{3})(\d)/, ".$1-$2");

            updatedTransporta.CPF = onlyDigits;
            updatedTransporta.CNPJ = "";
        } else if (onlyDigits.length <= 14) {
            // Formatar como CNPJ: 00.000.000/0000-00
            formatted = onlyDigits
                .replace(/^(\d{2})(\d)/, "$1.$2")
                .replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3")
                .replace(/\.(\d{3})(\d)/, ".$1/$2")
                .replace(/(\d{4})(\d)/, "$1-$2");

            updatedTransporta.CNPJ = onlyDigits;
            updatedTransporta.CPF = "";
        }

        setFreteData((prevData) => ({
            ...prevData,
            transporta: {
                ...prevData.transporta,
                ...updatedTransporta,
            },
        }));

        // Atualizar o input visível (sem afetar o valor armazenado)
        e.target.value = formatted;
    };


    useEffect(() => {
        if (selectedTipoFrete && ['0', '1', '2'].includes(selectedTipoFrete)) {
            setModalFreteOpen(true);
        } else {
            setModalFreteOpen(false);
        }
    }, [selectedTipoFrete]);


    useEffect(() => {
        const id = localStorage.getItem("ultimaVendaId");
        if (id) {
            setVendaId(Number(id));
        }
    }, []);


    const { data: produtos = [], isLoading, error } = useGetProdutosQuery();
    const [
        buscarCliente,
        {
            data: clienteEncontrado,
            isFetching: buscandoCliente,
            error: erroCliente,
        },
    ] = useLazyGetClientesByPhoneQuery();
    const [totalDesconto, setTotalDesconto] = useState("0,00");

    useEffect(() => {
        const id = localStorage.getItem("ultimaVendaId");
        if (id) {
            setVendaId(Number(id));
        }
    }, []);

    const handleSwitchClick = () => {
        console.log("Emitindo NFS...");
        const nextState = !showNf;
        setShowNf(nextState);

        if (nextState) {
            setModalOpen(true);
        } else {
            localStorage.removeItem("tipoNotaFiscal");
        }
    };

    const clienteSanitizado = { ...cliente };

    if (clienteSanitizado?.pessoaJuridica === null) {
        delete clienteSanitizado.pessoaJuridica;
    }

    if (clienteSanitizado?.pessoaFisica === null) {
        delete clienteSanitizado.pessoaFisica;
    }

    const handleTipoSelecionado = async (tipo: "NFS" | "NFE") => {
        setModalOpen(false);
        setTipoNotaSelecionado(tipo);

        const clienteEndereco = cliente?.endereco || {};
        const emitenteEndereco = emitente?.endereco || {};

        if (tipo === "NFS") {
            localStorage.setItem("tipoNotaFiscal", "NFS");

            const payloadNfs: EmitirNotaPayload = {
                cpfCnpjTomador:
                    cliente?.pessoaFisica?.cpf || cliente?.pessoaJuridica?.cnpj || "",
                nomeTomador:
                    cliente?.pessoaFisica?.nome ||
                    cliente?.pessoaJuridica?.razaoSocial ||
                    "",
                telefone:
                    cliente?.pessoaFisica?.telefone ||
                    cliente?.pessoaJuridica?.telefone ||
                    "",
                email:
                    cliente?.pessoaFisica?.email || cliente?.pessoaJuridica?.email || "",
                endereco: {
                    cep: clienteEndereco.cep || "",
                    bairro: clienteEndereco.bairro || "",
                    municipio: clienteEndereco.municipio || "",
                    logradouro: clienteEndereco.logradouro || "",
                    numero: clienteEndereco.numero || "",
                    uf: clienteEndereco.uf || "",
                    complemento: clienteEndereco.complemento || null,
                    codigoIbge: clienteEndereco.codigoIbge || "",
                },
                servico: {
                    descricao: "Programação de sistemas sob demanda",
                    valor: produtosSelecionados.reduce(
                        (acc, p) =>
                            acc +
                            parseFloat((p.precoUnitario ?? "0").toString()) * p.quantidade,
                        0
                    ),
                    codigoTributacaoMunicipal: "103",
                    codigoTributacaoNacional: "103",
                    cnae: "6209100",
                    nbs: "123456000",
                    informacoesComplementares:
                        "Sistema ERP desenvolvido sob demanda e entregue via repositório Git privado.",
                    locPrest: {
                        cLocPrestacao: clienteEndereco.codigoIbge || "",
                        cPaisPrestacao: "BR",
                    },
                    cServ: {
                        cTribNac: "103",
                        cTribMun: "103",
                        CNAE: "6209100",
                        xDescServ: "Programação de sistemas sob demanda",
                        cNBS: "123456000",
                    },
                    infoCompl: {
                        xInfComp:
                            "Sistema ERP desenvolvido sob demanda e entregue via repositório Git privado.",
                        idDocTec: null,
                        docRef: null,
                    },
                },
                empresa: {
                    razaoSocial: emitente?.razaoSocial || "",
                    nomeFantasia: emitente?.nomeFantasia || "",
                    cnpj: emitente?.cnpj || "",
                    inscricaoEstadual: emitente?.inscricaoEstadual || "",
                    inscricaoMunicipal: emitente?.inscricaoMunicipal || "",
                    regimeTributario: emitente?.regimeTributario || "",
                    cnae: emitente?.cnae || "",
                    crt: emitente?.crt ? emitente.crt.toString() : "",
                    fone: emitente?.fone || "",
                    endereco: {
                        cep: emitenteEndereco.cep || "",
                        bairro: emitenteEndereco.bairro || "",
                        municipio: emitenteEndereco.municipio || "",
                        logradouro: emitenteEndereco.logradouro || "",
                        numero: emitenteEndereco.numero || "",
                        uf: emitenteEndereco.uf || "",
                        complemento: emitenteEndereco.complemento || null,
                    },
                },
            };

            try {
                const response = await addNfe(payloadNfs).unwrap();
                console.log("NFS emitida com sucesso", response);

                // Abrir DANFE/NFS em PDF
                const blob = await getDanfe(response.id).unwrap();
                const url = URL.createObjectURL(blob);
                window.open(url, "_blank");
            } catch (err) {
                console.error("Erro ao emitir NFS:", err);
            }
        } else if (tipo === "NFE") {
            if (!vendaId) {
                console.error("vendaId não definido. Finalize a venda primeiro.");
                return;
            }

            localStorage.setItem("tipoNotaFiscal", "NFE");

            setConfirmarImpostosModal(true);

            const payloadNfe = {
                vendaId,
                emitenteId: emitente?.id || 1,
            };

            try {
                const response = await addNf(payloadNfe).unwrap();
                console.log("NFE emitida com sucesso", response);

                const blob = await getDanfe(response.idNota).unwrap();
                const url = URL.createObjectURL(blob);
                window.open(url, "_blank");
            } catch (err) {
                console.error("Erro ao emitir NFE:", err);
            }
        }
    };

    const sanitizeNumber = (value: string | number) => {
        if (typeof value === "string") {
            return parseFloat(value.replace(",", "."));
        }
        return value;
    };

    const parsePreco = (preco: string | number): number => {
        if (typeof preco === "number") {
            return preco;
        }
        return Number(preco.replace(",", ".").replace(/[^\d.-]/g, "")) || 0;
    };

    useEffect(() => {
        inputRef.current?.focus();
    }, []);

    const formatarApenasNumeros = (valor: string) => valor.replace(/\D/g, "");

    const formatPreco = (valor: number): string => {
        return valor.toFixed(2).replace(".", ",");
    };

    const formatPrecoBackend = (valor: number): string => {
        return valor.toFixed(2);
    };

    // Atualize o estado do formulário
    const [freteData, setFreteData] = useState<NfeFrete>({
        valorFrete: 0,
        valorSeguro: 0,
        valorDesconto: 0,
        outrasDespesas: 0,
        transporta: {
            CNPJ: "",
            CPF: "",
            xNome: "",
            xEnder: "",
            xMun: "",
            UF: "",
            IE: "",
        },
        volumes: [
            {
                qVol: 0,
                esp: "",
                marca: "",
                nVol: "",
                pesoL: 0,
                pesoB: 0,
            },
        ],
    });

    // Função para atualizar os campos do frete
    const handleFreteChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;

        // Verifica se o campo pertence a transporta
        if (name in (freteData.transporta || {})) {
            setFreteData((prev) => ({
                ...prev,
                transporta: {
                    ...prev.transporta,
                    [name]: value,
                },
            }));
        }
        // Verifica se o campo pertence ao primeiro volume
        else if (name in (freteData.volumes?.[0] || {})) {
            setFreteData((prev) => ({
                ...prev,
                volumes:
                    prev.volumes?.map((vol, i) =>
                        i === 0 ? { ...vol, [name]: value } : vol
                    ) || [],
            }));
        }
        // Campos diretos do frete
        else {
            setFreteData((prev) => ({
                ...prev,
                [name]: value,
            }));
        }
    };

    const gerarPayloadVenda = (
        clienteEncontrado: ClienteProps | null,
        clienteId: number,
        produtosSelecionados: ProdutoSelecionado[],
        pagamento: Pagamento,
        somaProdutos: number,
        showNf: boolean
    ): VendaData => {
        const agora = new Date().toISOString();

        // Verifica se o cliente existe e tem ID válido
        const clientePayload = clienteEncontrado?.clienteId
            ? { id: clienteEncontrado.clienteId }
            : null;

        return {
            emitirNotaFiscal: true,
            documentoCliente: clienteEncontrado?.documento || "",
            clienteId: clienteId,
            emitenteId: empresa.id,
            cliente: clientePayload, // Usa o objeto verificado
            modelo: "NFE",
            itens: produtosSelecionados.map((p) => ({
                produtoId: p.produtoId,
                nomeProduto: p.nome,
                precoUnitario: sanitizeNumber(p.precoUnitario || "0"),
                quantidade: p.quantidade,
                totalItem: sanitizeNumber(p.totalItem || "0"),
            })),
            pagamento: {
                formaPagamento: selectedValuePag || "DINHEIRO",
                valorPago: formatPrecoBackend(sanitizeNumber(somaProdutos)),
                valorRestante: formatPrecoBackend(0),
                dataPagamento: agora,
                status: "PAGO",
                numeroParcelas: Number(selectedValue) || 1,
                totalVenda: formatPrecoBackend(sanitizeNumber(somaProdutos)),
                totalDesconto: formatPrecoBackend(sanitizeNumber(totalDesconto)),
                totalPagamento: formatPrecoBackend(sanitizeNumber(somaProdutos)),
            },
            dataVenda: agora,
            status: "EM_PREPARO",
            vendaAnonima: !clienteEncontrado,
        };
    };
    const pagamento: Pagamento = {
        formaPagamento: "",
        valorPago: "0,00",
        valorRestante: "0,00",
        dataPagamento: "",
        status: "",
        numeroParcelas: 0,
        totalVenda: "0,00",
        totalDesconto: "0,00",
        totalPagamento: "0,00",
    };

    const [formData, setFormData] = useState({
        cnpj: "",
        cpf: "",
        nome: "",
        endereco: "",
        municipio: "",
        uf: "",
        ie: "",
        quantidade: 0,
        especie: "",
        marca: "",
        numeracao: "",
        pesoLiquido: 0,
        pesoBruto: 0,
    });

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    useEffect(() => {
        const delayDebounce = setTimeout(() => {
            if (clienteBusca.trim().length >= 3) {
                buscarCliente(clienteBusca);
            }
        }, 500);

        return () => clearTimeout(delayDebounce);
    }, [clienteBusca, buscarCliente]);

    const somaProdutos = produtosSelecionados.reduce((total, p) => {
        const preco = sanitizeNumber(p.precoUnitario || "0");
        const quantidade = sanitizeNumber(p.quantidade || 1);
        return total + preco * quantidade;
    }, 0);

    const handleFinalizarVenda = async () => {
        if (
            !clienteEncontrado &&
            !window.confirm("Deseja continuar sem cliente associado à venda?")
        ) {
            return;
        }

        const clienteId = clienteEncontrado?.id ?? null;
        const agora = new Date().toISOString();

        const clientePayload = clienteEncontrado
            ? {
                id: clienteEncontrado.id,
                tipoPessoa: clienteEncontrado.tipoPessoa,
                pessoaFisica: clienteEncontrado.pessoaFisica ?? null,
                pessoaJuridica: clienteEncontrado.pessoaJuridica ?? null,
            }
            : null;

        const payload = {
            emitirNotaFiscal: true,
            documentoCliente: clienteEncontrado?.documento || "",
            emitenteId: empresa.id,
            cliente: clientePayload,
            modelo: "NFE",
            itens: produtosSelecionados.map((p) => ({
                produtoId: p.produtoId,
                nomeProduto: p.nome,
                precoUnitario: sanitizeNumber(p.precoUnitario || "0"),
                quantidade: p.quantidade,
                totalItem: sanitizeNumber(p.totalItem || "0"),
            })),
            pagamento: {
                formaPagamento: selectedValuePag || "DINHEIRO",
                valorPago: formatPrecoBackend(sanitizeNumber(somaProdutos)),
                valorRestante: formatPrecoBackend(0),
                dataPagamento: agora,
                status: "PAGO",
                numeroParcelas: Number(selectedValue) || 1,
                totalVenda: formatPrecoBackend(sanitizeNumber(somaProdutos)),
                totalDesconto: formatPrecoBackend(sanitizeNumber(totalDesconto)),
                totalPagamento: formatPrecoBackend(sanitizeNumber(somaProdutos)),
            },
            dataVenda: agora,
            status: "EM_PREPARO",
            vendaAnonima: !clienteEncontrado,
            modFrete: parseInt(selectedTipoFrete ?? ""), // Garante que sempre terá um valor
            frete: {
                valorFrete: freteData.valorFrete,
                valorSeguro: freteData.valorSeguro,
                valorDesconto: freteData.valorDesconto,
                outrasDespesas: freteData.outrasDespesas,
                transportadora: freteData.transporta,
                volumes: freteData.volumes,
            },
        };

        try {
            const vendaCompleta = {
                cliente: clienteEncontrado,
                produtos: produtosSelecionados,
                pagamento,
                total: somaProdutos,
                data: agora,
                nf: true,
            };

            localStorage.setItem("ultimaVenda", JSON.stringify(vendaCompleta));

            const response = await addVenda(payload).unwrap();

            console.log("Resposta recebida do backend:", response);

            if (response?.vendaId) {
                setVendaId(response.vendaId);
                localStorage.setItem("ultimaVendaId", response.vendaId.toString());
                setModalOpen(true);
            } else {
                alert("Erro: resposta inesperada do backend.");
                return;
            }

            alert("Venda finalizada com sucesso!");
            setProdutosSelecionados([]);
            setClienteBusca("");
        } catch (error: any) {
            console.error("Erro ao finalizar venda:", error);

            if (error.data) {
                alert(`Erro da API: ${JSON.stringify(error.data)}`);
            } else if (error.message) {
                alert(`Erro: ${error.message}`);
            } else {
                alert("Erro desconhecido ao finalizar venda.");
            }
        }
    };

    const handleRemoverProduto = (indexToRemove: number) => {
        setProdutosSelecionados((prev) =>
            prev.filter((_, i) => i !== indexToRemove)
        );
    };

    const handleAdicionarProduto = async (produto: ProdutoProps) => {
        const precoUnitario = sanitizeNumber(produto.precoUnitario);
        const quantidade = 1;
        const totalItem = precoUnitario * quantidade;

        const novoItem: ProdutoSelecionado = {
            id: produto.id,
            produtoId: produto.id,
            nome: produto.nome,
            preco: produto.preco,
            precoUnitario: precoUnitario.toFixed(2),
            quantidade,
            totalItem: totalItem.toFixed(2),
        };

        setProdutosSelecionados((prev) => {
            const existente = prev.find((p) => p.produtoId === produto.id);

            if (existente) {
                const novaQuantidade = existente.quantidade + 1;
                const preco = sanitizeNumber(existente.precoUnitario ?? "0");
                const novoTotal = preco * novaQuantidade;

                return prev.map((p) =>
                    p.produtoId === produto.id
                        ? {
                            ...p,
                            precoUnitario: precoUnitario.toFixed(2),
                            quantidade: novaQuantidade,
                            totalItem: novoTotal.toFixed(2),
                        }
                        : p
                );
            }

            return [...prev, novoItem];
        });

        try {
            await adicionarPedido({
                itens: [
                    {
                        produtoId: produto.id,
                        quantidade: 1,
                        observacao: "",
                    },
                ],
            }).unwrap();
        } catch (err) {
            console.error("Erro ao adicionar pedido:", err);
        }
    };

    const opcoesPagamento = [
        { value: "DINHEIRO", label: "Dinheiro", active: true },
        { value: "PIX", label: "Pix" },
        { value: "CARTAO_CREDITO", label: "Cartão de Crédito" },
        { value: "CARTAO_DEBITO", label: "Cartão de Débito" },
        { value: "PARCELADO_LOJA", label: "Parcelado Loja" },
        { value: "CARTAO", label: "Cartão Genérico" },
    ];

    const parcelas = [
        { value: "1", label: "1x", active: true },
        { value: "2", label: "2x" },
        { value: "3", label: "3x" },
        { value: "4", label: "4x" },
        { value: "5", label: "5x" },
        { value: "6", label: "6x" },
    ];

    const [selectedParcela, setSelectedParcela] = useState(
        parcelas.find((p) => p.active) || null
    );
    const [selectedOpcoesPagamento, setSelectedOpcoesPagamento] = useState(
        opcoesPagamento.find((p) => p.active) || null
    );
    const descontoNumerico = parsePreco(totalDesconto);
    const totalComDesconto =
        somaProdutos - (isNaN(descontoNumerico) ? 0 : descontoNumerico);

    const formatarTelefone = (telefone: string) => {
        if (!telefone) return "";
        const numeros = telefone.replace(/\D/g, "");
        if (numeros.length === 11) {
            return `(${numeros.slice(0, 2)}) ${numeros.slice(2, 7)}-${numeros.slice(
                7
            )}`;
        }
        return telefone;
    };

    const formatarCpf = (cpf: string) => {
        if (!cpf) return "";
        const numeros = cpf.replace(/\D/g, "");
        if (numeros.length === 11) {
            return `${numeros.slice(0, 3)}.${numeros.slice(3, 6)}.${numeros.slice(
                6,
                9
            )}-${numeros.slice(9)}`;
        }
        return cpf;
    };

    const limitarTexto = (texto: string, limite: number = 17): string => {
        return texto.length > limite ? texto.substring(0, limite) + "..." : texto;
    };

    const limitar = (texto: string, limite: number = 30): string => {
        return texto.length > limite ? texto.substring(0, limite) + "..." : texto;
    };

    const emitirNotaFiscalComImpostos = async (valoresExtras: ValoresExtras) => {
        try {
            if (!vendaId) {
                console.error("vendaId não definido. Finalize a venda primeiro.");
                alert("Erro: vendaId não definido.");
                return;
            }
            const payloadNfe = {
                vendaId,
                emitenteId: emitente?.id || 1,
                ...valoresExtras, // injeta frete, seguro, descontos, etc
            };

            const response = await addNf(payloadNfe).unwrap();

            if (response?.danfeUrl) {
                window.open(response.danfeUrl, "_blank");
            } else {
                const blob = await getDanfe(response.idNota).unwrap();
                const url = URL.createObjectURL(blob);
                window.open(url, "_blank");
            }
        } catch (err) {
            console.error("Erro ao emitir NFE:", err);
            alert("Erro ao emitir NF-e.");
        }
    };

    const handleUFChange = (value: string) => {
        setFreteData((prev) => ({
            ...prev,
            transporta: {
                ...prev.transporta,
                UF: value,
            },
        }));
    };


    return (
        <>
            <div
                style={{
                    margin: "0 auto",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    width: "100%",
                }}
            ></div>
            <div style={{ padding: "1rem" }}>
                <div
                    style={{
                        display: "flex",
                        justifyContent: "end",
                        width: "100%",
                        margin: "0 auto",
                    }}
                >
                    <div
                        style={{
                            borderRadius: "8px",
                            display: "flex",
                            justifyContent: "center",
                            fontSize: "28px",
                            alignItems: "center",
                            backgroundColor: "#ccc",
                        }}
                    ></div>
                </div>
            </div>
            <>
                <Top></Top>
                <Container>
                    <LeftPane>
                        <div>
                            <InputMask
                                mask="(99) 99999-9999"
                                value={clienteBusca}
                                onChange={(e) =>
                                    setClienteBusca(formatarApenasNumeros(e.target.value))
                                }
                            >
                                {(inputProps: any) => (
                                    <Input
                                        {...inputProps}
                                        type="text"
                                        placeholder="Buscar cliente por telefone"
                                    />
                                )}
                            </InputMask>
                            {buscandoCliente && <p>Buscando cliente...</p>}
                            {Boolean(erroCliente) && <p>Erro ao buscar cliente.</p>}
                            {clienteBusca.trim().length >= 3 &&
                                !buscandoCliente &&
                                (clienteEncontrado ? (
                                    <ClienteInfoContainer>
                                        <div className="info">
                                            <p>
                                                <strong>Nome:</strong>{" "}
                                                {clienteEncontrado?.pessoaFisica?.nome}
                                            </p>
                                            <p>
                                                <strong>CPF:</strong>{" "}
                                                {formatarCpf(
                                                    clienteEncontrado?.pessoaFisica?.cpf || ""
                                                )}
                                            </p>
                                            <p>
                                                <strong>Telefone:</strong>{" "}
                                                {formatarTelefone(
                                                    clienteEncontrado?.pessoaFisica?.telefone || ""
                                                )}
                                            </p>
                                        </div>

                                        <div className="pagamento">
                                            <div>
                                                <label>Forma de Pagamento</label>
                                                <Select
                                                    options={opcoesPagamento}
                                                    value={selectedOpcoesPagamento}
                                                    onChange={(option) =>
                                                        setSelectedValuePag(option ? option.value : null)
                                                    }
                                                    placeholder="Selecione uma forma de pagamento"
                                                />
                                            </div>

                                            <div>
                                                <label>Parcelamento</label>
                                                <Select
                                                    options={parcelas}
                                                    value={selectedParcela}
                                                    onChange={(option) =>
                                                        setSelectedValue(option ? option.value : null)
                                                    }
                                                    placeholder="Selecione parcelamento"
                                                />
                                            </div>

                                            <div>
                                                <label>Desconto</label>
                                                <Input
                                                    type="text"
                                                    value={totalDesconto}
                                                    placeholder="Valor desconto"
                                                    onChange={(e) => setTotalDesconto(e.target.value)}
                                                />
                                            </div>
                                        </div>
                                    </ClienteInfoContainer>
                                ) : (
                                    <p>Nenhum cliente encontrado.</p>
                                ))}
                        </div>
                        <Link to={"/cadastro-clientes"}>
                            <PdvButton>Cadastrar Cliente</PdvButton>
                        </Link>
                        <OrderList>
                            <h4>Produtos Selecionados:</h4>
                            <ul>
                                {produtosSelecionados.map((produto, index) => {
                                    const id = produto.id ?? produto.produtoId ?? "N/A";
                                    const nome = produto.nome ?? "Sem nome";
                                    const preco = produto.precoUnitario ?? "0";
                                    const quantidade = produto.quantidade ?? 0;
                                    const total = formatPreco(parsePreco(preco) * quantidade);

                                    return (
                                        <li key={index}>
                                            <div className="produto-info">
                                                <div>
                                                    <strong>{nome}</strong>
                                                </div>
                                                <span>ID: {id}</span>
                                                <div>
                                                    R$ {Number(parsePreco(preco)).toFixed(2)} x{" "}
                                                    {quantidade}
                                                    <strong></strong>
                                                </div>
                                            </div>
                                            <button
                                                title="remover"
                                                className="remover"
                                                onClick={() => handleRemoverProduto(index)}
                                            >
                                                ×
                                            </button>
                                        </li>
                                    );
                                })}
                            </ul>
                        </OrderList>
                        <TotaisContainer>
                            <div className="linha">
                                <strong>Total:</strong>{" "}
                                <span>R$ {somaProdutos.toFixed(2)}</span>
                            </div>
                            <div className="linha">
                                <strong>Desconto:</strong>{" "}
                                <span>R$ {descontoNumerico.toFixed(2)}</span>
                            </div>
                            <div className="linha total-com-desconto">
                                <strong>Total com desconto:</strong>{" "}
                                <span>R$ {totalComDesconto.toFixed(2)}</span>
                            </div>
                        </TotaisContainer>
                        <label>
                            <input
                                type="checkbox"
                                checked={showNf}
                                onChange={handleSwitchClick}
                            />
                            Emitir Nota Fiscal
                        </label>

                        <div style={{ marginBottom: "1rem" }}>
                            <label>Tipo de Frete</label>
                            <Select
                                options={tiposFrete}
                                placeholder="Selecione o tipo de frete"
                                onChange={(option) => {
                                    setSelectedTipoFrete(option?.value || null);
                                    if (option && ["0", "1", "2", "3", "4"].includes(option.value)) {
                                        setModalFreteOpen(true);
                                    }
                                }}
                            />
                        </div>
                        <PdvButton onClick={handleFinalizarVenda} disabled={enviandoVenda}>
                            {enviandoVenda ? "Enviando..." : "Finalizar Venda"}
                        </PdvButton>
                    </LeftPane>
                    <RightPane>
                        <div
                            style={{
                                display: "flex",
                                justifyContent: "start",
                                alignItems: "center",
                            }}
                        >
                            <HiMiniMagnifyingGlass
                                style={{
                                    color: "#ccc",
                                    fontSize: "28px",
                                    position: "relative",
                                    left: "32px",
                                }}
                            />
                            <div style={{ width: "300px" }}>
                                <Input
                                    ref={inputRef}
                                    style={{ textAlign: "right" }}
                                    type="text"
                                    placeholder="Buscar produto"
                                    value={produtoBusca}
                                    onChange={(e: {
                                        target: { value: React.SetStateAction<string> };
                                    }) => setProdutoBusca(e.target.value)}
                                />
                            </div>
                            <div style={{ display: "flex", marginLeft: "12rem" }}>
                                <Link to={"/produtos-cadastrar"}>
                                    <div>
                                        <PdvButton style={{ marginRight: "24px" }}>
                                            Cadastrar produtos
                                        </PdvButton>
                                    </div>
                                </Link>
                            </div>
                        </div>
                        <div>
                            {isLoading && (
                                <p>
                                    <Loader />
                                </p>
                            )}
                            {Boolean(error) && <p>Erro ao carregar produtos.</p>}

                            {!isLoading && !error && (
                                <ProductList>
                                    {produtos
                                        .filter((produto: ProdutoProps) =>
                                            produto.nome
                                                ?.toLowerCase()
                                                .includes(produtoBusca.toLowerCase())
                                        )
                                        .map((produto: ProdutoProps) => (
                                            <div
                                                key={produto.id}
                                                onClick={() => handleAdicionarProduto(produto)}
                                            >
                                                <ImgContainer>
                                                    {produto.imagem && (
                                                        <img
                                                            src={
                                                                produto.imagem.startsWith("data:")
                                                                    ? produto.imagem
                                                                    : produto.imagem
                                                            }
                                                            alt="Produto"
                                                            style={{
                                                                width: "100%",
                                                                height: "120px",
                                                                objectFit: "contain",
                                                            }}
                                                        />
                                                    )}
                                                </ImgContainer>
                                                <NameProduct>
                                                    {limitarTexto(produto.nome ?? "")}
                                                </NameProduct>
                                                <Description>
                                                    <div>
                                                        <p>{limitar(produto.descricao ?? "")}</p>
                                                    </div>
                                                </Description>
                                                <span>
                                                    R$ {parseFloat(produto.precoUnitario)?.toFixed(2)}{" "}
                                                </span>
                                                <Icon>
                                                    <span>+</span>
                                                </Icon>
                                            </div>
                                        ))}
                                </ProductList>
                            )}
                        </div>
                    </RightPane>
                    <NotaFiscalModal
                        isOpen={modalOpen}
                        onClose={() => {
                            setModalOpen(false);
                            setShowNf(false);
                        }}
                        onSelect={handleTipoSelecionado}
                    />
                </Container>
                {modalFreteOpen && (
                    <ModalWrapper>
                        <ModalContainer style={{ maxWidth: "1120px", maxHeight: '90vh', overflowY: "auto" }}>
                            <TitleModal>Dados do Frete e Transportadora</TitleModal>

                            <h3>Dados do Frete</h3>
                            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "1rem" }}>
                                <div>
                                    <Label>Valor do Frete</Label>
                                    <Input
                                        type="number"
                                        name="valorFrete"
                                        value={freteData.valorFrete}
                                        onChange={handleFreteChange}
                                        placeholder="0,00"
                                    />
                                </div>
                                <div>
                                    <Label>Valor do Seguro</Label>
                                    <Input
                                        type="number"
                                        name="valorSeguro"
                                        value={freteData.valorSeguro}
                                        onChange={handleFreteChange}
                                        placeholder="0,00"
                                    />
                                </div>
                                <div>
                                    <Label>Desconto</Label>
                                    <Input
                                        type="number"
                                        name="valorDesconto"
                                        value={freteData.valorDesconto}
                                        onChange={handleFreteChange}
                                        placeholder="0,00"
                                    />
                                </div>
                                <div>
                                    <Label>Outras Despesas</Label>
                                    <Input
                                        type="number"
                                        name="outrasDespesas"
                                        value={freteData.outrasDespesas}
                                        onChange={handleFreteChange}
                                        placeholder="0,00"
                                    />
                                </div>
                            </div>

                            <h3 style={{ marginTop: "1rem" }}>Dados do Transportador</h3>
                            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "1rem" }}>
                                <div>
                                    <Label>CPF ou CNPJ</Label>
                                    <Input
                                        name="CPF"
                                        value={freteData.transporta?.CPF || freteData.transporta?.CNPJ || ""}
                                        onChange={handleDocChange}
                                        placeholder="000.000.000-00 ou 00.000.000/0000-00"
                                    />
                                </div>

                                <div>
                                    <Label>Nome</Label>
                                    <Input
                                        name="xNome"
                                        value={freteData.transporta?.xNome || ""}
                                        onChange={handleFreteChange}
                                        placeholder="Nome do transportador"
                                    />
                                </div>
                                <div>
                                    <Label>Endereço</Label>
                                    <Input
                                        name="xEnder"
                                        value={freteData.transporta?.xEnder || ""}
                                        onChange={handleFreteChange}
                                        placeholder="Endereço completo"
                                    />
                                </div>
                                <div>
                                    <Label>Município</Label>
                                    <Input
                                        name="xMun"
                                        value={freteData.transporta?.xMun || ""}
                                        onChange={handleFreteChange}
                                        placeholder="Município"
                                    />
                                </div>
                                <div>
                                    <Label>UF</Label>
                                    <Select
                                        name="UF"
                                        placeholder="Selecione o estado"
                                        options={estadosUF}
                                        value={estadosUF.find((option) => option.value === freteData.transporta?.UF)}
                                        onChange={(selectedOption) => handleUFChange(selectedOption?.value || '')}
                                        styles={{
                                            control: (base) => ({
                                                ...base,
                                                borderRadius: '8px',
                                                padding: '2px 4px',
                                                borderColor: '#ccc',
                                            }),
                                        }}
                                    />
                                </div>
                                <div>
                                    <Label>Inscrição Estadual</Label>
                                    <Input
                                        name="IE"
                                        value={freteData.transporta?.IE || ""}
                                        onChange={handleFreteChange}
                                        placeholder="IE"
                                    />
                                </div>
                            </div>

                            <h3 style={{ marginTop: "1rem" }}>Volume Transportado</h3>
                            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "1rem" }}>
                                <div>
                                    <Label>Quantidade</Label>
                                    <Input
                                        type="number"
                                        name="qVol"
                                        value={freteData.volumes?.[0]?.qVol || 0}
                                        onChange={handleFreteChange}
                                    />
                                </div>
                                <div>
                                    <Label>Espécie</Label>
                                    <Input
                                        name="esp"
                                        value={freteData.volumes?.[0]?.esp || ""}
                                        onChange={handleFreteChange}
                                    />
                                </div>
                                <div>
                                    <Label>Marca</Label>
                                    <Input
                                        name="marca"
                                        value={freteData.volumes?.[0]?.marca || ""}
                                        onChange={handleFreteChange}
                                    />
                                </div>
                                <div>
                                    <Label>Numeração</Label>
                                    <Input
                                        name="nVol"
                                        value={freteData.volumes?.[0]?.nVol || ""}
                                        onChange={handleFreteChange}
                                    />
                                </div>
                                <div>
                                    <Label>Peso Líquido (kg)</Label>
                                    <Input
                                        type="number"
                                        name="pesoL"
                                        value={freteData.volumes?.[0]?.pesoL || 0}
                                        onChange={handleFreteChange}
                                    />
                                </div>
                                <div>
                                    <Label>Peso Bruto (kg)</Label>
                                    <Input
                                        type="number"
                                        name="pesoB"
                                        value={freteData.volumes?.[0]?.pesoB || 0}
                                        onChange={handleFreteChange}
                                    />
                                </div>
                            </div>

                            <div style={{ display: "flex", justifyContent: "flex-end", marginTop: "1rem", gap: "1rem" }}>
                                <Button onClick={() => setModalFreteOpen(false)}>Cancelar</Button>
                                <Button onClick={() => setModalFreteOpen(false)}>Salvar</Button>
                            </div>
                        </ModalContainer>
                    </ModalWrapper>
                )}
            </>
        </>
    );
};

export default VendaBalcao;
