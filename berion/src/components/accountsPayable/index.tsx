import React, { useState } from 'react';
import { 
  useGetProdutosQuery, 
  useAddAccountsPayableMutation, 
  useGetAccountsPayableQuery 
} from '../../services/api';
import {
    Card, Cards, CardTitle, CardValue, Container,
    Section, SectionTitle, StatusTag, Table,
    ModalOverlay, ModalContent, ModalTitle, ModalForm,
    Input, Select, Button, Title
} from './styles';
import Loader from '../Loader';
import { Label } from '../OrderList/styles';

interface Produto {
    precoCusto?: number;
    [key: string]: any;
}

interface Conta {
    id: number;
    fornecedor: string;
    valor: number;
    vencimento: string;
    dataPagamento: string;
    status: 'A_VENCER' | 'VENCIDA' | 'PAGO' | 'PENDENTE' | 'ATRASADO';
}

interface FormState {
    fornecedor: string;
    valor: string;
    vencimento: string;
    dataPagamento: string;
    status: 'A_VENCER' | 'VENCIDA' | 'PAGO' | 'PENDENTE' | 'ATRASADO';
}

const statusMap: Record<string, string> = {
    A_VENCER: "A Vencer",
    VENCIDA: "Vencida",
    PAGO: "Pago",
    PENDENTE: "Pendente",
    ATRASADO: "Atrasado",
};

const AccountsPayable: React.FC = () => {
    const [isOpen, setIsOpen] = useState(false);
    const { data: produtos } = useGetProdutosQuery();
    const [addContaApi] = useAddAccountsPayableMutation();
    const { data: contas = [], isLoading, isError } = useGetAccountsPayableQuery();
    
    
    const custoProdutoTotal: number = produtos?.reduce(
        (total: number, produto: Produto) => total + (produto.precoCusto || 0),
        0
    ) || 0;

    const [form, setForm] = useState<FormState>({
        fornecedor: '',
        valor: '',
        vencimento: '',
        dataPagamento: '',
        status: 'PENDENTE'
    });

    const handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
    ) => {
        const { name, value } = e.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const novaConta: Conta = {
            id: Date.now(),
            fornecedor: form.fornecedor,
            valor: parseFloat(form.valor),
            vencimento: form.vencimento,
            dataPagamento: form.dataPagamento,
            status: form.status,
        };

        try {
            await addContaApi({
                fornecedor: novaConta.fornecedor,
                valor: novaConta.valor,
                vencimento: novaConta.vencimento,
                dataPagamento: novaConta.dataPagamento,
                status: novaConta.status
            }).unwrap();
            
            setForm({
                fornecedor: '',
                valor: '',
                vencimento: '',
                dataPagamento: '',
                status: 'PENDENTE'
            });
            setIsOpen(false);
        } catch (err: unknown) {
            const error = err as { message?: string };
            alert('Erro ao adicionar conta: ' + (error?.message || 'Erro desconhecido'));
        }
    };

    const total = contas.reduce((sum: any, c: { valor: any; }) => sum + c.valor, 0) + custoProdutoTotal;
     const vencidas = contas
    .filter((c: { status: string; }) => c.status === 'VENCIDA')
    .reduce((sum: any, c: { valor: any; }) => sum + c.valor, 0);

  const aVencer = contas
    .filter((c: { status: string; }) => c.status === 'A_VENCER')
    .reduce((sum: any, c: { valor: any; }) => sum + c.valor, 0);

    function formatarData(dataString: string, formato: 'curto' | 'extenso' = 'curto'): string {
        try {
            const data = new Date(dataString);

            if (isNaN(data.getTime())) {
                throw new Error('Data inválida');
            }

            if (formato === 'extenso') {
                return data.toLocaleDateString('pt-BR', {
                    day: 'numeric',
                    month: 'long',
                    year: 'numeric'
                });
            } else {
                return data.toLocaleDateString('pt-BR');
            }
        } catch (error) {
            console.error('Erro ao formatar data:', error);
            return dataString;
        }
    }

    if (isLoading) return <div><Loader /></div>;
    if (isError) return <div>Erro ao carregar contas a pagar</div>;

    return (
        <Container>
            <Title>📤 Contas a Pagar</Title>
            <Button style={{ marginBottom: '8px' }} onClick={() => setIsOpen(true)}>
                + Nova Conta
            </Button>

            <Cards>
                <Card>
                    <CardTitle>Total</CardTitle>
                    <CardValue>R$ {total.toFixed(2)}</CardValue>
                </Card>
                <Card>
                    <CardTitle>Vencidas</CardTitle>
                    <CardValue red>R$ {vencidas.toFixed(2)}</CardValue>
                </Card>
                <Card>
                    <CardTitle>A Vencer</CardTitle>
                    <CardValue green>R$ {aVencer.toFixed(2)}</CardValue>
                </Card>
            </Cards>

            <Section>
                <SectionTitle>📄 Lista de Contas</SectionTitle>
                <Table>
                    <thead>
                        <tr>
                            <th>Fornecedor</th>
                            <th>Valor</th>
                            <th>Vencimento</th>
                            <th>Data do pagamento</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Custo Produtos</td>
                            <td>R$ {custoProdutoTotal.toFixed(2)}</td>
                            <td></td>
                            <td></td>
                            <td><StatusTag status="Pago">Pago</StatusTag></td>
                        </tr>
                        {contas?.map((conta: Conta) => (
                            <tr key={conta.id}>
                                <td>{conta.fornecedor}</td>
                                <td>R$ {conta.valor.toFixed(2)}</td>
                                <td>{formatarData(conta.vencimento)}</td>
                                <td>{formatarData(conta.dataPagamento)}</td>
                                <td>
                                    <StatusTag status={conta.status}>
                                        {statusMap[conta.status]}
                                    </StatusTag>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            </Section>

            {isOpen && (
                <ModalOverlay>
                    <ModalContent>
                        <ModalTitle>Nova Conta a Pagar</ModalTitle>
                        <ModalForm onSubmit={handleSubmit}>
                            <Label htmlFor="fornecedor">Fornecedor</Label>
                            <Input
                                name="fornecedor"
                                placeholder="Fornecedor"
                                value={form.fornecedor}
                                onChange={handleChange}
                                required
                            />
                            <Label htmlFor="valor">Valor</Label>
                            <Input
                                name="valor"
                                placeholder="Valor"
                                type="number"
                                value={form.valor}
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
                            <Label htmlFor="dataPagamento">Data de pagamento</Label>
                            <Input
                                name="dataPagamento"
                                type="date"
                                value={form.dataPagamento}
                                onChange={handleChange}
                                required
                            />
                            <Label htmlFor="status">Status pagamento</Label>
                            <Select name="status" value={form.status} onChange={handleChange}>
                                <option value="A_VENCER">A Vencer</option>
                                <option value="VENCIDA">Vencida</option>
                                <option value="PAGO">Pago</option>
                                <option value="PENDENTE">Pendente</option>
                                <option value="ATRASADO">Atrasado</option>
                            </Select>
                            <Button type="submit">Cadastrar</Button>
                            <Button type="button" onClick={() => setIsOpen(false)} style={{ backgroundColor: '#6c757d' }}>
                                Cancelar
                            </Button>
                        </ModalForm>
                    </ModalContent>
                </ModalOverlay>
            )}
        </Container>
    );
};

export default AccountsPayable;