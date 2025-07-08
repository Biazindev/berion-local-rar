import { Amount, Card, Cards, CardTitle, Container, Section, SectionTitle, Table, Title } from './styles';
import { useGetProdutosQuery, useGetVendasQuery, useGetAccountsPayableQuery, useGetAccountsReceivableQuery, ProdutoProps } from '../../services/api';
import { Link } from 'react-router-dom';

interface Produto {
    precoCusto?: number;
    precoUnitario?: number;
    [key: string]: any;
}

interface Conta {
    id: string;
    descricao: string;
    fornecedor: string;
    valor: number;
    data: string;
    status: string;
    [key: string]: any;
}

const Financial = () => {
    // Recupera as contas do store de contas a pagar usando a query com localStorage
    const { data: contasAPagar = [] } = useGetAccountsPayableQuery(undefined, {
        selectFromResult: ({ data }) => ({
            data: data || JSON.parse(localStorage.getItem('contasAPagar') || '[]')
        })
    });

    // Recupera as cobranças do store de contas a receber usando a query com localStorage
    const { data: cobrancas = [] } = useGetAccountsReceivableQuery(undefined, {
        selectFromResult: ({ data }) => ({
            data: data || JSON.parse(localStorage.getItem('cobrancas') || '[]')
        })
    });


    // Recupera os produtos da API
    const { data: produtos } = useGetProdutosQuery();
    const { data: venda } = useGetVendasQuery();

    // Calcula o custo total dos produtos (para contas a pagar)
    const custoProdutoTotal = produtos?.reduce(
        (total: number, produto: ProdutoProps) =>
            total + (parseFloat(produto.precoCusto?.toString() || '0')),
        0
    ) ?? 0;

    // Calcula o total a receber de produtos (para contas a receber)
    const recebimentoProdutoTotal = venda
        ?.filter((venda) => venda.pagamento?.totalPagamento)
        .reduce((total, venda) => total + (venda.pagamento!.totalPagamento || 0), 0) || 0;

    // Calcula o total a pagar (contas não pagas + custo dos produtos)
    const totalAPagar = [...contasAPagar]
        .filter((c: Conta) => c.status !== 'PAGO')
        .reduce((sum: number, c: Conta) => sum + c.valor, 0) + custoProdutoTotal;

    // Calcula o total a receber (cobranças pendentes + recebimento de produtos)
    const totalAReceber = [...cobrancas].reduce((sum: number, c: Conta) => sum + c.valor, 0) + recebimentoProdutoTotal;

    // Calcula o saldo (recebido - pago)
    const saldo = totalAReceber - totalAPagar;

    // Obtém as últimas 5 entradas (contas a receber)
    const ultimasEntradas = [...cobrancas]
        .sort((a: Conta, b: Conta) => new Date(b.data).getTime() - new Date(a.data).getTime())
        .slice(0, 5)
        .map((conta: Conta) => ({
            id: conta.id,
            descricao: conta.descricao,
            valor: conta.valor,
            status: conta.status,
            data: new Date(conta.vencimento).toLocaleDateString('pt-BR')
        }));

    // Obtém as últimas 5 saídas (contas a pagar)
    const ultimasSaidas = [...contasAPagar]
        .sort((a: Conta, b: Conta) => new Date(b.data).getTime() - new Date(a.data).getTime())
        .slice(0, 5)
        .map((conta: Conta) => ({
            id: conta.id,
            fornecedor: conta.fornecedor,
            descricao: conta.descricao,
            valor: conta.valor,
            status: conta.status,
            data: new Date(conta.vencimento).toLocaleDateString('pt-BR')
        }));

    const statusMap: Record<string, string> = {
        A_VENCER: "A Vencer",
        VENCIDA: "Vencida",
        PAGO: "Pago",
        PENDENTE: "Pendente",
        ATRASADO: "Atrasado",
    };

    return (
        <Container>
            <Title>💰 Painel Financeiro</Title>
            <Cards>
                <Card>
                    <CardTitle>Saldo Atual</CardTitle>
                    <Amount green={saldo >= 0} red={saldo < 0}>
                        R$ {saldo.toFixed(2)}
                    </Amount>
                </Card>
                <Link style={{ textDecoration: 'none' }} to={'/accounts-payable'}>
                    <Card style={{ textDecoration: 'none' }}>
                        <CardTitle style={{ textDecoration: 'none' }}>Contas a Pagar</CardTitle>
                        <Amount red>R$ {totalAPagar.toFixed(2)}</Amount>
                    </Card>
                </Link>
                <Link style={{ textDecoration: 'none' }} to={'/accounts-receivable'}>
                    <Card>
                        <CardTitle>Contas a Receber</CardTitle>
                        <Amount green>R$ {totalAReceber.toFixed(2)}</Amount>
                    </Card>
                </Link>
            </Cards>

            <Section>
                <SectionTitle>📈 Últimas Entradas</SectionTitle>
                <Table>
                    <thead>
                        <tr>
                            <th>Descrição</th>
                            <th>Valor</th>
                            <th>Data de vencimento</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {ultimasEntradas.map((item) => (
                            <tr key={item.id}>
                                <td>{item.descricao}</td>
                                <td>R$ {item.valor.toFixed(2)}</td>
                                <td>{item.data}</td>
                                <td>{statusMap[item!.status]}</td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            </Section>

            <Section>
                <SectionTitle>📉 Últimas Saídas</SectionTitle>
                <Table>
                    <thead>
                        <tr>
                            <th>Descrição</th>
                            <th>Valor</th>
                            <th>Data de vencimento</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {ultimasSaidas.map((item) => (
                            <tr key={item.id}>
                                <td>{item.fornecedor}</td>
                                <td>R$ {item.valor.toFixed(2)}</td>
                                <td>{item.data}</td>
                                <td>{statusMap[item.status]}</td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            </Section>
        </Container>
    );
};

export default Financial;