import { useNavigate } from 'react-router-dom';
import { Button, DeleteButton, Td } from "../../SaleList/styles";
import { VendaProps } from "../../SaleList/index";
import { PencilLine, Trash2 } from "lucide-react";

type VendaRowProps = {
  venda: VendaProps;
  onDelete: (id: number) => void | Promise<void>;
  onEdit: (venda: VendaProps) => void;
};

const VendaRow = ({ venda, onDelete, onEdit }: VendaRowProps) => {
  const navigate = useNavigate();

  const totalVenda = venda.pagamento?.totalVenda ?? venda.totalVenda ?? 0;
  const totalDesconto = venda.pagamento?.totalDesconto ?? venda.totalDesconto ?? 0;
  const totalPagamento = venda.pagamento?.totalPagamento ?? venda.totalPagamento ?? 0;
  const formaPagamento = venda.pagamento?.formaPagamento ?? venda.formaPagamento ?? 'N/A';
  const cliente = venda.cliente?.pessoaFisica?.nome ?? 'Venda Anônima';

  const handleRowClick = () => {
    localStorage.setItem('vendaSelecionada', JSON.stringify(venda));
    navigate(`/venda/${venda.id}`);
  };

  return (
    <tr onClick={handleRowClick} style={{ cursor: 'pointer' }}>
      <Td>{venda.id}</Td>
      <Td>{cliente}</Td>
      <Td>R$ {totalVenda.toFixed(2)}</Td>
      <Td>R$ {totalPagamento.toFixed(2)}</Td>
      <Td>{formaPagamento}</Td>
      <Td>{venda.chaveAcessoNfe ? 'Sim' : 'Não'}</Td>
      <Td>{new Date(venda.dataVenda).toLocaleString('pt-BR')}</Td>
      <Td
        onClick={(e) => e.stopPropagation()}
        style={{ display: 'flex', gap: '0.5rem' }}
      >
        <Button onClick={() => onEdit(venda)}>
          <PencilLine size={16} />
        </Button>
        <DeleteButton onClick={() => onDelete(venda.id)}>
          <Trash2 size={16} />
        </DeleteButton>
      </Td>
    </tr>
  );
};

export default VendaRow;
