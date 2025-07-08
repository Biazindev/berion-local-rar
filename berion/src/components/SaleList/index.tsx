import { useState, useEffect } from 'react';
import { skipToken } from '@reduxjs/toolkit/query';


import {
  useGetVendasPorPeriodoQuery,
  useGetTotalPorPeriodoQuery,
  useDeleteVendaMutation,
} from '../../services/api';

import {
  Label,
  Input,
  InfoText,
  Table,
  Th,
  EmptyMessage,
  PageContainer,
  Card,
  Button,
} from './styles';
import DraggableTableContainer from '../DraggableTableContainer';
import VendaRow from '../Utils/VendaRow';
import Loader from '../Loader';

export type ItemVenda = {
  id: number;
  quantidade: number;
  produtoId: number | null;
  nomeProduto?: string;
  produto?: {
    id: number;
    nome: string;
    precoUnitario: number
  };
  precoUnitario?: number
  totalItem?: number;
};

export type Endereco = {
  cep: string;
  bairro: string;
  logradouro: string;
  numero: string;
  uf: string;
  complemento: string;
  codigoIbge: string;
  municipio: string;
};

export type PessoaFisica = {
  id: number;
  nome: string;
  cpf: string;
  email: string;
  telefone: string;
  endereco: Endereco;
  dataNascimento: string;
};

export type Cliente = {
  id: number;
  tipoPessoa: 'FISICA' | 'JURIDICA' | string;
  pessoaFisica: PessoaFisica | null;
  pessoaJuridica: any; 
  lancamentos: any[];
  pedidos: any[];
  dataCadastro: string | null;
};

export type Emitente = {
  id: number;
  razaoSocial: string;
  nomeFantasia: string;
  cnpj: string;
  inscricaoEstadual: string;
  inscricaoMunicipal: string;
  regimeTributario: string;
  cnae: string;
  crt: number;
  fone: string;
  serieNfe: number;
  ambiente: string;
  endereco: Endereco;
  hibernateLazyInitializer?: Record<string, unknown>;
};

export type Pagamento = {
  id: number;
  formaPagamento: 'DINHEIRO' | 'CARTAO' | 'PIX' | string;
  valorPago: number | null;
  venda: any | null;
  valorRestante: number | null;
  dataPagamento: string | null;
  status: string | null;
  numeroParcelas: number;
  totalVenda: number | null;
  totalDesconto: number;
  totalPagamento: number;
};

export type Frete = {
  valorFrete: number | null;
  valorSeguro: number | null;
  valorDesconto: number | null;
  outrasDespesas: number | null;
  transportadora: any | null;
  volumes: any[];
};

export type VendaProps = {
  formaPagamento: string;
  totalPagamento: number;
  totalDesconto: number;
  id: number;
  dataVenda: string;
  totalVenda: number;
  documentoCliente: string;
  itens: ItemVenda[];
  status: string;
  cliente: Cliente;
  emitente: Emitente;
  chaveAcessoNfe: string | null;
  numeroProtocoloNfe: string | null;
  modelo: string;
  vendaAnonima: boolean;
  emitirNotaFiscal: boolean;
  pagamento: Pagamento;
  modFrete: string | null;
  ultimaAtualizacao: string | null;
  observacao: string | null;
  infAdFisco: string | null;
  frete: Frete;
};


const SaleList = () => {
  const [inicio, setInicio] = useState('');
  const [fim, setFim] = useState('');
  const [isInitialLoad, setIsInitialLoad] = useState(true);

  // 1. Set initial date range (last 30 days)
  useEffect(() => {
  const hoje = new Date();
  hoje.setDate(hoje.getDate() - 1);

  const cincoDiasAtras = new Date(hoje);
  cincoDiasAtras.setDate(hoje.getDate() - 5);

  const formatDate = (date: Date) => date.toISOString().split('T')[0];
  setInicio(formatDate(cincoDiasAtras));
  setFim(formatDate(hoje));
}, []);


  // 2. Fetch sales data
  const {
    data: vendas = [],
    isLoading: loadingVendas,
    error: errorVendas,
    refetch: refetchVendas
  } = useGetVendasPorPeriodoQuery(
    inicio && fim ? { inicio, fim } : skipToken
  );

  const {
    data: total = 0,
    isLoading: loadingTotal,
    error: errorTotal,
    refetch: refetchTotal
  } = useGetTotalPorPeriodoQuery(
    inicio && fim ? { inicio, fim } : skipToken
  );

  // 4. Mark initial load as complete after first fetch
  useEffect(() => {
    if (inicio && fim && isInitialLoad) {
      setIsInitialLoad(false);
    }
  }, [inicio, fim, isInitialLoad]);

  // 5. Format items with product names
  const formatItems = (items: ItemVenda[]) => {
    return items.map(item => ({
      ...item,
      nomeProduto: item.produto?.nome || item.nomeProduto || 'Produto não informado',
      precoUnitario: item.produto?.precoUnitario || item.precoUnitario || 0,
      totalItem: item.totalItem || (item.quantidade * (item.produto?.precoUnitario || item.precoUnitario || 0))
    }));
  };

  // 6. Delete mutation
  const [deleteVenda] = useDeleteVendaMutation();

  const handleDelete = async (id: number) => {
    if (window.confirm('Tem certeza que deseja excluir esta venda?')) {
      try {
        await deleteVenda(id).unwrap();
        refetchVendas();
        refetchTotal();
        alert('Venda excluída com sucesso!');
      } catch (error) {
        console.error('Erro ao excluir venda:', error);
        alert('Erro ao excluir venda.');
      }
    }
  };

  const handleEdit = (venda: VendaProps) => {
    alert(`Editar venda ID: ${venda.id}`);
  };

  // 7. Debug log
  useEffect(() => {
    if (vendas.length > 0) {
      console.log('Dados das vendas recebidos:', vendas);
      console.log('Total recebido:', total);
    }
  }, [vendas, total]);

  return (
    <PageContainer>
      <Card>
        <Label>
          Início:
          <Input
            type="date"
            value={inicio}
            onChange={(e) => setInicio(e.target.value)}
          />
        </Label>

        <Label>
          Fim:
          <Input
            type="date"
            value={fim}
            onChange={(e) => setFim(e.target.value)}
          />
        </Label>
      </Card>
      {/* Display area */}
      {!isInitialLoad && (
        <>
          {/* Total sales */}
          {!loadingTotal && !errorTotal && (
            <InfoText>
              Total no período: <strong>R$ {Number(total).toFixed(2)}</strong>
            </InfoText>
          )}

          {/* Loading/error states */}
          {loadingVendas ? (
            <InfoText><Loader /></InfoText>
          ) : errorVendas ? (
            <InfoText className={errorVendas ? 'error' : ''}>
              Erro ao carregar vendas, tente novo período na busca
            </InfoText>
          ) : (
            /* Sales table */
            <>
              {vendas.length > 0 ? (
                <DraggableTableContainer>
                  <Table>
                    <thead>
                      <tr>
                        <Th>ID</Th>
                        <Th>Cliente</Th>
                        <Th>Valor Total</Th>
                        <Th>Valor Pago</Th>
                        <Th>Pagamento</Th>
                        <Th>Nota Fiscal</Th>
                        <Th>Data</Th>
                        <Th>Ações</Th>
                      </tr>
                    </thead>
                    <tbody>
                      {vendas.map(venda => (
                        <VendaRow
                          key={venda.id}
                          venda={{
                            ...venda,
                            itens: formatItems(venda.itens)
                          }}
                          onDelete={handleDelete}
                          onEdit={handleEdit}
                        />
                      ))}
                    </tbody>
                  </Table>
                </DraggableTableContainer>
              ) : (
                <EmptyMessage>Nenhuma venda encontrada no período selecionado</EmptyMessage>
              )}
            </>
          )}
        </>
      )}
    </PageContainer>
  );
};

export default SaleList;