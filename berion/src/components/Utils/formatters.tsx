import { PedidoEntregaDTO } from "../../services/api";

export function formatarStatus(status?: PedidoEntregaDTO['status']) {
  switch (status) {
    case 'EM_PREPARO':
      return 'Em preparo';
    case 'SAIU_PARA_ENTREGA':
      return 'Saiu para entrega';
    case 'ENTREGUE':
      return 'Entregue';
    case 'CANCELADO':
      return 'Cancelado';
    default:
      return 'Indefinido';
  }
}
