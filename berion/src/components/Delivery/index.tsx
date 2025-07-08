import { useState } from 'react';
import { useGetPedidosEntregaQuery } from '../../services/api'
import {
  DeliveryCard,
  DeliveryContainer,
  DeliveryTable,
  DeliveryTitle,
  StatusSelect,
  TableHeader,
  TableRow
} from './styles';

import { Cliente, Produto } from '../Venda/types';
import { Link } from 'react-router-dom';
import Loader from '../Loader';

interface Endereco {
  logradouro?: string;
  bairro?: string;
  numero?: string;
  municipio?: string;
}

interface PedidoEntrega {
  id: number;
  cliente: Cliente;
  produtos: Produto[];
  total: number;
  status: 'Pendente' | 'Concluída';
}

const parsePreco = (preco?: string | number | null): number => {
  if (typeof preco === 'number') return preco;
  if (typeof preco === 'string') {
    const normalizado = preco.replace(',', '.');
    const valor = parseFloat(normalizado);
    return isNaN(valor) ? 0 : valor;
  }
  return 0;
};

const Delivery = () => {
  const { data: pedidosEntrega = [], isLoading, isError } = useGetPedidosEntregaQuery();

  const [statusAtualizado, setStatusAtualizado] = useState<Record<number, 'Pendente' | 'Concluída'>>({});

  const handleChangeStatus = (pedidoId: number, novoStatus: 'Pendente' | 'Concluída') => {
    setStatusAtualizado((prev) => ({
      ...prev,
      [pedidoId]: novoStatus
    }));
    // Aqui você pode futuramente chamar um endpoint para atualizar o status no backend
  };

  const renderClienteInfo = (cliente: Cliente) => {
    if (cliente.pessoaFisica) {
      const endereco = cliente.pessoaFisica.endereco;
      return (
        <>
          <td>{cliente.pessoaFisica.nome}</td>
          <td>{endereco?.logradouro || '-'}</td>
          <td>{endereco?.bairro || '-'}</td>
          <td>{endereco?.numero || '-'}</td>
          <td>{endereco?.municipio || '-'}</td>
        </>
      );
    } else if (cliente.pessoaJuridica) {
      const endereco = cliente.pessoaJuridica.endereco;
      return (
        <>
          <td>{cliente.pessoaJuridica.razaoSocial}</td>
          <td>{endereco?.logradouro || '-'}</td>
          <td>{endereco?.bairro || '-'}</td>
          <td>{endereco?.numero || '-'}</td>
          <td>{endereco?.municipio || '-'}</td>
        </>
      );
    }
    return (
      <>
        <td colSpan={5}>Cliente desconhecido</td>
      </>
    );
  };

  if (isLoading) return <div><Loader /></div>;
  if (isError) return <div>Erro ao carregar entregas.</div>;

  return (
    <DeliveryContainer>
      <DeliveryCard>
        <DeliveryTitle>Entregas</DeliveryTitle>

        <DeliveryTable>
          <thead>
            <TableHeader>
              <th>#</th>
              <th>Cliente</th>
              <th>Rua</th>
              <th>Bairro</th>
              <th>Número</th>
              <th>Cidade</th>
              <th>Produtos</th>
              <th>Total (R$)</th>
              <th>Status</th>
            </TableHeader>
          </thead>
          <tbody>
            {pedidosEntrega.map((entrega) => (
              <TableRow key={entrega.id}>
                <td>{entrega.id}</td>
                {entrega.cliente ? renderClienteInfo(entrega.cliente as Cliente) : <td colSpan={5}>Cliente não informado</td>}
                <td>
                  <ul style={{ margin: 0, paddingLeft: '1rem' }}>
                    {entrega.produtos!.map((produto) => (
                      <Link to={'delivery/entrega/${id}'}>
                        <li key={produto.id}>
                          {produto.nome} ({produto.quantidade} x R$ {parsePreco(produto.precoUnitario).toFixed(2)})
                        </li>
                      </Link>
                    ))}
                  </ul>
                </td>
                <td>R$ {parsePreco(entrega.total).toFixed(2)}</td>
                <td>
                  <StatusSelect
                    value={statusAtualizado[entrega.id!] || entrega.status}
                    onChange={(e) =>
                      handleChangeStatus(entrega.id!, e.target.value as 'Pendente' | 'Concluída')
                    }
                  >
                    <option value="Pendente">Pendente</option>
                    <option value="Concluída">Concluída</option>
                  </StatusSelect>
                </td>
              </TableRow>
            ))}
          </tbody>
        </DeliveryTable>
      </DeliveryCard>
    </DeliveryContainer>
  );
};

export default Delivery;
