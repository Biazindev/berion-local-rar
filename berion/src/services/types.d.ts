export interface Emitente {
  nome: string;
  nomeFantasia?: string;
  cnpj: string;
  inscricaoEstadual?: string;
  inscricaoMunicipal?: string;
  endereco: {
    logradouro: string;
    numero: string;
    complemento?: string;
    bairro: string;
    codigoMunicipio: string;
    municipio: string;
    uf: string;
    cep: string;
    pais?: string;
    telefone?: string;
  };
}

export interface Destinatario {
  nome: string;
  cpf?: string;
  cnpj?: string;
  inscricaoEstadual?: string;
  email?: string;
  endereco: {
    logradouro: string;
    numero: string;
    complemento?: string;
    bairro: string;
    codigoMunicipio: string;
    municipio: string;
    uf: string;
    cep: string;
    pais?: string;
    telefone?: string;
  };
}

export interface Produto {
  codigo: string;
  descricao: string;
  ncm: string;
  cfop: string;
  unidade: string;
  quantidade: number;
  valorUnitario: number;
  valorTotal: number;
  valorDesconto?: number;
  ean?: string;
  cest?: string;
  informacoesAdicionais?: string;
}

export interface Pagamento {
  forma: string;
  valor: number;
  troco?: number;
}

export interface Transportadora {
  nome?: string;
  cpf?: string;
  cnpj?: string;
  inscricaoEstadual?: string;
  endereco?: {
    logradouro: string;
    numero: string;
    complemento?: string;
    bairro: string;
    codigoMunicipio: string;
    municipio: string;
    uf: string;
    cep: string;
    pais?: string;
  };
  veiculo?: {
    placa: string;
    uf: string;
    rntc?: string;
  };
  volumes?: {
    quantidade: number;
    especie: string;
    marca?: string;
    numeracao?: string;
    pesoLiquido?: number;
    pesoBruto?: number;
  };
}

export interface NFe {
  autorizacao: any;
  chave: any;
  valor_total: any;
  ambiente: any;
  id: string;
  chaveAcesso: string;
  numero: string;
  serie: string;
  modelo: string;
  tipoOperacao: 'entrada' | 'saida';
  naturezaOperacao: string;
  dataEmissao: string;
  dataSaidaEntrada?: string;
  tipoDocumento: number;
  localDestino?: string;
  finalidadeEmissao?: number;
  consumidorFinal?: boolean;
  presencaComprador?: number;
  emitente: Emitente;
  destinatario: Destinatario;
  produtos: Produto[];
  totais: {
    valorTotalProdutos: number;
    valorDesconto?: number;
    valorTotal: number;
    valorFrete?: number;
    valorSeguro?: number;
    valorOutrasDespesas?: number;
    valorIpi?: number;
    valorAproximadoTributos?: number;
    valorPis?: number;
    valorCofins?: number;
    valorIcms?: number;
    baseCalculoIcms?: number;
  };
  pagamentos?: Pagamento[];
  transportadora?: Transportadora;
  informacoesAdicionais?: {
    observacoes?: string;
    processoEmissao?: string;
    versaoProcesso?: string;
    fiscal?: any;
  };
  status: {
    codigo: string;
    motivo?: string;
    dataHora?: string;
  };
  protocolo?: {
    numero: string;
    dataHora: string;
    digival: string;
  };
}

export interface Endereco {
  cep: string;
  bairro: string;
  logradouro: string;
  numero: string;
  uf: string;
  complemento: string;
  codigoIbge: string;
  municipio: string;
}

export interface Emitente {
  id: number;
  razaoSocial: string;
  nomeFantasia: string;
  cnpj: string;
  inscricaoEstadual: string;
  inscricaoMunicipal: string | null | 'null';
  regimeTributario: string;
  cnae: string;
  crt: number;
  fone: string;
  endereco: Endereco;
}

export interface NFe {
  id: string;
  ambiente: string;
  status: string;
  dataEmissao: string;
  numero: number;
  serie: number;
  modelo: string;
  valor_total: number;
  chave: string;
  autorizacao?: {
    numero_protocolo?: string;
  };
  emitente: {
    nome: string;
    cnpj: string;
    inscricaoMunicipal?: string;
  };
  destinatario: {
    nome: string;
    cpf?: string;
    cnpj?: string;
  };
}
