import { useState, useEffect } from 'react';
import { useAddNfeMutation, useGetEmitenteQuery } from '../../services/api';
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

export interface EmitirNotaPayload {
    cpfCnpjTomador: string;
    nomeTomador: string;
    telefone: string;
    email: string;
    endereco: {
        cep: string;
        bairro: string;
        municipio: string;
        logradouro: string;
        numero: string;
        uf: string;
        complemento: string | null;
        codigoIbge: string;
    };
    servico: {
        descricao: string;
        valor: number;
        codigoTributacaoMunicipal: string;
        codigoTributacaoNacional: string;
        cnae: string;
        nbs: string;
        informacoesComplementares: string;
        locPrest: {
            cLocPrestacao: string;
            cPaisPrestacao: string;
        };
        cServ: {
            cTribNac: string;
            cTribMun: string;
            CNAE: string;
            xDescServ: string;
            cNBS: string;
        };
        infoCompl: {
            xInfComp: string;
            idDocTec: null;
            docRef: null;
        };
    };
    empresa: {
        razaoSocial: string;
        nomeFantasia: string;
        cnpj: string;
        inscricaoEstadual: string;
        inscricaoMunicipal: string;
        regimeTributario: string;
        cnae: string;
        crt: string;
        fone: string;
        endereco: {
            cep: string;
            bairro: string;
            municipio: string;
            logradouro: string;
            numero: string;
            uf: string;
            complemento: string | null;
        };
    };
}

export const NFSComponent = () => {
    const [enviarNota, { isLoading, isSuccess, isError, error }] = useAddNfeMutation();
    const { data: emitente } = useGetEmitenteQuery();
    const empresa = emitente?.[0] ?? null;
    const cliente = useSelector((state: RootState) => state.venda.cliente);
    const produtos: Produto[] = useSelector((state: RootState) => state.venda.produtos);

    const [tipoNota, setTipoNota] = useState<string | null>(null);
    const [emissaoAutomaticaRealizada, setEmissaoAutomaticaRealizada] = useState(false);

    // Estados omitidos para brevidade... (nome, cpf, cnpj, etc.)

    const total = produtos.reduce((acc, p) => acc + parseFloat(p.precoUnitario.toString()) * p.quantidade, 0);

    useEffect(() => {
        const tipo = localStorage.getItem('tipoNotaFiscal');
        setTipoNota(tipo);
    }, []);

    const handleEmitirNotaFiscalServ = async () => {
        if (!cliente || produtos.length === 0 || !empresa) return;

        const enderecoCliente = cliente.endereco || {};

        const payload: EmitirNotaPayload = {
            cpfCnpjTomador: cliente?.pessoaFisica?.cpf || cliente?.pessoaJuridica?.cnpj || '',
            nomeTomador: cliente?.pessoaFisica?.nome || cliente?.pessoaJuridica?.razaoSocial || '',
            telefone: cliente?.pessoaFisica?.telefone || cliente?.pessoaJuridica?.telefone || '',
            email: cliente?.pessoaFisica?.email || cliente?.pessoaJuridica?.email || '',
            endereco: {
                cep: enderecoCliente.cep || '',
                bairro: enderecoCliente.bairro || '',
                municipio: enderecoCliente.municipio || '',
                logradouro: enderecoCliente.logradouro || '',
                numero: enderecoCliente.numero || '',
                uf: enderecoCliente.uf || '',
                complemento: enderecoCliente.complemento || null,
                codigoIbge: enderecoCliente.codigoIbge || '',
            },
            servico: {
                descricao: 'Programação de sistemas sob demanda',
                valor: total,
                codigoTributacaoMunicipal: '103',
                codigoTributacaoNacional: '103',
                cnae: '6209100',
                nbs: '123456000',
                informacoesComplementares: 'Sistema ERP desenvolvido sob demanda e entregue via repositório Git privado.',
                locPrest: {
                    cLocPrestacao: enderecoCliente.codigoIbge || '',
                    cPaisPrestacao: 'BR',
                },
                cServ: {
                    cTribNac: '103',
                    cTribMun: '103',
                    CNAE: '6209100',
                    xDescServ: 'Programação de sistemas sob demanda',
                    cNBS: '123456000',
                },
                infoCompl: {
                    xInfComp: 'Sistema ERP desenvolvido sob demanda e entregue via repositório Git privado.',
                    idDocTec: null,
                    docRef: null,
                },
            },
            empresa: {
                razaoSocial: empresa.razaoSocial,
                nomeFantasia: empresa.nomeFantasia,
                cnpj: empresa.cnpj,
                inscricaoEstadual: empresa.inscricaoEstadual,
                inscricaoMunicipal: empresa.inscricaoMunicipal,
                regimeTributario: empresa.regimeTributario,
                cnae: empresa.cnae,
                crt: empresa.crt.toString(),
                fone: empresa.fone,
                endereco: {
                    ...empresa.endereco,
                    complemento: empresa.endereco.complemento ?? null
                }
            }
        };

        try {
            await enviarNota(payload).unwrap();
            setEmissaoAutomaticaRealizada(true);
        } catch (e) {
            console.error('Erro ao emitir NFS-e automaticamente:', e);
        }
    };

    // Dispara a emissão automática se tipoNota for NFS
    useEffect(() => {
        if (tipoNota === 'NFS' && !emissaoAutomaticaRealizada) {
            handleEmitirNotaFiscalServ();
        }
    }, [tipoNota, cliente, produtos, empresa, emissaoAutomaticaRealizada]);

    return (
        <Container>
            <Form>
                <SectionTitle>Nota Fiscal de Serviço - NFS-e</SectionTitle>

                {tipoNota !== 'NFS' && (
                    <>
                        {/* Exibe campos apenas se não for automático */}
                        {cliente?.pessoaFisica ? (
                            <>
                                <Label>Nome</Label>
                                <Input value={cliente.pessoaFisica.nome || ''} readOnly />
                                <Label>CPF</Label>
                                <Input value={cliente.pessoaFisica.cpf || ''} readOnly />
                            </>
                        ) : (
                            <>
                                <Label>Razão Social</Label>
                                <Input value={cliente?.pessoaJuridica?.razaoSocial || ''} readOnly />
                                <Label>CNPJ</Label>
                                <Input value={cliente?.pessoaJuridica?.cnpj || ''} readOnly />
                            </>
                        )}

                        <Label>Email</Label>
                        <Input value={cliente?.pessoaFisica?.email || cliente?.pessoaJuridica?.email || ''} readOnly />
                        <Label>Telefone</Label>
                        <Input value={cliente?.pessoaFisica?.telefone || cliente?.pessoaJuridica?.telefone || ''} readOnly />
                    </>
                )}

                <SectionTitle>Valor do Serviço</SectionTitle>
                <Label>R$ {total.toFixed(2)}</Label>

                {/* Botão só aparece no modo manual */}
                {tipoNota !== 'NFS' && (
                    <ContainerButton>
                        <Button 
                            type="button" 
                            onClick={handleEmitirNotaFiscalServ}
                            disabled={isLoading}
                        >
                            {isLoading ? 'Emitindo...' : 'Emitir NFS-e'}
                        </Button>
                    </ContainerButton>
                )}

                {isLoading && <Message>Emitindo nota de serviço... <Loader /></Message>}
                {isSuccess && <SuccessMessage>Nota fiscal de serviço emitida com sucesso!</SuccessMessage>}
                {isError && <ErrorMessage>Erro ao emitir nota: {JSON.stringify(error)}</ErrorMessage>}
            </Form>
        </Container>
    );
};
export default NFSComponent