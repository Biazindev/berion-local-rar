import { useState, useEffect } from 'react';
import { useAddNfMutation, useGetEmitenteQuery } from '../../services/api';
import { useSelector } from 'react-redux';
import { RootState } from '../../store/reducers';
import { Produto } from '../../store/reducers/vendaSlice';
import Loader from '../Loader';
import {
    Button,
    Container,
    ContainerButton,
    ErrorMessage,
    Form,
    Input,
    Label,
    SectionTitle,
    SuccessMessage,
} from '../Venda/styles';
import { Message } from './styles';

export interface NfDTO {
  vendaId: any;
  emitenteId: any;
  tipo?: 'NFS' | 'NFE';
}


export const NFComponent = () => {
    const [enviarNf, { isLoading, isSuccess, isError, error }] = useAddNfMutation();
    const { data: emitente } = useGetEmitenteQuery();
    const empresa = emitente?.[0] ?? null;
    const cliente = useSelector((state: RootState) => state.venda.cliente);
    const produtos: Produto[] = useSelector((state: RootState) => state.venda.produtos);
    const [tipoNota, setTipoNota] = useState<string | null>(null);
    const [emissaoAutomaticaRealizada, setEmissaoAutomaticaRealizada] = useState(false);

    const total = produtos.reduce((acc, p) => acc + parseFloat(p.precoUnitario.toString()) * p.quantidade, 0);

    useEffect(() => {
        const tipo = localStorage.getItem('tipoNotaFiscal');
        setTipoNota(tipo);
    }, []);

    useEffect(() => {
        const vendaId = localStorage.getItem('ultimaVendaId'); // ← você precisa salvar isso ao finalizar a venda
        if (tipoNota === 'NFE' && empresa?.id && vendaId && !emissaoAutomaticaRealizada) {
            const payload = {
                vendaId: Number(vendaId),
                emitenteId: empresa.id
            };

            enviarNf(payload)
                .unwrap()
                .then(() => {
                    console.log('NF-e emitida automaticamente');
                    setEmissaoAutomaticaRealizada(true);
                })
                .catch((e) => {
                    console.error('Erro ao emitir NF-e automaticamente:', e);
                });
        }
    }, [tipoNota, empresa, enviarNf, emissaoAutomaticaRealizada]);

    const handleClick = async () => {
        if (!empresa?.id) {
            console.error('ID da empresa não encontrado');
            return;
        }

        const vendaId = localStorage.getItem('ultimaVendaId');
        if (!vendaId) return;

        const nfeData: NfDTO = {
            vendaId: Number(vendaId),
            emitenteId: empresa.id
        };

        try {
            await enviarNf(nfeData).unwrap();
        } catch (e) {
            console.error('Erro ao emitir NF-e manualmente:', e);
        }
    };

    return (
        <Container>
            <Form>
                <SectionTitle>Nota Fiscal - {tipoNota === 'NFE' ? 'NF-e' : 'NFS-e'}</SectionTitle>

                {/* Exibe campos SOMENTE SE for NFS ou se não tiver tipo definido */}
                {(!tipoNota || tipoNota === 'NFS') && (
                    <>
                        {cliente?.pessoaFisica && (
                            <>
                                <Label>Nome</Label>
                                <Input value={cliente.pessoaFisica.nome || ''} readOnly />
                                <Label>CPF</Label>
                                <Input value={cliente.pessoaFisica.cpf || ''} readOnly />
                            </>
                        )}
                        {cliente?.pessoaJuridica && (
                            <>
                                <Label>Razão Social</Label>
                                <Input value={cliente.pessoaJuridica.razaoSocial || ''} readOnly />
                                <Label>CNPJ</Label>
                                <Input value={cliente.pessoaJuridica.cnpj || ''} readOnly />
                            </>
                        )}
                        <Label>Email</Label>
                        <Input value={cliente?.pessoaFisica?.email || cliente?.pessoaJuridica?.email || ''} readOnly />
                        <Label>Telefone</Label>
                        <Input value={cliente?.pessoaFisica?.telefone || cliente?.pessoaJuridica?.telefone || ''} readOnly />
                    </>
                )}

                <SectionTitle>Valor Total</SectionTitle>
                <Label>R$ {total.toFixed(2)}</Label>

                <SectionTitle>Emitente</SectionTitle>
                <Label>Razão Social</Label>
                <Input value={empresa?.razaoSocial ?? ''} readOnly />
                <Label>CNPJ</Label>
                <Input value={empresa?.cnpj ?? ''} readOnly />

                {/* Botão visível só se não for emissão automática */}
                {tipoNota !== 'NFE' && (
                    <ContainerButton>
                        <Button type="button" onClick={handleClick} disabled={isLoading}>
                            {isLoading ? 'Emitindo...' : 'Emitir Nota Fiscal'}
                        </Button>
                    </ContainerButton>
                )}

                {isLoading && <Message>Emitindo nota... <Loader /></Message>}
                {isSuccess && <SuccessMessage>Nota fiscal emitida com sucesso!</SuccessMessage>}
                {isError && <ErrorMessage>Erro ao emitir nota: {JSON.stringify(error)}</ErrorMessage>}
            </Form>
        </Container>
    );
};

export default NFComponent