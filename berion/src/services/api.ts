import { VendaProps } from '../components/SaleList/index'
import { Usuario } from '../../src/components/User/User'
import {
  createApi,
  fetchBaseQuery,
  BaseQueryFn,
  FetchArgs,
} from '@reduxjs/toolkit/query/react'
import { Mutex } from 'async-mutex'
import { EmitirNotaPayload } from '../components/Venda/types'
import { OrdemServicoDTO } from '../Enum/enum'
import { Cliente, ItemMesa, ProdutoSelecionado } from '../components/PDVmesa/index'
import { NFe } from './types'
import { NotaFiscal } from '../components/TaxEnvironment/index'

export interface ForgotPasswordRequest { email: string }
export interface ResetPasswordRequest { token: string; newPassword: string }
export interface Endereco {
  cep: string; bairro: string; municipio: string; logradouro: string; numero: string; uf: string; complemento?: string
}


type DocumentoTipo = 'cpf' | 'cnpj';
type DocumentoParam = {
  tipo: DocumentoTipo;
  numero: string;
};

export interface EmitirNotaRequestDTO {
  vendaId: number;
  emitenteId: number;
}

export interface EmitirNfPayload {
  vendaId: number;
  emitenteId: number;
}

export interface EmitirNfeRequestDTO {
  // Defina aqui as propriedades conforme seu backend
}

export interface emitente {
  razaoSocial: string;
  nomeFantasia: string;
  cnpj: string;
  inscricaoEstadual: string;
  inscricaoMunicipal: string;
  regimeTributario: string;
  cnae: string;
  crt: number; // 1: Simples Nacional, 2: Simples Nacional - excesso, 3: Regime Normal
  fone: string;
  endereco: Endereco;
}

export interface Empresa {
  id: number;
  razaoSocial: string;
  nomeFantasia: string;
  cnpj: string;
  inscricaoEstadual: string;
  inscricaoMunicipal: string;
  regimeTributario: string;
  cnae: string;
  crt: number; // 1: Simples Nacional, 2: Simples Nacional - excesso, 3: Regime Normal
  fone: string;
  emitente: emitente;
  endereco: Endereco;
}


export interface NotaFiscalResponseDTO {
  id: string;
  status: string;
  cnpjEmitente: string;
  numero: string;
  serie: string;
  valorTotal: number;
  // ... outros campos
}

type ImportarProdutosResponse = {
  success: boolean;
  importedCount: number;
  totalCount: number;
  message: string;
};

export interface PedidoEntregaDTO {
  pago?: any
  id?: number
  cliente?: Cliente
  total?: string | number | null | undefined
  pedidoId?: number;
  cliente_id?: number | null;
  fone: string;
  enderecoEntrega: Endereco;
  observacao?: string;
  nomeMotoboy?: string;
  precisaTroco?: boolean;
  trocoPara?: number;
  status?: 'EM_PREPARO' | 'SAIU_PARA_ENTREGA' | 'ENTREGUE' | 'CANCELADO';
  produtos?: ProdutoProps[];
}

interface DailyData {
  label: string;
  totais: {
    [key: string]: number;
  };
}

export interface Mesa {
  id: number;
  numero: number;
  aberta: boolean;
  // você pode adicionar mais campos que existam na sua entidade Mesa
}

export type TotaisAno = {
  label: string;
  totais?: {
    [key: string]: number;
  };
};

export interface Pedido {
  cliente: string;
  id: number;
  mesaId: number;
  status: StatusPedido;
  itens: PedidoItem[];
  // adicione outros campos conforme seu modelo
}

export interface PedidoItem {
  id?: number
  produtoId: number;
  quantidade: number;
  nomeProduto?: string;
  precoUnitario?: string | number
  totalItem?: string | number
  observacao?: string
  produto?: ProdutoSelecionado[]
}

export interface PedidoMesaDTO {
  numeroMesa?: number;
  itens: PedidoItem[];
}

export interface ItemMesaDTO {
  produtoId: number;
  nomeProduto: string;
  quantidade: number;
  precoUnitario: number
  totalItem: number;
}

export enum StatusPedido {
  ABERTO = 'ABERTO',
  CONCLUIDO = 'CONCLUIDO',
  CANCELADO = 'CANCELADO',
}

export interface PessoaFisica {
  nome?: string;
  cpf?: string;
  email?: string;
  telefone?: string;
  dataNascimento: string;
  endereco?: Endereco;
}

export interface PedidoPayload {
  itens: {
    produtoId: number;
    quantidade: number;
    fone: string;
  }[];
}


export interface Endereco {
  cep: string;
  bairro: string;
  municipio: string;
  logradouro: string;
  numero: string;
  uf: string;
  complemento?: string;
}


export interface PessoaJuridica {
  cnpj: string;
  razaoSocial: string;
  nomeFantasia: string;
  situacao: string;
  tipo: string;
  naturezaJuridica: string;
  porte: string;
  dataAbertura: string;
  ultimaAtualizacao: string | null;
  atividadesPrincipais: Atividade[];
  atividadesSecundarias: Atividade[];
  socios: Socio[];
  endereco: Endereco;
  simples?: SimplesNacional;
  telefone?: string;
  inscricaoEstadual: string;
  capitalSocial: number;
  email: string;
}



export interface Atividade {
  codigo: string;
  descricao: string;
}

export interface Socio {
  nome: string;
  qualificacao: string;
  cpf: string;
}

export interface SimplesNacional {
  optante: boolean;
  mei?: boolean;
  dataExclusao?: string | null;
  ultimaAtualizacao?: string | null;
}


export interface Simples {
  simples: boolean;
}

export interface Endereco {
  cep: string;
  bairro: string;
  municipio: string;
  logradouro: string;
  numero: string;
  uf: string;
  complemento?: string;
}


export interface ClienteProps {
  dataClientes?: string | number | Date
  tipoPessoa: string;
  cliente: void | ClienteProps;
  clienteId: number;
  id?: number;
  nome: string;
  cpf?: string;
  documento?: string;
  cnpj?: string;
  email?: string;
  telefone?: string;
  endereco?: Endereco;
  dataNascimento: string;
  razaoSocial?: string;
  pessoaFisica?: PessoaFisica;
  pessoaJuridica?: PessoaJuridica;
  municipio: string
}

export interface CreateClienteRequest {
  tipoPessoa?: 'FISICA' | 'JURIDICA';
  pessoaFisica?: PessoaFisica | null;
  pessoaJuridica?: PessoaJuridica | null;
}

export interface UpdateClienteRequest extends CreateClienteRequest {
  id: number;
}

export type ImpostoProdutoProps = {
  tipoImposto: string;
  aliquota: number;
  cst: string;
  origem: string;
  csosn: string;
  valorFrete?: number;
  valorSeguro?: number;
  desconto?: number;
  pis?: number;
  cofins?: number;
};

export type ProdutoProps = {
  id: number;
  nome: string;
  descricao: string;
  precoUnitario: string;
  precoCusto: number;
  ean: string;
  categoriaId: number;
  ncm: string;
  dataVencimento: string;
  ativo: boolean;
  quantidade: string;
  observacao?: string;
  imagem: string | null;
  cfop: string;
  unidade: string;
  valorUnitarioComercial: number;
  valorUnitarioDesconto: number;
  valorUnitarioTotal: number;
  valorUnitarioTributavel: number;
  brand?: any
  code?: any;
  gtin?: any
  thumbnail?: string | null
  max_price?: any
  category?: any
  description?: string
  produtoId: any
  preco?: any
  codigoBarras?: string | undefined
  mensagem?: string
  impostos: ImpostoProdutoProps[];
}
export interface LoginRequest { username: string; password: string, accessToken: string }
export interface LoginResponse {
  id: number;
  accessToken: string;
}
export type ProdutoPayload = Omit<ProdutoProps, 'id' | 'produtoId'>;
const mutex = new Mutex()

const baseQuery = fetchBaseQuery({
  baseUrl: 'http://localhost:8080',
  credentials: 'include',
  prepareHeaders: (headers) => {
    const token = localStorage.getItem('ACCESS_TOKEN');
    if (token) {
      headers.set('Authorization', `Bearer ${token}`);
    }
    headers.set('SIMPLIFICA-API-KEY', 'biaza');

    return headers;
  }

})

export const baseQueryWithReauth: BaseQueryFn<
  string | FetchArgs,
  unknown,
  unknown
> = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions)

  if (result.error && (result.error as any).status === 401) {
    console.log('401, tentando refresh…')
    const refreshResult = await baseQuery(
      { url: '/auth/refresh-token', method: 'POST' } as FetchArgs,
      api,
      extraOptions
    )
    if (refreshResult.data) {
      const newToken = (refreshResult.data as any).accessToken
      localStorage.setItem('ACCESS_TOKEN', newToken)

      result = await baseQuery(args, api, extraOptions)
    } else {
      api.dispatch({ type: 'auth/logout' })
    }
  }

  return result
}

export const api = createApi({
  reducerPath: 'api',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Auth',
    'Produto',
    'Venda',
    'Cliente',
    'Filial',
    'Mesa',
    'Pedido',
    'OrdemServico',
    'atualizarOrdem',
    'PedidosEntrega',
    'Empresas',
    'NFe'
  ],
  endpoints: (builder) => ({
    login: builder.mutation<LoginResponse, LoginRequest>({
      query: (creds) => ({
        url: '/auth/login',
        method: 'POST',
        body: creds
      })
    }),

    forgotPassword: builder.mutation<{ message: string }, ForgotPasswordRequest>({
      query: (body) => ({
        url: '/help/forgot-password',
        method: 'POST',
        body
      })
    }),

    resetPassword: builder.mutation<{ message: string }, ResetPasswordRequest>({
      query: (body) => ({
        url: '/help/reset-password',
        method: 'POST',
        body,
        responseHandler: async (response) => response.text(),
      })
    }),
    criarUsuario: builder.mutation<string, Usuario>({
      query: (usuario) => ({
        url: 'usuario',
        method: 'POST',
        body: usuario,
        responseHandler: async (res) => res.text(),
      }),
    }),
    buscarUsuario: builder.query<Usuario[], void>({
      query: () => ({
        url: '/usuario/listar',
        method: 'GET',
      }),
    }),
    buscarUsuarioPorId: builder.query<Usuario, number>({
      query: (id) => ({
        url: `/usuario/${id}`,
        method: 'GET',
      }),
    }),
    logout: builder.mutation<void, void>({
      query: () => ({
        url: '/auth/logout',
        method: 'POST',
        responseHandler: (response) => response.text(),
      }),
    }),
    addProduto: builder.mutation<ProdutoProps, Partial<ProdutoProps>>({
      query: (novoProduto) => ({
        url: '/produtos',
        method: 'POST',
        body: novoProduto
      }),
      invalidatesTags: ['Produto']
    }),
    getProdutos: builder.query<ProdutoProps[], void>({
      query: () => '/produtos',
      providesTags: ['Produto']
    }),
    getProdutoById: builder.query<ProdutoProps, number>({
      query: (id) => `produtos/${id}`,
      providesTags: (result, error, id) => [{ type: 'Produto', id }],
    }),
    getProdutosByName: builder.query<ProdutoProps[], string>({
      query: (nome) => `/produtos/buscar/nome?nome=${encodeURIComponent(nome)}`,
      providesTags: ['Produto']
    }),
    searchProdutos: builder.query<ProdutoProps[], string>({
      query: (searchTerm) => `/produtos?search=${searchTerm}`,
      providesTags: ['Produto']
    }),
    updateProduto: builder.mutation<ProdutoProps, { id: number; data: ProdutoPayload }>({
      query: ({ id, data }) => ({
        url: `/produtos/${id}`,
        method: 'PUT',
        body: data,
      }),
    }),
    deleteProduto: builder.mutation<{ success: boolean; id: number }, number>({
      query: (id) => ({
        url: `/produtos/${id}`,
        method: 'DELETE'
      }),
      invalidatesTags: ['Produto']
    }),
    getVendas: builder.query<VendaProps[], { inicio?: string; fim?: string } | void>({
      query: (params) => {
        const { inicio, fim } = params || {};
        return {
          url: '/venda',
          params: inicio && fim ? { inicio, fim } : undefined,
        };
      },
      providesTags: ['Venda'],
    }),

    getTotalDia: builder.query<number, void>({
      query: () => '/venda/totais-semana',
      providesTags: ['Venda'],
    }),
    getTotalDiaSing: builder.query<number, void>({
      query: () => 'venda/totais-diario',
      providesTags: ['Venda'],
    }),
    getTotalDiaSingle: builder.query<DailyData[], void>({
      query: () => 'venda/total-hoje',
      providesTags: ['Venda'],
    }),
    getTotalSemana: builder.query<number, void>({
      query: () => '/venda/total-semana-atual',
      providesTags: ['Venda'],
    }),
    getTotalSemanas: builder.query<number, void>({
      query: () => 'venda/totais-semanais',
      providesTags: ['Venda'],
    }),

    getTotalMes: builder.query<number, void>({
      query: () => '/venda/total-mes-atual',
      providesTags: ['Venda'],
    }),
    getTotalMeses: builder.query<number, void>({
      query: () => '/venda/totais-mensais',
      providesTags: ['Venda'],
    }),
    getTotalAno: builder.query<TotaisAno[], void>({
      query: () => '/venda/total-ano',
      providesTags: ['Venda'],
    }),
    getTotalAnoAtual: builder.query<TotaisAno[], void>({
      query: () => '/venda/total-ano-atual',
      providesTags: ['Venda'],
    }),
    getVendasPorPeriodo: builder.query<VendaProps[], { inicio: string; fim: string }>({
      query: ({ inicio, fim }) => `venda/vendas-por-periodo?inicio=${inicio}&fim=${fim}`,
    }),
    getTotalPorPeriodo: builder.query<number, { inicio: string; fim: string }>({
      query: ({ inicio, fim }) => `venda/total-por-periodo?inicio=${inicio}&fim=${fim}`,
    }),
    addVenda: builder.mutation<
      {
        clienteId: Number; id: number; vendaId: number; mensagem: string; reciboUrl: string
      },
      any
    >({
      query: (venda) => ({
        url: '/venda/finalizar',
        method: 'POST',
        body: venda,
      }),
      transformResponse: (response: any) => {
        return {
          clienteId: response.clienteId,
          id: response.id,
          vendaId: response.vendaId,
          mensagem: response.mensagem,
          reciboUrl: response.reciboUrl,
        };
      },
      invalidatesTags: ['Venda'],
    }),

    updateVenda: builder.mutation<VendaProps, VendaProps>({
      query: (venda) => ({
        url: `'/venda'${venda.id}`,
        method: 'PUT',
        body: venda
      }),
      invalidatesTags: ['Venda']
    }),
    deleteVenda: builder.mutation<{ success: boolean; id: number }, number>({
      query: (id) => ({
        url: `/venda${id}`,
        method: 'DELETE'
      }),
      invalidatesTags: ['Venda']
    }),

    getClientes: builder.query<ClienteProps[], void>({
      query: () => '/clientes',
      providesTags: ['Cliente']
    }),
    getClienteByDocumento: builder.query<ClienteProps, string>({
      query: (documento) => `/clientes/buscar-documento?documento=${documento}`,
    }),
    getClientesByPhone: builder.query<ClienteProps, string>({
      query: (telefone) => `/clientes/buscar-telefone?telefone=${telefone}`,
      providesTags: ['Cliente']
    }),

    addCliente: builder.mutation<ClienteProps, CreateClienteRequest>({
      query: (cliente) => ({
        url: '/clientes',
        method: 'POST',
        body: cliente
      }),
      invalidatesTags: ['Cliente']
    }),

    updateCliente: builder.mutation<void, {
      id: number;
      pessoaFisica?: Partial<PessoaFisica> | null;
      pessoaJuridica?: Partial<PessoaJuridica> | null;
    }>
      ({
        query: ({ id, pessoaFisica, pessoaJuridica }) => ({
          url: `/clientes/${id}`,
          method: 'PUT',
          body: {
            pessoaFisica: pessoaFisica ?? null,
            pessoaJuridica: pessoaJuridica ?? null
          },
        }),
      }),

    deleteCliente: builder.mutation<{ success: boolean; id: number }, number>({
      query: (id) => ({
        url: `/clientes/${id}`,
        method: 'DELETE'
      }),
      invalidatesTags: ['Cliente']
    }),

    getClienteIdByCpf: builder.query<{ id: string }, string>({
      query: (cpf) => `/clientes/cpf/${cpf}`,
    }),

    getClienteById: builder.query<any, string>({
      query: (id) => `/clientes/${id}`,
    }),
    getClientesPf: builder.query<ClienteProps[], string>({
      query: () => '/pessoas-fisicas',
      providesTags: ['Cliente']
    }),

    getClienteByCpfPf: builder.query<ClienteProps, string>({
      query: (cpf) => `/pessoas-fisicas/buscar-cpf?cpf=${cpf}`
    }),
    addClientePf: builder.mutation<ClienteProps, CreateClienteRequest>({
      query: (cliente) => ({
        url: '/pessoas-fisicas',
        method: 'POST',
        body: cliente,
        tipoPessoa: undefined
      }),
      invalidatesTags: ['Cliente']
    }),
    updateClientePf: builder.mutation<ClienteProps, ClienteProps>({
      query: (cliente) => ({
        url: `/pessoas-fisicas/${cliente.clienteId}`,
        method: 'PUT',
        body: cliente,
        tipoPessoa: undefined,
      }),
      invalidatesTags: ['Cliente']
    }),
    deleteClientePf: builder.mutation<{ success: boolean; id: number }, number>({
      query: (id) => ({
        url: `/pessoas-fisicas/${id}`,
        method: 'DELETE'
      }),
      invalidatesTags: ['Cliente']
    }),
    getClientesPj: builder.query<ClienteProps[], void>({
      query: () => '/pessoas-juridicas',
      providesTags: ['Cliente']
    }),
    getClienteByCpfPj: builder.query<ClienteProps, string>({
      query: (cpf) => `/pessoas-juridicas/buscar-cpf?cpf=${cpf}`
    }),
    addClientePj: builder.mutation<ClienteProps, CreateClienteRequest>({
      query: (cliente) => ({
        url: '/pessoas-juridicas',
        method: 'POST',
        body: cliente,
        tipoPessoa: undefined,
      }),
      invalidatesTags: ['Cliente']
    }),
    updateClientePj: builder.mutation<ClienteProps, ClienteProps>({
      query: (cliente) => ({
        url: `/pessoas-juridicas/${cliente.clienteId}`,
        method: 'PUT',
        body: cliente,
      }),
      invalidatesTags: ['Cliente']
    }),
    deleteClientePj: builder.mutation<{ success: boolean; id: number }, number>({
      query: (id) => ({
        url: `/pessoas-juridicas/${id}`,
        method: 'DELETE'
      }),
      invalidatesTags: ['Cliente']
    }),
    importarProdutosXml: builder.mutation<string, { file: File; filialId: number }>({
      query: ({ file, filialId }) => {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('filialId', filialId.toString());
        return {
          url: '/produtos/importar-xml',
          method: 'POST',
          body: formData,
          responseHandler: (res) => res.text()
        };
      },
      invalidatesTags: ['Produto']
    }),

    importarProdutosXmlFilial: builder.mutation<ImportarProdutosResponse, { file: File; filialId: number }>({
      query: ({ file, filialId }) => {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('filialId', filialId.toString());

        return {
          url: `/produtos/importar-xml`,
          method: 'POST',
          body: formData,
        };
      },
    }),


    listarFiliais: builder.query<any[], void>({
      query: () => 'filial',
      providesTags: ['Filial'],
    }),
    addNfe: builder.mutation<any, EmitirNotaPayload>({
      query: (body) => ({
        url: 'api/nfse/dps/emitir?empresaId=1',
        method: 'POST',
        body
      })
    }),
    addNf: builder.mutation<any, EmitirNfPayload>({
      query: (payload) => ({
        url: 'api/nfe/emitir',
        method: 'POST',
        body: payload
      }),
    }),
    CreateServiceOrder: builder.mutation<OrdemServicoDTO, Partial<OrdemServicoDTO>>({
      query: (novoProduto) => ({
        url: '/ordens-servico',
        method: 'POST',
        body: novoProduto
      }),
      invalidatesTags: ['Produto']
    }),
    addPedidoEntrega: builder.mutation<{ id: number }, PedidoPayload>({
      query: (body) => ({
        url: '/pedidos',
        method: 'POST',
        body,
      }),
    }),
    finalizarPedido: builder.mutation<void, number>({
      query: (id) => ({
        url: `/pedidos/${id}/entregar`,
        method: 'POST',
      }),
    }),
    // Criar nova mesa
    criarMesa: builder.mutation<{ id: number }, { numero: number; aberta: boolean }>({
      query: (payload) => ({
        url: 'mesas/criar-ou-reutilizar',      // ou a rota correta do backend
        method: 'POST',
        body: payload,      // aqui vai { numero: x, aberta: true }
      }),
    }),

    // Criar ou reutilizar mesa por número
    criarOuReutilizarMesa: builder.mutation({
      query: (numero: number) => ({
        url: `mesas/criar-ou-reutilizar?numero=${numero}`,
        method: 'POST',
      }),
    }),

    // Listar mesas abertas
    listarMesasAbertas: builder.query<Mesa[], void>({
      query: () => 'mesas/ativas',
      providesTags: ['Mesa'],
    }),

    // Calcular total da mesa
    calcularTotalMesa: builder.query<number, number>({
      query: (numeroMesa: any) => `mesas${numeroMesa}/total`,
    }),

    // Finalizar mesa e obter PDF (retorno em blob)
    finalizarMesa: builder.mutation<Blob, number>({
      query: (numeroMesa: any) => ({
        url: `mesas${numeroMesa}/finalizar`,
        method: 'POST',
        responseHandler: async (response: { blob: () => any }) => {
          const blob = await response.blob();
          return blob;
        },
      }),
      invalidatesTags: ['Mesa'],
    }),

    // Listar itens da mesa
    listarItensDaMesa: builder.query<ItemMesa[], number>({
      query: (numeroMesa: any) => `mesas${numeroMesa}/itens`,
      providesTags: ['Mesa'],
    }),

    listarPedidos: builder.query<Pedido[], { status?: string; mesaId?: number }>({
      query: ({ status, mesaId }) => {
        const params = new URLSearchParams();
        if (status) params.append('status', status);
        if (mesaId !== undefined) params.append('mesaId', mesaId.toString());
        return {
          url: '/mesas',
          params,
        };
      },
      providesTags: ['Pedido'],
    }),


    // Adicionar pedido à mesa
    adicionarPedido: builder.mutation<{ id: number }, PedidoMesaDTO>({
      query: (dto: any) => ({
        url: 'mesas/pedido',
        method: 'POST',
        body: dto,
      }),
      invalidatesTags: ['Mesa', 'Pedido'],
    }),
    sairParaEntrega: builder.mutation<void, number>({
      query: (id) => ({
        url: `/pedidos/${id}/entregar`,
        method: 'PUT',
      }),
    }),

    // GET /ativas
    getMesasAtivas: builder.query<Mesa[], void>({
      query: () => 'mesas/ativas',
      providesTags: ['Mesa'],
    }),

    // GET /{numeroMesa}/total
    getTotalMesa: builder.query<number, number>({
      query: (numeroMesa) => `/${numeroMesa}/total`,
    }),
    // GET /{numeroMesa}/itens
    getItensMesa: builder.query<ItemMesaDTO[], number>({
      query: (numeroMesa) => `mesas/${numeroMesa}/itens`,
      providesTags: ['Pedido'],
    }),
    criarOrdemServico: builder.mutation({
      query: (dto) => ({
        url: '/ordens-servico',
        method: 'POST',
        body: dto,
      }),
      invalidatesTags: ['OrdemServico'],
    }),
    listarOrdensServico: builder.query({
      query: () => '/ordens-servico',
      providesTags: ['OrdemServico'],
    }),
    buscarOrdemServico: builder.query({
      query: (id) => `/ordens-servico/${id}`,
      providesTags: (result, error, id) => [{ type: 'OrdemServico', id }],
    }),
    deletarOrdemServico: builder.mutation({
      query: (id) => ({
        url: `/ordens-servico/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: ['OrdemServico'],
    }),
    gerarComprovante: builder.query({
      query: (id) => ({
        url: `/ordens-servico/comprovante/${id}`,
        responseHandler: (response) => response.blob(),
      }),
      providesTags: (result, error, id) => [{ type: 'OrdemServico', id }],
    }),
    atualizarOrdemServico: builder.mutation({
      query: ({ id, ...patch }) => ({
        url: `ordem-servico/${id}`,
        method: 'PUT',
        body: patch
      }),
      invalidatesTags: ['OrdemServico']
    }),
    limparMesa: builder.mutation<void, number>({
      query: (numeroMesa) => ({
        url: `mesas/limpar/${numeroMesa}`,
        method: 'DELETE',
      }),
    }),
    getProdutoPorGtin: builder.query<ProdutoProps, string>({
      query: (codigo) => `produtos/gtins/${codigo}`,
      providesTags: (result, error, codigo) => [{ type: 'Produto', id: codigo }],
    }),
    enviarParaEntrega: builder.mutation<PedidoEntregaDTO, PedidoEntregaDTO>({
      query: (pedidoEntrega) => ({
        url: 'pedidos-entrega',
        method: 'POST',
        body: pedidoEntrega,
      }),
    }),
    gerarVenda: builder.mutation<any, number>({
      query: (pedidoId) => ({
        url: `/pedidos-entrega/${pedidoId}/venda`,
        method: 'POST',
      }),
    }),
    getPedidosEntrega: builder.query<PedidoEntregaDTO[], void>({
      query: () => '/pedidos-entrega',
      transformResponse: (response: PedidoEntregaDTO[]) => response,
    }),
    getPedidosEntregaById: builder.query<PedidoEntregaDTO[], string>({
      query: (id) => `/pedidos-entrega/${id}`,
      transformResponse: (response: PedidoEntregaDTO[]) => response,
    }),
    addAccountsPayable: builder.mutation<void, {
      fornecedor: string;
      valor: number;
      vencimento: string;
      dataPagamento: string;
      status: 'A_VENCER' | 'VENCIDA' | 'PAGO' | 'PENDENTE' | 'ATRASADO';
    }>({
      query: (body) => ({
        url: 'contas-pagar',
        method: 'POST',
        body,
      }),
    }),
    getAccountsPayable: builder.query<any, void>({
      query: () => '/contas-pagar',
    }),
    getAccountsReceivable: builder.query<any, void>({
      query: () => '/contas-receber',
    }),
    addAccountsReceivable: builder.mutation<void, {
      cliente: string;
      descricao: string;
      valor: number;
      vencimento: string;
      status: 'PENDENTE' | 'PAGO';
    }>({
      query: (body) => ({
        url: 'contas-receber',
        method: 'POST',
        body,
      }),
    }),
    updateAccountsReceivable: builder.mutation({
      query: ({ id, ...patch }) => ({
        url: `contas-receber/${id}`,
        method: 'PUT',
        body: patch
      }),
      invalidatesTags: ['OrdemServico']
    }),
    emitirNota: builder.mutation<NotaFiscalResponseDTO, EmitirNotaRequestDTO>({
      query: (body) => ({
        url: '/emitir',
        method: 'POST',
        body,
      }),
    }),
    emitirNotaTeste: builder.mutation<NotaFiscalResponseDTO, EmitirNfeRequestDTO>({
      query: (body) => ({
        url: '/teste',
        method: 'POST',
        body,
      }),
    }),
    getEmitente: builder.query<Empresa[], void>({
      query: () => '/empresas',
      providesTags: ['Empresas'],
    }),
    consultarNfe: builder.query<string, string>({
      query: (id) => `/api/nfe/consultar/${id}`,
    }),
    baixarDanfeNfe: builder.query<Blob, string>({
      query: (id) => ({
        url: `/api/nfe/${id}`,
        responseHandler: (response) => response.blob(),
      }),
    }),
    buscarPorDocumento: builder.query<NFe[], DocumentoParam>({
      query: ({ tipo, numero }) => ({
        url: '/api/nfe/consultar-por-documento',
        params: { tipo, numero },
      }),
      providesTags: (result) =>
        result
          ? [...result.map(({ id }) => ({ type: 'NFe' as const, id })), 'NFe']
          : ['NFe'],
    }),
    getNf: builder.query<{ data: NotaFiscal[] }, string>({
      query: () => '/api/nfe/documentos'
    }),
    cancelarNota: builder.mutation<void, NotaFiscal>({
      query: (body) => ({
        url: `api/nfe/cancelar`,
        method: 'POST',
        body,
      }),
    }),
  }),
})

export const {
  useCancelarNotaMutation,
  useGetNfQuery,
  useGetClienteIdByCpfQuery,
  useGetProdutoByIdQuery,
  useLazyBaixarDanfeNfeQuery,
  useBuscarPorDocumentoQuery,
  useEmitirNotaTesteMutation,
  useGetEmitenteQuery,
  useConsultarNfeQuery,
  useBaixarDanfeNfeQuery,
  useGetTotalAnoAtualQuery,
  useUpdateAccountsReceivableMutation,
  useGetAccountsPayableQuery,
  useGetAccountsReceivableQuery,
  useAddAccountsReceivableMutation,
  useAddAccountsPayableMutation,
  useGetPedidosEntregaByIdQuery,
  useGetPedidosEntregaQuery,
  useGerarVendaMutation,
  useEnviarParaEntregaMutation,
  useGetProdutoPorGtinQuery,
  useLimparMesaMutation,
  useAtualizarOrdemServicoMutation,
  useCriarOrdemServicoMutation,
  useListarOrdensServicoQuery,
  useBuscarOrdemServicoQuery,
  useDeletarOrdemServicoMutation,
  useGerarComprovanteQuery,
  useLazyGerarComprovanteQuery,
  useLazyGetItensMesaQuery,
  useLazyListarPedidosQuery,
  useLazyGetMesasAtivasQuery,
  useGetMesasAtivasQuery,
  useGetTotalMesaQuery,
  useGetItensMesaQuery,
  useSairParaEntregaMutation,
  useCriarMesaMutation,
  useCriarOuReutilizarMesaMutation,
  useListarMesasAbertasQuery,
  useCalcularTotalMesaQuery,
  useFinalizarMesaMutation,
  useListarItensDaMesaQuery,
  useListarPedidosQuery,
  useAdicionarPedidoMutation,
  useLazyGetClientesByPhoneQuery,
  useAddPedidoEntregaMutation,
  useFinalizarPedidoMutation,
  useGetClientesByPhoneQuery,
  useCreateServiceOrderMutation,
  useAddNfeMutation,
  useAddNfMutation,
  useGetTotalPorPeriodoQuery,
  useGetTotalDiaSingleQuery,
  useImportarProdutosXmlFilialMutation,
  useGetTotalDiaSingQuery,
  useLoginMutation,
  useLogoutMutation,
  useBuscarUsuarioPorIdQuery,
  useBuscarUsuarioQuery,
  useGetTotalDiaQuery,
  useGetTotalSemanaQuery,
  useGetTotalSemanasQuery,
  useGetTotalMesQuery,
  useGetTotalMesesQuery,
  useGetClienteByDocumentoQuery,
  useGetProdutosQuery,
  useGetProdutosByNameQuery,
  useAddProdutoMutation,
  useUpdateProdutoMutation,
  useDeleteProdutoMutation,
  useGetVendasQuery,
  useAddVendaMutation,
  useUpdateVendaMutation,
  useDeleteVendaMutation,
  useGetClientesQuery,
  useAddClienteMutation,
  useUpdateClienteMutation,
  useDeleteClienteMutation,
  useForgotPasswordMutation,
  useResetPasswordMutation,
  useGetClientesPfQuery,
  useGetClienteByCpfPfQuery,
  useAddClientePfMutation,
  useUpdateClientePfMutation,
  useDeleteClientePfMutation,
  useGetClientesPjQuery,
  useGetClienteByCpfPjQuery,
  useAddClientePjMutation,
  useUpdateClientePjMutation,
  useDeleteClientePjMutation,
  useLazyGetClienteIdByCpfQuery,
  useLazyGetClienteByIdQuery,
  useGetClienteByIdQuery,
  useLazyGetClienteByDocumentoQuery,
  useSearchProdutosQuery,
  useCriarUsuarioMutation,
  useImportarProdutosXmlMutation,
  useListarFiliaisQuery,
  useGetTotalAnoQuery,
  useGetVendasPorPeriodoQuery,
  useLazyGetProdutoPorGtinQuery
} = api

