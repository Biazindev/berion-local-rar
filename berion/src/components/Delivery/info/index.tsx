import { useParams } from 'react-router-dom';
import { useGetPedidosEntregaByIdQuery } from '../../../services/api';
import { Container, Field, Title } from '../info/styles';
import { formatarStatus } from '../../Utils/formatters'; 

const Info = () => {
  const { id } = useParams<{ id: string }>();
const { data, isLoading, error } = useGetPedidosEntregaByIdQuery(id!)

  const pedido = data?.[0]; 

  if (isLoading) return <Container>Carregando...</Container>;
  if (error || !pedido) return <Container>Erro ao carregar dados.</Container>;

  return (
    <Container>
      <Title>Informações da Entrega</Title>
      <Field><span>Telefone:</span> {pedido.fone}</Field>
      <Field><span>Endereço:</span></Field>
      <Field><span>Rua:</span> {pedido.enderecoEntrega?.logradouro}</Field>
      <Field><span>Número:</span> {pedido.enderecoEntrega?.numero}</Field>
      <Field><span>Bairro:</span> {pedido.enderecoEntrega?.bairro}</Field>
      <Field><span>Cidade:</span> {pedido.enderecoEntrega?.municipio}</Field>
      <Field><span>UF:</span> {pedido.enderecoEntrega?.uf}</Field>
      <Field><span>CEP:</span> {pedido.enderecoEntrega?.cep}</Field>

      {pedido.enderecoEntrega?.complemento && (
        <Field><span>Complemento:</span> {pedido.enderecoEntrega.complemento}</Field>
      )}

      <Field><span>Pago:</span> {pedido.pago ? 'Sim' : 'Não'}</Field>

      {pedido.precisaTroco && (
        <Field>
          <span>Troco para:</span> R$ {Number(pedido.trocoPara).toFixed(2)}
        </Field>
      )}

      <Field><span>Status:</span> {formatarStatus(pedido.status)}</Field>
      <Field><span>Motoboy:</span> {pedido.nomeMotoboy || 'Não informado'}</Field>
      <Field><span>Observação:</span> {pedido.observacao || 'Nenhuma'}</Field>
    </Container>
  );
};

export default Info;
