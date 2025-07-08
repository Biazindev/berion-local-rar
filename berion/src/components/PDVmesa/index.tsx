import React, { useState, useEffect, useRef, useLayoutEffect } from 'react'
import { Link } from 'react-router-dom';
import InputMask from 'react-input-mask';
import Select from 'react-select';
import { GiHamburgerMenu } from "react-icons/gi";
import { HiMiniMagnifyingGlass } from "react-icons/hi2";
import { FixedSizeGrid as Grid } from 'react-window';
import { emitirNotaFiscal as handleClickNfe } from '../Utils/nfService';
import { emitirNotaFiscalServico as handleClickNfs } from '../Utils/nfsService';
import { NotaFiscalModal } from '../NotaFiscal/NotaFiscalModal'

import {
    Container,
    LeftPane,
    ProductList,
    RightPane,
    TableSelector,
    PdvButton,
    Wrapper,
    SwitchContainer,
    ToggleSwitch,
    Slider,
    Input,
    Title,
    Top,
    Legend,
    ImgContainer,
    Description,
    Icon,
    OrderList,
    TotaisContainer,
    ClienteInfoContainer,
    NameProduct
} from './styles';
import {
    ProdutoProps,
    useGetProdutosQuery,
    useLazyGetClientesByPhoneQuery,
    useAddVendaMutation,
    useCriarOuReutilizarMesaMutation,
    ClienteProps,
    useFinalizarMesaMutation,
    useAdicionarPedidoMutation,
    useSairParaEntregaMutation,
    useListarMesasAbertasQuery,
    useLazyGetItensMesaQuery,
    useLazyListarPedidosQuery,
    useLimparMesaMutation
} from '../../services/api';
import VendaBalcao from '../PDVbalcao';
import { ItemVenda } from '../../types';
import { number } from 'yup';
import Loader from '../Loader';
import { useSelector } from 'react-redux';
import { RootState } from '../../store/reducers';

export type VendaData = {
    emitirNotaFiscal?: boolean;
    vendaAnonima?: boolean;
    documentoCliente?: string | null;
    cliente?: any | null;
    clienteId?: number;
    emitenteId?: number | null;
    modelo?: string | null;
    itens?: ItemVenda[];
    pagamento?: Pagamento;
    dataVenda?: string;
    status?: string;
};

export interface ItemMesa {
    numeroParcelas: number;
    formPagamento: number;
    preco: string; // Alterado para string
    produtoId: number;
    nome: string;
    produto: {
        id: number;
        nome: string;
        precoUnitario: string; // Alterado para string
        totalItem: string
    };
    quantidade: number;
}

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
    tipoPessoa?: 'FISICA' | 'JURIDICA';
    pessoaFisica?: ClientePessoaFisica;
    pessoaJuridica?: ClientePessoaJuridica;
}

export interface ProdutoSelecionado {
    produtoId: number;
    id?: number;
    nome?: string;
    nomeProduto?: string;
    preco?: string; // Alterado para string
    precoUnitario?: string; // Alterado para string
    quantidade: number;
    formPagamento?: string;
    numeroParcelas?: number;
    totalItem?: string; // Alterado para string
}

export type Pagamento = {
    formaPagamento: string;
    valorPago: string; // Alterado para string
    valorRestante: string; // Alterado para string
    dataPagamento: string;
    status: string;
    numeroParcelas: number;
    totalVenda: string; // Alterado para string
    totalDesconto: string; // Alterado para string
    totalPagamento: string; // Alterado para string
};

export type MesaLocal = {
    numero: number;
    ocupada: boolean;
}

const VendaMesa: React.FC = () => {
    const [mesaAtual, setMesaAtual] = useState<number | null>(null);
    const [clienteBusca, setClienteBusca] = useState('');
    const [produtoBusca, setProdutoBusca] = useState('');
    const [vendasPorMesa, setVendasPorMesa] = useState<Record<number, VendaData>>({});
    const [addVenda, { isLoading: enviandoVenda, error: erroVenda }] = useAddVendaMutation();
    const [selectedValue, setSelectedValue] = useState<string | null>(null);
    const [selectedValuePag, setSelectedValuePag] = useState<string | null>(null);
    const inputRef = useRef<HTMLInputElement>(null);
    const [modalOpen, setModalOpen] = useState(false);

    const [produtosSelecionados, setProdutosSelecionados] = useState<ProdutoSelecionado[]>([]);
    const [dadosEntrega, setDadosEntrega] = useState<{ clienteBusca: string; produtosSelecionados: ProdutoSelecionado[] } | null>(null);
    const [addPedidoEntrega, { isLoading: enviandoPedido }] = useFinalizarMesaMutation();
    const [bloqueado, setBloqueado] = React.useState(false);
    const [criarOuReutilizarMesa] = useCriarOuReutilizarMesaMutation();
    const [adicionarPedido] = useAdicionarPedidoMutation();
    const [tipoAtendimento, setTipoAtendimento] = useState<'mesa' | 'entrega' | 'balcao'>('mesa');
    const [sairParaEntrega] = useSairParaEntregaMutation()
    const [getItensMesa] = useLazyGetItensMesaQuery();
    const [listarPedidos] = useLazyListarPedidosQuery();
    const [limpaMesa] = useLimparMesaMutation();
    const { data: mesasAbertas } = useListarMesasAbertasQuery();
    const vendaId = Number(localStorage.getItem('ultimaVendaId'));
    const emitente = useSelector((state: RootState) => state.empresa);
    const cliente = useSelector((state: RootState) => state.venda.cliente);
    const produto = useSelector((state: RootState) => state.venda.produtos);


    const [showNf, setShowNf] = useState(false);
    const formRef = useRef<HTMLDivElement>(null);


    const handleSwitchClick = () => {
        const nextState = !showNf;
        setShowNf(nextState);

        if (nextState) {
            setModalOpen(true);
        } else {
            localStorage.removeItem("tipoNotaFiscal");
        }
    };

    const handleTipoSelecionado = async (tipo: "NFS" | "NFE") => {
        setModalOpen(false);

        if (tipo === "NFS") {
            localStorage.setItem("tipoNotaFiscal", "NFS");
            await handleClickNfs(cliente, produto, emitente);
        } else if (tipo === "NFE") {
            localStorage.setItem("tipoNotaFiscal", "NFE");
            await handleClickNfe({
                vendaId,
                emitenteId: emitente.id,
            });
        }
    };

    useLayoutEffect(() => {
        if (showNf && formRef.current) {
            // Força o scroll assim que o DOM do form estiver pronto
            formRef.current.scrollIntoView({ behavior: 'smooth', block: 'start' });
        } else if (!showNf) {
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    }, [showNf]);


    const sanitizeNumber = (value: string | number) => {
        if (typeof value === 'string') {
            return parseFloat(value.replace(',', '.'));
        }
        return value;
    };

    const parsePreco = (preco: string | number): number => {
        if (typeof preco === 'number') {
            return preco;
        }
        return Number(preco.replace(',', '.').replace(/[^\d.-]/g, '')) || 0;
    };


    const numerosMesasAtivas = mesasAbertas?.map((mesa) => mesa.numero) ?? [];

    const [mesas, setMesas] = useState<MesaLocal[]>(
        () =>
            Array.from({ length: 20 }, (_, i) => ({
                numero: i + 1,
                ocupada: false,
            }))
    );

    type VendaData = {
        emitirNotaFiscal: boolean;
        vendaAnonima: boolean;
        documentoCliente: string | null;
        clienteId?: number;
        cliente: any | null;
        emitenteId: number | null;
        modelo: string | null;
        itens?: ItemVenda[];
        pagamento: Pagamento;
        dataVenda: string;
        status: string;
        clienteBusca?: string;
        produtosSelecionados?: ProdutoSelecionado[];
    };

    const formatarApenasNumeros = (valor: string) => valor.replace(/\D/g, '');

    // Função para formatar número para string com 2 casas decimais
    const formatPreco = (valor: number): string => {
        return valor.toFixed(2).replace('.', ',');
    };

    const formatPrecoBackend = (valor: number): string => {
        return valor.toFixed(2);
    };

    const gerarPayloadVenda = (
        clienteEncontrado: ClienteProps | null,
        produtosSelecionados: ProdutoSelecionado[],
        pagamento: Pagamento,
        somaProdutos: number,
        showNf: boolean,
    ): VendaData => {
        const agora = new Date().toISOString(); // Será convertido no backend para LocalDateTime

        return {
            emitirNotaFiscal: showNf,
            documentoCliente: clienteEncontrado?.documento || '',
            cliente: showNf ? clienteEncontrado : null,
            emitenteId: showNf ? 1 : null, // obrigatório só se for emitir nota
            modelo: showNf ? 'NFE' : null,
            itens: produtosSelecionados.map((p) => ({
                produtoId: p.produtoId,
                nomeProduto: p.nome,
                precoUnitario: sanitizeNumber(p.precoUnitario || '0'),
                quantidade: p.quantidade,
                totalItem: sanitizeNumber(p.totalItem || '0'),
            })),
            pagamento: {
                formaPagamento: selectedValuePag || 'DINHEIRO',
                valorPago: formatPrecoBackend(sanitizeNumber(somaProdutos)),
                valorRestante: formatPrecoBackend(0),
                dataPagamento: agora,
                status: 'PAGO',
                numeroParcelas: Number(selectedValue) || 1,
                totalVenda: formatPrecoBackend(sanitizeNumber(somaProdutos)),
                totalDesconto: formatPrecoBackend(sanitizeNumber(totalDesconto)),
                totalPagamento: formatPrecoBackend(sanitizeNumber(somaProdutos)),
            },
            dataVenda: agora,
            status: 'EM_PREPARO',
            vendaAnonima: !clienteEncontrado,
        };
    };

    const pagamento: Pagamento = {
        formaPagamento: '',
        valorPago: '0,00',
        valorRestante: '0,00',
        dataPagamento: '',
        status: '',
        numeroParcelas: 0,
        totalVenda: '0,00',
        totalDesconto: '0,00',
        totalPagamento: '0,00',
    }

    const { data: produtos = [], isLoading, error } = useGetProdutosQuery();
    const [buscarCliente, { data: clienteEncontrado, isFetching: buscandoCliente, error: erroCliente }] =
        useLazyGetClientesByPhoneQuery();
    const [totalDesconto, setTotalDesconto] = React.useState(pagamento?.totalDesconto || '0,00');

    useEffect(() => {
        inputRef.current?.focus();
    }, []);

    useEffect(() => {
        adicionarPedido
        const delayDebounce = setTimeout(() => {
            if (clienteBusca.trim().length >= 3) {
                buscarCliente(clienteBusca);
            }
        }, 500);

        return () => clearTimeout(delayDebounce);
    }, [clienteBusca, buscarCliente]);

    useEffect(() => {
        if (mesaAtual !== null) {
            const dadosMesa = vendasPorMesa[mesaAtual];
            setClienteBusca(dadosMesa?.clienteBusca || '');
            setProdutosSelecionados(dadosMesa?.produtosSelecionados || []);
        }
    }, [mesaAtual, vendasPorMesa]);

    useEffect(() => {
        const carregarDadosMesa = async () => {
            if (mesaAtual !== null) {
                try {
                    const itensResponse = await getItensMesa(mesaAtual).unwrap();

                    const produtosMapeados = itensResponse.map((item: any) => {
                        const produtoId = item.produtoId;
                        const nome = item.nome || item.produto?.nome || item.nomeProduto;
                        const precoUnitario = item.precoUnitario || item.produto?.precoUnitario || item.preco || '0';
                        const preco = item.preco || precoUnitario;
                        const quantidade = item.quantidade || 1;
                        const formPagamento = item.formPagamento?.toString() || 'DINHEIRO';
                        const numeroParcelas = item.numeroParcelas || 1;

                        return {
                            produtoId,
                            id: produtoId,
                            nome,
                            precoUnitario,
                            preco,
                            quantidade,
                            formPagamento,
                            numeroParcelas,
                            totalItem: formatPreco(parsePreco(preco) * quantidade)
                        } as unknown as ProdutoSelecionado;
                    });

                    setProdutosSelecionados(produtosMapeados);

                    const pedidosResponse = await listarPedidos({ mesaId: mesaAtual }).unwrap();
                    const ultimoPedido = Array.isArray(pedidosResponse)
                        ? pedidosResponse.at(-1)
                        : undefined;

                    if (ultimoPedido?.cliente) {
                        if (typeof ultimoPedido.cliente === 'string') {
                            setClienteBusca(ultimoPedido.cliente);
                        } else {
                            const cliente = ultimoPedido.cliente as Cliente;
                            const documento = cliente.tipoPessoa === 'FISICA'
                                ? cliente.pessoaFisica?.cpf
                                : cliente.pessoaJuridica?.cnpj;

                            if (documento) {
                                setClienteBusca(documento);
                            }
                        }
                    }

                } catch (error) {
                    console.error('Erro ao buscar dados da mesa:', error);
                }
            }
        };

        carregarDadosMesa();
    }, [mesaAtual, getItensMesa, listarPedidos]);

    useEffect(() => {
        const carregarItensMesa = async () => {
            if (mesaAtual !== null) {
                try {
                    const itensResponse = await getItensMesa(mesaAtual).unwrap() as unknown as ItemMesa[];
                    const produtosMapeados = itensResponse.map((item) => ({
                        produtoId: item.produtoId,
                        id: item.produtoId,
                        nome: item.nome,
                        precoUnitario: item.preco,
                        quantidade: item.quantidade,
                        formPagamento: item.formPagamento.toString(),
                        numeroParcelas: item.numeroParcelas,
                    }));
                    setProdutosSelecionados(produtosSelecionados);
                } catch (error) {
                    console.error('Erro ao carregar itens da mesa:', error);
                }
            }
        };

        carregarItensMesa();
    }, [mesaAtual, getItensMesa]);

    const salvarDadosMesaAtual = () => {
        if (mesaAtual !== null) {
            const payload = gerarPayloadVenda(
                clienteEncontrado ?? null,
                produtosSelecionados,
                pagamento,
                parsePreco(somaProdutos.toString()),
                showNf
            );

            setVendasPorMesa((prev) => ({
                ...prev,
                [mesaAtual]: {
                    ...payload,
                    clienteBusca,
                    produtosSelecionados,
                },
            }));
        }
    };

    const handleSelecionarMesa = async (mesa: number) => {
        if (mesa !== mesaAtual) {
            salvarDadosMesaAtual();
            setMesaAtual(mesa);
        }

        try {
            await criarOuReutilizarMesa(mesa).unwrap();

            const itensResponse = await getItensMesa(mesa).unwrap();

            const produtosMapeados = itensResponse.map((item: any) => ({
                produtoId: item.produtoId || item.id,
                id: item.produtoId || item.id,
                nome: item.nome || item.nomeProduto,
                precoUnitario: item.preco || item.precoUnitario || '0',
                quantidade: item.quantidade || 1,
                formPagamento: item.formPagamento?.toString() || 'DINHEIRO',
                numeroParcelas: item.numeroParcelas || 1,
                mensagem: '',
                descricao: '',
                ncm: '',
                EAN: '',
                CEST: '',
                unidade: 'UN',
                precoCompra: '0',
                estoqueAtual: 0
            }));

            setProdutosSelecionados(produtosMapeados);

            const pedidosResponse = await listarPedidos({ mesaId: mesa }).unwrap();
            const ultimoPedido = Array.isArray(pedidosResponse) ? pedidosResponse.at(-1) : null;

            if (ultimoPedido?.cliente) {
                if (typeof ultimoPedido.cliente === 'string') {
                    setClienteBusca(ultimoPedido.cliente);
                } else {
                    const cliente = ultimoPedido.cliente as Cliente;
                    const documento = cliente.tipoPessoa === 'FISICA'
                        ? cliente.pessoaFisica?.cpf
                        : cliente.pessoaJuridica?.cnpj;

                    if (documento) {
                        setClienteBusca(documento);
                    }
                }
            }
        } catch (error) {
            console.error('Erro ao selecionar mesa:', error);
            alert('Ocorreu um erro ao selecionar a mesa. Por favor, tente novamente.');
        }
    }

    const somaProdutos = produtosSelecionados.reduce((total, p) => {
        const preco = sanitizeNumber(p.precoUnitario || '0');
        const quantidade = sanitizeNumber(p.quantidade || 1);
        return total + preco * quantidade;
    }, 0);

    const handleFinalizarVenda = async () => {
        if (mesaAtual === null) {
            alert('Selecione uma mesa antes de finalizar a venda.');
            return;
        }

        // Criar objeto com todos os dados da venda
        const vendaCompleta = {
            cliente: clienteEncontrado,
            produtos: produtosSelecionados,
            mesa: mesaAtual,
            pagamento,
            total: somaProdutos,
            data: new Date().toISOString(),
            nf: showNf
        };

        // Salvar no localStorage
        try {
            // 1. Salvar dados completos da venda atual
            localStorage.setItem('ultimaVenda', JSON.stringify(vendaCompleta));

            // 2. Adicionar ao histórico de vendas (opcional)
            const historicoVendas = JSON.parse(localStorage.getItem('historicoVendas') || '[]');
            historicoVendas.push(vendaCompleta);
            localStorage.setItem('historicoVendas', JSON.stringify(historicoVendas));

            // 3. Salvar individualmente para fácil recuperação
            localStorage.setItem('clienteVenda', JSON.stringify(clienteEncontrado));
            localStorage.setItem('produtosVenda', JSON.stringify(produtosSelecionados));
            localStorage.setItem('infoPagamento', JSON.stringify(pagamento));

            // Gerar e enviar payload para API
            const payload = gerarPayloadVenda(
                clienteEncontrado ?? null,
                produtosSelecionados,
                pagamento,
                somaProdutos,
                showNf,
            );

            await addVenda(payload).unwrap();

            alert('Venda finalizada e salva localmente com sucesso!');

            // setClienteEncontrado(null);
            // setProdutosSelecionados([]);
            // setPagamento(/* estado inicial */);

        } catch (error) {
            console.error('Erro ao processar venda:', error);
            alert('Erro ao finalizar venda. Os dados foram salvos localmente para recuperação.');
        }
    };
    const limparEstado = async () => {
        if (mesaAtual !== null) {
            try {
                await limpaMesa(mesaAtual).unwrap();
            } catch (error) {
                console.error("Erro ao limpar mesa no backend:", error);
            }

            setVendasPorMesa((prev) => {
                const copy = { ...prev };
                delete copy[mesaAtual];
                return copy;
            });
        }

        setClienteBusca('');
        setProdutosSelecionados([]);
    };

    const handleRemoverProduto = (indexToRemove: number) => {
        setProdutosSelecionados((prev) => prev.filter((_, i) => i !== indexToRemove));
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
            const existente = prev.find(p => p.produtoId === produto.id);

            if (existente) {
                const novaQuantidade = existente.quantidade + 1;
                const preco = sanitizeNumber(existente.precoUnitario ?? "0");
                const novoTotal = preco * novaQuantidade;

                return prev.map(p =>
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
                numeroMesa: mesaAtual!,
                itens: [{
                    produtoId: produto.id,
                    quantidade: 1,
                    observacao: "",
                }],
            }).unwrap();
        } catch (err) {
            console.error('Erro ao adicionar pedido:', err);
        }
    };


    const renderTableInfo = (orderData: any) => {
        if (orderData?.type === 'mesa' && orderData?.table?.tableNumber) {
            return <h3>Mesa {orderData.table.tableNumber}</h3>;
        }
        return null;
    };

    const opcoesPagamento = [
        { value: 'PIX', label: 'Pix' },
        { value: 'DINHEIRO', label: 'Dinheiro' },
        { value: 'CARTAO_CREDITO', label: 'Cartão de Crédito' },
        { value: 'CARTAO_DEBITO', label: 'Cartão de Débito' },
        { value: 'PARCELADO_LOJA', label: 'Parcelado Loja' },
        { value: 'CARTAO', label: 'Cartão Genérico' }
    ]

    const parcelas = [
        { value: '1', label: '1x' },
        { value: '2', label: '2x' },
        { value: '3', label: '3x' },
        { value: '4', label: '4x' },
        { value: '5', label: '5x' },
        { value: '6', label: '6x' }
    ]

    const descontoNumerico = parsePreco(totalDesconto);
    const totalComDesconto = somaProdutos - (isNaN(descontoNumerico) ? 0 : descontoNumerico);

    const formatarTelefone = (telefone: string) => {
        if (!telefone) return '';
        const numeros = telefone.replace(/\D/g, '');
        if (numeros.length === 11) {
            return `(${numeros.slice(0, 2)}) ${numeros.slice(2, 7)}-${numeros.slice(7)}`;
        }
        return telefone;
    }

    const formatarCpf = (cpf: string) => {
        if (!cpf) return '';
        const numeros = cpf.replace(/\D/g, '');
        if (numeros.length === 11) {
            return `${numeros.slice(0, 3)}.${numeros.slice(3, 6)}.${numeros.slice(6, 9)}-${numeros.slice(9)}`;
        }
        return cpf;
    }

    function limitarTexto(texto: string, limite: number = 17): string {
        return texto.length > limite ? texto.substring(0, limite) + '...' : texto;
    }

    function limitar(texto: string, limite: number = 30): string {
        return texto.length > limite ? texto.substring(0, limite) + '...' : texto;
    }


    return (
        <>
            <div style={{ margin: '32px 0', display: 'flex', justifyContent: 'center', alignItems: 'center', width: '100%' }}>
                <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '1rem', width: '320px', height: '36px', backgroundColor: '#ccc', borderRadius: '24px' }}>
                    <button
                        onClick={() => setTipoAtendimento('mesa')}
                        style={{
                            width: '160px',
                            backgroundColor: tipoAtendimento === 'mesa' ? '#5f27cd' : '#ccc',
                            color: 'white',
                            border: 'none',
                            borderRadius: '24px',
                            cursor: 'pointer',
                            fontSize: '18px',
                            fontWeight: 'bold'
                        }}
                    >
                        Mesa
                    </button>
                    <button
                        onClick={() => setTipoAtendimento('balcao')}
                        style={{
                            width: '160px',
                            backgroundColor: tipoAtendimento === 'balcao' ? '#5f27cd' : '#ccc',
                            color: 'white',
                            border: 'none',
                            borderRadius: '24px',
                            cursor: 'pointer',
                            fontSize: '18px',
                            fontWeight: 'bold'
                        }}
                    >
                        Balcão/Entrega
                    </button>
                </div>
            </div>
            <div style={{ padding: '1rem' }}>
                <div>
                    {renderTableInfo(VendaMesa)}
                </div>
                <div style={{
                    display: 'flex',
                    justifyContent: 'end',
                    width: '100%',
                    margin: '0 auto'
                }}><div style={{
                    borderRadius: '8px',
                    display: 'flex',
                    justifyContent: 'center',
                    fontSize: '28px',
                    alignItems: 'center'
                }}>
                    </div>
                </div>
                {tipoAtendimento === 'balcao' && <VendaBalcao />}
            </div>
            {tipoAtendimento === 'mesa' && (
                <>
                    <Top>
                    </Top>
                    <Container>
                        <LeftPane>
                            <div>
                                <Legend>Mesas vazias<span><div></div></span> Mesas ocupadas <span><div className='container'></div></span></Legend>
                                <div className='container'><span><GiHamburgerMenu /></span><h3>Mesas</h3></div>
                            </div>
                            <TableSelector style={{ padding: '8px' }}>
                                {mesas.map((mesa) => (
                                    <button
                                        key={mesa.numero}
                                        onClick={() => handleSelecionarMesa(mesa.numero)}
                                        style={{
                                            backgroundColor:
                                                mesa.numero === mesaAtual
                                                    ? 'rgba(204, 204, 204, 0.4)'
                                                    : numerosMesasAtivas!.includes(mesa.numero)
                                                        ? '#218c74'
                                                        : 'rgba(255, 82, 82, 0.3)',
                                            color: '#fff',
                                            border: '1px solid rgba(255, 255, 255, 0.3)',
                                            borderRadius: '12px',
                                            backdropFilter: 'blur(8px)',
                                            WebkitBackdropFilter: 'blur(8px)',
                                            boxShadow: '0 4px 10px rgba(0, 0, 0, 0.2)',
                                            padding: '1rem',
                                            transition: 'all 0.3s ease',
                                        }}
                                    >
                                        {mesa.numero}
                                    </button>
                                ))}
                            </TableSelector>
                            <div>
                                <InputMask
                                    mask="(99) 99999-9999"
                                    value={(clienteBusca)}
                                    onChange={(e) => setClienteBusca(formatarApenasNumeros(e.target.value))}
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
                                {clienteBusca.trim().length >= 3 && !buscandoCliente && (
                                    clienteEncontrado ? (
                                        <ClienteInfoContainer>
                                            <div className="info">
                                                <p><strong>Nome:</strong> {clienteEncontrado?.pessoaFisica?.nome}</p>
                                                <p><strong>CPF:</strong> {formatarCpf(clienteEncontrado?.pessoaFisica?.cpf || '')}</p>
                                                <p><strong>Telefone:</strong> {formatarTelefone(clienteEncontrado?.pessoaFisica?.telefone || '')}</p>
                                            </div>

                                            <div className="pagamento">
                                                <div>
                                                    <label>Forma de Pagamento</label>
                                                    <Select
                                                        options={opcoesPagamento}
                                                        value={opcoesPagamento.find(op => op.value === selectedValue)}
                                                        onChange={option => setSelectedValuePag(option ? option.value : null)}
                                                        placeholder="Selecione uma forma de pagamento"
                                                    />
                                                </div>

                                                <div>
                                                    <label>Parcelamento</label>
                                                    <Select
                                                        options={parcelas}
                                                        value={parcelas.find(op => op.value === selectedValue)}
                                                        onChange={option => setSelectedValue(option ? option.value : null)}
                                                        placeholder="Selecione parcelamento"
                                                    />
                                                </div>

                                                <div>
                                                    <label>Desconto</label>
                                                    <Input
                                                        type="text"
                                                        value={totalDesconto}
                                                        placeholder="Valor desconto"
                                                        onChange={e => setTotalDesconto(e.target.value)}
                                                    />
                                                </div>
                                            </div>
                                        </ClienteInfoContainer>
                                    ) : (
                                        <p>Nenhum cliente encontrado.</p>
                                    )
                                )}
                            </div>
                            <Link to={'/cadastro-clientes'}>
                                <PdvButton>Cadastrar Cliente</PdvButton>
                            </Link>
                            <label>
                                <input type="checkbox" checked={showNf} onChange={handleSwitchClick} />
                                Emitir Nota Fiscal
                            </label>
                            <OrderList>
                                <h4>Produtos Selecionados:</h4>
                                <ul>
                                    {produtosSelecionados.map((produto, index) => {
                                        const id = produto.id ?? produto.produtoId ?? 'N/A';
                                        const nome = produto.nome ?? 'Sem nome';
                                        const preco = produto.precoUnitario ?? '0';
                                        const quantidade = produto.quantidade ?? 0;
                                        const total = formatPreco(parsePreco(preco) * quantidade);

                                        return (
                                            <li key={index}>
                                                <div className="produto-info">
                                                    <div><strong>{nome}</strong></div>
                                                    <span>ID: {id}</span>
                                                    <div>
                                                        R$ {Number(parsePreco(preco)).toFixed(2)} x {quantidade}<strong>
                                                        </strong>
                                                    </div>

                                                </div>
                                                <button title='remover' className="remover" onClick={() => handleRemoverProduto(index)}>×</button>
                                            </li>
                                        );
                                    })}
                                </ul>
                            </OrderList>
                            <TotaisContainer>
                                <div className="linha">
                                    <strong>Total:</strong> <span>R$ {somaProdutos.toFixed(2)}</span>
                                </div>
                                <div className="linha">
                                    <strong>Desconto:</strong> <span>R$ {descontoNumerico.toFixed(2)}</span>
                                </div>
                                <div className="linha total-com-desconto">
                                    <strong>Total com desconto:</strong> <span>R$ {totalComDesconto.toFixed(2)}</span>
                                </div>
                            </TotaisContainer>
                            <PdvButton onClick={handleFinalizarVenda} disabled={enviandoVenda}>
                                {enviandoVenda ? 'Enviando...' : 'Finalizar Venda'}
                            </PdvButton>
                            <PdvButton onClick={limparEstado}>Limpar mesa</PdvButton>
                        </LeftPane>

                        <RightPane>
                            <div style={{ display: 'flex', justifyContent: 'start', alignItems: 'center' }}>
                                {/* <h2><span></span>Catálogos de produtos</h2> */}
                                <HiMiniMagnifyingGlass style={{ color: '#ccc', fontSize: '28px', position: 'relative', left: '32px' }} />
                                <div style={{ width: '300px' }}>
                                    <Input
                                        ref={inputRef}
                                        style={{ textAlign: 'right' }}
                                        type="text"
                                        placeholder="Buscar produto"
                                        value={produtoBusca}
                                        onChange={(e: { target: { value: React.SetStateAction<string>; }; }) => setProdutoBusca(e.target.value)}
                                    />
                                </div>
                                <div style={{ display: 'flex', marginLeft: '12rem' }}>
                                    <Link to={'/produtos-cadastrar'}>
                                        <div>
                                            <PdvButton style={{ marginRight: '24px' }}>Cadastrar produtos</PdvButton>
                                        </div>
                                    </Link>
                                </div>
                            </div>
                            <div>
                                {isLoading && <p><Loader /></p>}
                                {Boolean(error) && <p>Erro ao carregar produtos.</p>}

                                {!isLoading && !error && (
                                    <ProductList>
                                        {produtos
                                            .filter((produto: ProdutoProps) =>
                                                produto.nome.toLowerCase().includes(produtoBusca.toLowerCase())
                                            )
                                            .map((produto: ProdutoProps) => (
                                                <div onClick={() => handleAdicionarProduto(produto)}>
                                                    <ImgContainer>
                                                        {produto.imagem && (
                                                            <img
                                                                src={produto.imagem.startsWith("data:") ? produto.imagem : produto.imagem}
                                                                alt="Produto"
                                                                loading="lazy"
                                                                style={{ width: "100%", height: "120px", objectFit: "contain" }}
                                                            />
                                                        )}

                                                    </ImgContainer>
                                                    <NameProduct>
                                                        {limitarTexto(produto.nome)}
                                                    </NameProduct>
                                                    <Description>
                                                        <div>
                                                            <p>{limitar(produto.descricao ?? '')}</p>
                                                        </div>
                                                    </Description>
                                                    <span>R$ {parseFloat(produto.precoUnitario)?.toFixed(2)}{' '}</span>
                                                    <Icon><span>+</span></Icon>
                                                </div>
                                            ))}
                                    </ProductList>
                                )}
                            </div>
                        </RightPane>
                    </Container>
                </>
            )}
            <label>
                <input type="checkbox" checked={showNf} onChange={handleSwitchClick} />
                Emitir Nota Fiscal
            </label>

            {/* modal */}
            <NotaFiscalModal
                isOpen={modalOpen}
                onClose={() => {
                    setModalOpen(false);
                    setShowNf(false);
                }}
                onSelect={handleTipoSelecionado}
            />


        </>
    );
}

export default VendaMesa;