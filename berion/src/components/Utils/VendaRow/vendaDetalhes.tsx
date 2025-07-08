import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { VendaProps } from '../../SaleList';
import {
    useCancelarNotaMutation,
    useLazyBaixarDanfeNfeQuery,
} from '../../../services/api';
import {
    Container,
    Title,
    Info,
    Label,
    Value,
    StatusBadge,
    ButtonGroup,
    ButtonAction,
    CancelButton,
    Loading,
} from './styles';

const VendaDetalhes = () => {
    const { id } = useParams();
    const [venda, setVenda] = useState<VendaProps | null>(null);
    const [cancelarNota, { isLoading: cancelando }] = useCancelarNotaMutation();
    const [getDanfe] = useLazyBaixarDanfeNfeQuery();
    const [abrindoDanfe, setAbrindoDanfe] = useState(false);

    useEffect(() => {
        const vendaStorage = localStorage.getItem('vendaSelecionada');
        if (vendaStorage) setVenda(JSON.parse(vendaStorage));
    }, []);

    const abrirDanfe = async () => {
        if (!venda?.numeroProtocoloNfe) return;

        setAbrindoDanfe(true);
        try {
            const blob = await getDanfe(venda.numeroProtocoloNfe).unwrap();
            const url = window.URL.createObjectURL(blob);
            window.open(url, '_blank');
        } catch (error) {
            console.error('Erro ao abrir DANFE:', error);
            alert('Erro ao abrir DANFE');
        } finally {
            setAbrindoDanfe(false);
        }
    };

    const cancelar = async () => {
        if (!venda) return;
        const motivo = prompt('Informe o motivo do cancelamento:');
        if (!motivo) return;

        try {
            await cancelarNota({
                idVenda: venda.id, motivo,
                id: '',
                ambiente: '',
                status: '',
                numero: 0,
                serie: 0,
                modelo: 0,
                valor_total: 0,
                chave: ''
            }).unwrap();
            alert('Nota fiscal cancelada com sucesso!');
            setVenda({ ...venda, status: 'CANCELADA' });
        } catch (err) {
            console.error('Erro ao cancelar nota:', err);
            alert('Erro ao cancelar nota fiscal.');
        }
    };

    if (!venda) return <Loading>Carregando venda...</Loading>;

    return (
        <Container>
            <div>
                <Title>Detalhes da Venda #{venda.id}</Title>

                <Info>
                    <Label>Cliente:</Label>
                    <Value>{venda.cliente?.pessoaFisica?.nome ?? 'Venda Anônima'}</Value>
                </Info>

                <Info>
                    <Label>Status:</Label>
                    <StatusBadge status={venda.status}>{venda.status}</StatusBadge>
                </Info>

                <Info>
                    <Label>Total:</Label>
                    <Value>R$ {venda.totalVenda.toFixed(2)}</Value>
                </Info>

                <Info>
                    <Label>Forma de Pagamento:</Label>
                    <Value>{venda.pagamento?.formaPagamento ?? 'N/A'}</Value>
                </Info>

                {venda.chaveAcessoNfe && (
                    <ButtonGroup>
                        <ButtonAction onClick={abrirDanfe} disabled={abrindoDanfe}>
                            {abrindoDanfe ? 'Abrindo DANFE...' : 'Abrir DANFE'}
                        </ButtonAction>

                        <CancelButton onClick={cancelar} disabled={cancelando}>
                            {cancelando ? 'Cancelando...' : 'Cancelar Nota Fiscal'}
                        </CancelButton>
                    </ButtonGroup>
                )}
            </div>
        </Container>
    );
};

export default VendaDetalhes;
