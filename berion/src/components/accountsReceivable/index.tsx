import React, { useState } from 'react';
import {
    useAddAccountsReceivableMutation,
    useGetAccountsReceivableQuery,
    useUpdateAccountsReceivableMutation,
    useGetVendasQuery
} from '../../services/api';
import {
    Button, Container, Title, TitleRow, Summary, Card,
    CardTitle, CardValue, Table, Th, Td, StatusBadge,
    ModalOverlay, ModalContent, CancelButton, ModalTitle,
    Input, ModalActions, SaveButton, Select
} from './styles';
import { Label } from '../OrderList/styles';
import { limparPropriedadesHibernate } from '../Utils/limpPropHibernate'

interface Cobranca {
    id?: number;
    cliente: string;
    descricao: string;
    vencimento: string;
    valor: number;
    status: 'PENDENTE' | 'PAGO';
}

interface Venda {
    pagamento?: {
        totalPagamento?: number;
    };
    [key: string]: any;
}

interface FormState {
    cliente: string;
    descricao: string;
    vencimento: string;
    valor: string;
    status: 'PENDENTE' | 'PAGO';
}

type Totalizadores = {
    total: number;
    recebido: number;
    pendentes: number;
    recebimentoProdutoTotal: number;
};

const calcularTotais = (
    cobrancas: Cobranca[],
    vendas: Venda[] | undefined
): Totalizadores => {
    const recebimentoProdutoTotal = vendas
        ?.filter((venda): venda is { pagamento: { totalPagamento: number } } =>
            venda.pagamento?.totalPagamento !== undefined
        )
        .reduce((total, venda) => total + venda.pagamento.totalPagamento, 0) || 0;

    const totalCobrancas = cobrancas.reduce((sum, c) => sum + c.valor, 0);
    const recebidoCobrancas = cobrancas
        .filter((c): c is Cobranca & { status: 'PAGO' } => c.status === 'PAGO')
        .reduce((sum, c) => sum + c.valor, 0);
    const pendentesCobrancas = cobrancas
        .filter((c): c is Cobranca & { status: 'PENDENTE' } => c.status === 'PENDENTE')
        .reduce((sum, c) => sum + c.valor, 0);

    return {
        total: totalCobrancas + recebimentoProdutoTotal,
        recebido: recebidoCobrancas + recebimentoProdutoTotal,
        pendentes: pendentesCobrancas,
        recebimentoProdutoTotal
    };
};

const formatarData = (dataString: string): string => {
    try {
        const data = new Date(dataString);
        return isNaN(data.getTime()) ? dataString : data.toLocaleDateString('pt-BR');
    } catch (error) {
        console.error('Erro ao formatar data:', error);
        return dataString;
    }
};

const AccountsReceivable = () => {
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [addCobrancaApi] = useAddAccountsReceivableMutation();
    const [updateCobrancaApi] = useUpdateAccountsReceivableMutation();
    const { data: cobrancas = [], refetch } = useGetAccountsReceivableQuery();
    const { data: rawVendas } = useGetVendasQuery();
    const vendas = rawVendas ? limparPropriedadesHibernate<Venda[]>(rawVendas) : [];


    const { total, recebido, pendentes, recebimentoProdutoTotal } = calcularTotais(cobrancas, vendas);

    const [form, setForm] = useState<FormState>({
        cliente: '',
        descricao: '',
        vencimento: '',
        valor: '',
        status: 'PENDENTE',
    });

    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
    ) => {
        const { name, value } = e.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const novaCobranca: Omit<Cobranca, 'id'> = {
            cliente: form.cliente,
            descricao: form.descricao,
            valor: parseFloat(form.valor) || 0,
            vencimento: form.vencimento,
            status: form.status,
        };

        try {
            await addCobrancaApi(novaCobranca).unwrap();
            setForm({
                cliente: '',
                descricao: '',
                vencimento: '',
                valor: '',
                status: 'PENDENTE'
            });
            setIsModalOpen(false);
            refetch();
        } catch (err) {
            const error = err as { data?: { message?: string }, message?: string };
            alert('Erro ao adicionar cobrança: ' + (error?.data?.message || error?.message || 'Erro desconhecido'));
        }
    };

    const handleUpdateStatus = async (id: number, currentStatus: 'PENDENTE' | 'PAGO') => {
        const newStatus = currentStatus === 'PENDENTE' ? 'PAGO' : 'PENDENTE';
        try {
            await updateCobrancaApi({ id, status: newStatus }).unwrap();
            refetch();
        } catch (err) {
            const error = err as { data?: { message?: string }, message?: string };
            alert('Erro ao atualizar status: ' + (error?.data?.message || error?.message || 'Erro desconhecido'));
        }
    };

    return (
        <Container>
            <TitleRow>
                <Title>📥 Contas a Receber</Title>
                <Button onClick={() => setIsModalOpen(true)}>+ Nova Cobrança</Button>
            </TitleRow>

            <Summary>
                <Card>
                    <CardTitle>Total a Receber</CardTitle>
                    <CardValue>R$ {total.toFixed(2)}</CardValue>
                </Card>
                <Card>
                    <CardTitle>Recebido</CardTitle>
                    <CardValue>R$ {recebido.toFixed(2)}</CardValue>
                </Card>
                <Card>
                    <CardTitle>Pendentes</CardTitle>
                    <CardValue>R$ {pendentes.toFixed(2)}</CardValue>
                </Card>
            </Summary>

            <Table>
                <thead>
                    <tr>
                        <Th>Cliente</Th>
                        <Th>Descrição</Th>
                        <Th>Vencimento</Th>
                        <Th>Valor</Th>
                        <Th>Status</Th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <Td>All</Td>
                        <Td>Produto venda</Td>
                        <Td>default</Td>
                        <Td>R$ {recebimentoProdutoTotal.toFixed(2)}</Td>
                        <Td><StatusBadge status='PAGO'>Recebido</StatusBadge></Td>
                    </tr>
                    {cobrancas.map((cobranca: { id: React.Key | null | undefined; cliente: string | number | bigint | boolean | React.ReactElement<unknown, string | React.JSXElementConstructor<any>> | Iterable<React.ReactNode> | React.ReactPortal | Promise<string | number | bigint | boolean | React.ReactPortal | React.ReactElement<unknown, string | React.JSXElementConstructor<any>> | Iterable<React.ReactNode> | null | undefined> | null | undefined; descricao: string | number | bigint | boolean | React.ReactElement<unknown, string | React.JSXElementConstructor<any>> | Iterable<React.ReactNode> | React.ReactPortal | Promise<string | number | bigint | boolean | React.ReactPortal | React.ReactElement<unknown, string | React.JSXElementConstructor<any>> | Iterable<React.ReactNode> | null | undefined> | null | undefined; vencimento: string; valor: number; status: string | number | bigint | boolean | React.ReactElement<unknown, string | React.JSXElementConstructor<any>> | Iterable<React.ReactNode> | Promise<string | number | bigint | boolean | React.ReactPortal | React.ReactElement<unknown, string | React.JSXElementConstructor<any>> | Iterable<React.ReactNode> | null | undefined> | null | undefined; }) => (
                        <tr key={cobranca.id}>
                            <Td>{cobranca.cliente}</Td>
                            <Td>{cobranca.descricao}</Td>
                            <Td>{formatarData(cobranca.vencimento)}</Td>
                            <Td>R$ {cobranca.valor.toFixed(2)}</Td>
                            <Td>
                                <StatusBadge
                                    status={cobranca.status as "PAGO" | "PENDENTE"}
                                    onClick={() => handleUpdateStatus(cobranca.id as number, cobranca.status as "PAGO" | "PENDENTE")}
                                >
                                    {cobranca.status}
                                </StatusBadge>
                            </Td>
                        </tr>
                    ))}
                </tbody>
            </Table>

            {isModalOpen && (
                <ModalOverlay>
                    <ModalContent onSubmit={handleSubmit}>
                        <ModalTitle>Nova Cobrança</ModalTitle>
                        <Label htmlFor="cliente">Cliente</Label>
                        <Input
                            name="cliente"
                            placeholder="Cliente"
                            value={form.cliente}
                            onChange={handleChange}
                            required
                        />
                        <Label htmlFor="descricao">Descrição</Label>
                        <Input
                            name="descricao"
                            placeholder="Descrição"
                            value={form.descricao}
                            onChange={handleChange}
                            required
                        />
                        <Label htmlFor="vencimento">Vencimento</Label>
                        <Input
                            name="vencimento"
                            type="date"
                            value={form.vencimento}
                            onChange={handleChange}
                            required
                        />
                        <Label htmlFor="valor">Valor</Label>
                        <Input
                            name="valor"
                            placeholder="Valor (ex: 1200.00)"
                            value={form.valor}
                            onChange={handleChange}
                            required
                            pattern="\d+(\.\d{2})?"
                            title="Digite um valor válido (ex: 1200.00)"
                        />
                        <Label htmlFor="status">Status</Label>
                        <Select
                            name="status"
                            value={form.status}
                            onChange={handleChange}
                            required
                        >
                            <option value='PENDENTE'>Pendente</option>
                            <option value='PAGO'>Pago</option>
                        </Select>
                        <ModalActions>
                            <CancelButton type="button" onClick={() => setIsModalOpen(false)}>
                                Cancelar
                            </CancelButton>
                            <SaveButton type="submit">Salvar</SaveButton>
                        </ModalActions>
                    </ModalContent>
                </ModalOverlay>
            )}
        </Container>
    );
};

export default AccountsReceivable;
