import { useEffect, useState, useRef } from 'react'
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
import { RootState } from '../../store/reducers';
import { Message, Navbar, NavItem } from './styles'
import { useAddNfeMutation, useGetEmitenteQuery } from '../../services/api'
import { useAddNfMutation } from '../../services/api'
import { useDispatch, useSelector } from 'react-redux'
import { Produto, setCliente, setProdutos } from '../../store/reducers/vendaSlice'
import { Cliente, EmitirNotaPayload } from '../Venda/types';
import Loader from '../Loader';
import { BsFileEarmarkSpreadsheetFill } from 'react-icons/bs';


export interface NfDTO {
    vendaId: any;
    emitenteId: any;
}

export interface ItemVendaDTO {
    id?: number;
    produtoId?: number;
    nomeProduto?: string;
    precoUnitario?: string;
    quantidade?: number;
    totalItem?: string;
}


const NfContainer = () => {
    const [selectedType, setSelectedType] = useState<'Nota Fiscal de Serviço' | 'Nota Fiscal' | 'Cupom Fiscal'>('Nota Fiscal de Serviço');
    const [enviarNota, { isLoading, isSuccess, isError, error, data: resposta }] = useAddNfeMutation();
    const [enviarNf] = useAddNfMutation();
    const { data: emitente } = useGetEmitenteQuery();
    const empresa = emitente?.[0] ?? null;
    const cliente: Cliente | null = useSelector((state: RootState) => state.venda.cliente);
    const produtos: Produto[] = useSelector((state: RootState) => state.venda.produtos);
    const dispatch = useDispatch();

    // Estados para os campos do formulário
    const [nome, setNome] = useState('');
    const [cpf, setCpf] = useState('');
    const [dataNascimento, setDataNascimento] = useState('');
    const [razaoSocial, setRazaoSocial] = useState('');
    const [nomeFantasia, setNomeFantasia] = useState('');
    const [cnpj, setCnpj] = useState('');
    const [email, setEmail] = useState('');
    const [telefone, setTelefone] = useState('');
    const [logradouro, setLogradouro] = useState('');
    const [numero, setNumero] = useState('');
    const [bairro, setBairro] = useState('');
    const [cep, setCep] = useState('');
    const [uf, setUf] = useState('');
    const [complemento, setComplemento] = useState('');
    const [inscricaoEstadual, setInscricaoEstadual] = useState('');
    const [inscricaoMunicipal, setInscricaoMunicipal] = useState('');
    const [codigoIbge, setCodigoIbge] = useState('');
    const [municipio, setMunicipio] = useState('');
    const [pais, setPais] = useState('');

    const containerRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        containerRef.current?.scrollIntoView({
            behavior: 'smooth',// você pode testar 'center' também
        });
    }, [])

    useEffect(() => {
        const loadFromLocalStorage = () => {
            try {
                // Primeiro tenta carregar da venda completa
                const vendaCompleta = localStorage.getItem('ultimaVenda');
                if (vendaCompleta) {
                    const parsedVenda = JSON.parse(vendaCompleta);

                    // Se existir cliente na venda completa
                    if (parsedVenda.cliente) {
                        setClienteData(parsedVenda.cliente);
                        dispatch(setCliente(parsedVenda.cliente));
                    }

                    // Se existir produtos na venda completa
                    if (parsedVenda.produtos) {
                        dispatch(setProdutos(parsedVenda.produtos));
                    }

                    return;
                }

                // Se não encontrar venda completa, tenta carregar os dados individuais
                const clienteString = localStorage.getItem('clienteVenda') || localStorage.getItem('clienteSelecionado');
                const produtosString = localStorage.getItem('produtosVenda') || localStorage.getItem('produtosSelecionados');

                if (clienteString) {
                    const parsedCliente = JSON.parse(clienteString);
                    setClienteData(parsedCliente);
                    dispatch(setCliente(parsedCliente));
                }

                if (produtosString) {
                    const parsedProdutos = JSON.parse(produtosString);
                    dispatch(setProdutos(parsedProdutos));
                }
            } catch (err) {
                console.error("Erro ao carregar dados do localStorage:", err);
            }
        };

        const setClienteData = (clienteData: any) => {
            if (clienteData?.pessoaFisica) {
                setNome(clienteData.pessoaFisica.nome || '');
                setCpf(clienteData.pessoaFisica.cpf || '');
                setEmail(clienteData.pessoaFisica.email || '');
                setTelefone(clienteData.pessoaFisica.telefone || '');
                setDataNascimento(clienteData.dataNascimento || '');
                setInscricaoEstadual(clienteData.inscricaoEstadual || '');
                setInscricaoMunicipal(clienteData.inscricaoMunicipal || '');

                const end = clienteData.endereco || {};
                setLogradouro(end.logradouro || '');
                setNumero(end.numero || '');
                setBairro(end.bairro || '');
                setCep(end.cep || '');
                setUf(end.uf || '');
                setComplemento(end.complemento || '');
                setCodigoIbge(end.codigoIbge);
                setMunicipio(end.municipio);
            }

            if (clienteData?.pessoaJuridica) {
                setRazaoSocial(clienteData.pessoaJuridica.razaoSocial || '');
                setNomeFantasia(clienteData.nomeFantasia || '');
                setCnpj(clienteData.pessoaJuridica.cnpj || '');
                setEmail(clienteData.pessoaJuridica.email || '');
                setTelefone(clienteData.pessoaJuridica.telefone || '');
                setInscricaoEstadual(clienteData.inscricaoEstadual || '');
                setInscricaoMunicipal(clienteData.inscricaoMunicipal || '');

                const end = clienteData.endereco || {};
                setLogradouro(end.logradouro || '');
                setNumero(end.numero || '');
                setBairro(end.bairro || '');
                setCep(end.cep || '');
                setUf(end.uf || '');
                setComplemento(end.complemento || '');
                setCodigoIbge(end.codigoIbge || '');
                setMunicipio(end.municipio || '');
            }
        };

        loadFromLocalStorage();
    }, [dispatch]);

    const total = produtos.reduce((acc, p) => acc + parseFloat(p.precoUnitario.toString()) * p.quantidade, 0);

    const handleEmitir = async (nfeData: NfDTO) => {
        const payload = {
            vendaId: nfeData.vendaId,  
            emitenteId: nfeData.emitenteId
        };

        try {
            await enviarNf(payload).unwrap();
        } catch (e) {
            console.error('Erro ao emitir nota fiscal:', e);
        }
    };

    const handleClick = async () => {
        if (!empresa?.id) {
            console.error('ID da empresa não encontrado');
            return;
        }

        const nfeData: NfDTO = {
            vendaId: 1,
            emitenteId: empresa.id
        };

        await handleEmitir(nfeData);
    };



    const handleEmitirNotaFiscal = async (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        console.error('clique', e);

        if (!cliente || produtos.length === 0) return;

        const payload: EmitirNotaPayload = {
            cpfCnpjTomador: cpf || cnpj,
            nomeTomador: nome || razaoSocial,
            telefone,
            email,
            endereco: {
                cep,
                bairro,
                municipio,
                logradouro,
                numero,
                uf,
                complemento: complemento || null,
                codigoIbge,
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
                    cLocPrestacao: codigoIbge,
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
                razaoSocial: '',
                nomeFantasia: '',
                cnpj: '',
                inscricaoEstadual: '',
                inscricaoMunicipal: '',
                regimeTributario: '',
                cnae: '',
                crt: '',
                fone: '',
                endereco: {
                    cep: '',
                    bairro: '',
                    municipio: '',
                    logradouro: '',
                    numero: '',
                    uf: '',
                    complemento: null
                }
            }
        };

        try {
            await enviarNota(payload).unwrap();
        } catch (e) {
            console.error('Erro ao emitir nota fiscal:', e);
        }
    };

    const handleEmitirNotaFiscalServ = async (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        console.log('⚠️ Verificando cliente e produtos', { cliente, produtos });

        if (!cliente || produtos.length === 0) {
            console.warn('⛔ Bloqueado: cliente ou produtos ausentes');
            return;
        }

        console.log('✅ Vai montar payload...');


        if (!cliente || produtos.length === 0) return;

        const payload: EmitirNotaPayload = {
            cpfCnpjTomador: cpf || cnpj,
            nomeTomador: nome || razaoSocial,
            telefone,
            email,
            endereco: {
                cep,
                bairro,
                municipio,
                logradouro,
                numero,
                uf,
                complemento: empresa!.endereco.complemento ?? null,
                codigoIbge,
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
                    cLocPrestacao: codigoIbge,
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
                razaoSocial: empresa!.razaoSocial,
                nomeFantasia: empresa!.nomeFantasia,
                cnpj: empresa!.cnpj,
                inscricaoEstadual: empresa!.inscricaoEstadual,
                inscricaoMunicipal: empresa!.inscricaoMunicipal,
                regimeTributario: empresa!.regimeTributario,
                cnae: empresa!.cnae,
                crt: empresa!.crt.toString(),
                fone: empresa!.fone,
                endereco: {
                    ...empresa!.endereco,
                    complemento: empresa!.endereco.complemento ?? null
                }
            }
        };

        console.log('⚠️ Verificando cliente e produtos', { cliente, produtos });

        if (!cliente || produtos.length === 0) {
            console.warn('⛔ Bloqueado: cliente ou produtos ausentes');
            return;
        }

        console.log('✅ Vai montar payload...');

        try {
            await enviarNota(payload).unwrap();
        } catch (e) {
            console.error('Erro ao emitir nota fiscal:', e);
        }
    };



    return (
        <Container ref={containerRef}>
            <Navbar>
                {['Nota Fiscal de Serviço', 'Nota Fiscal', 'Cupom Fiscal'].map((type) => (
                    <NavItem
                        key={type}
                        active={selectedType === type}
                        onClick={() => setSelectedType(type as 'Nota Fiscal de Serviço' | 'Nota Fiscal' | 'Cupom Fiscal')}
                    >
                        {type}
                    </NavItem>
                ))}
            </Navbar>

            {selectedType === 'Nota Fiscal de Serviço' && (
                <Form>
                    <SectionTitle>Nota Fiscal de serviço - NFS-e</SectionTitle>

                    {cliente?.pessoaFisica && (
                        <>
                            <Label>Nome</Label>
                            <Input value={nome} onChange={(e) => setNome(e.target.value)} />
                            <Label>CPF</Label>
                            <Input value={cpf} onChange={(e) => setCpf(e.target.value)} />
                            <Label>Data de Nascimento</Label>
                            <Input type="date" value={dataNascimento} onChange={(e) => setDataNascimento(e.target.value)} />
                        </>
                    )}

                    {cliente?.pessoaJuridica && (
                        <>
                            <Label>Razão Social</Label>
                            <Input value={razaoSocial} onChange={(e) => setRazaoSocial(e.target.value)} />
                            <Label>Nome Fantasia</Label>
                            <Input value={nomeFantasia} onChange={(e) => setNomeFantasia(e.target.value)} />
                            <Label>CNPJ</Label>
                            <Input value={cnpj} onChange={(e) => setCnpj(e.target.value)} />
                            <Label>Inscrição Estadual</Label>
                            <Input value={inscricaoEstadual} onChange={(e) => setInscricaoEstadual(e.target.value)} />
                            <Label>Inscrição Municipal</Label>
                            <Input value={inscricaoMunicipal} onChange={(e) => setInscricaoMunicipal(e.target.value)} />
                        </>
                    )}

                    <Label>Email</Label>
                    <Input value={email} onChange={(e) => setEmail(e.target.value)} />

                    <Label>Telefone</Label>
                    <Input value={telefone} onChange={(e) => setTelefone(e.target.value)} />

                    <SectionTitle>Endereço</SectionTitle>
                    <Label>Logradouro</Label>
                    <Input value={logradouro} onChange={(e) => setLogradouro(e.target.value)} />

                    <Label>Número</Label>
                    <Input value={numero} onChange={(e) => setNumero(e.target.value)} />

                    <Label>Complemento</Label>
                    <Input value={complemento} onChange={(e) => setComplemento(e.target.value)} />

                    <Label>Bairro</Label>
                    <Input value={bairro} onChange={(e) => setBairro(e.target.value)} />

                    <Label>CEP</Label>
                    <Input value={cep} onChange={(e) => setCep(e.target.value)} />

                    <Label>Município</Label>
                    <Input value={municipio} onChange={(e) => setMunicipio(e.target.value)} />

                    <Label>UF</Label>
                    <Input value={uf} onChange={(e) => setUf(e.target.value)} />

                    <Label>Código IBGE</Label>
                    <Input value={codigoIbge} onChange={(e) => setCodigoIbge(e.target.value)} />

                    <Label>País</Label>
                    <Input value={pais} onChange={(e) => setPais(e.target.value)} />

                    <SectionTitle>Emitente</SectionTitle>
                    <Label>Razão Social</Label>
                    <Input value={empresa?.razaoSocial ?? ''} readOnly />
                    <Label>Nome Fantasia</Label>
                    <Input value={empresa?.nomeFantasia ?? ''} readOnly />
                    <Label>CNPJ</Label>
                    <Input value={empresa?.cnpj ?? ''} readOnly />
                    <Label>Inscrição Estadual</Label>
                    <Input value={empresa?.inscricaoEstadual ?? ''} readOnly />

                    <ContainerButton>
                        <Button type="button" onClick={handleEmitirNotaFiscalServ}>
                            Emitir Nota
                        </Button>
                    </ContainerButton>

                    {isLoading && <Message>Emitindo nota...<Loader /></Message>}
                    {isSuccess && resposta && <SuccessMessage>Nota emitida com sucesso</SuccessMessage>}
                    {isError && <ErrorMessage>Erro ao emitir nota: {JSON.stringify(error)}</ErrorMessage>}
                </Form>
            )}

            {selectedType === 'Nota Fiscal' && (
                <Form>
                    <SectionTitle>Nota Fiscal - NF-e</SectionTitle>

                    {cliente?.pessoaFisica && (
                        <>
                            <Label>Nome</Label>
                            <Input value={nome} onChange={(e) => setNome(e.target.value)} />
                            <Label>CPF</Label>
                            <Input value={cpf} onChange={(e) => setCpf(e.target.value)} />
                            <Label>Data de Nascimento</Label>
                            <Input type="date" value={dataNascimento} onChange={(e) => setDataNascimento(e.target.value)} />
                        </>
                    )}

                    {cliente?.pessoaJuridica && (
                        <>
                            <Label>Razão Social</Label>
                            <Input value={razaoSocial} onChange={(e) => setRazaoSocial(e.target.value)} />
                            <Label>Nome Fantasia</Label>
                            <Input value={nomeFantasia} onChange={(e) => setNomeFantasia(e.target.value)} />
                            <Label>CNPJ</Label>
                            <Input value={cnpj} onChange={(e) => setCnpj(e.target.value)} />
                            <Label>Inscrição Estadual</Label>
                            <Input value={inscricaoEstadual} onChange={(e) => setInscricaoEstadual(e.target.value)} />
                            <Label>Inscrição Municipal</Label>
                            <Input value={inscricaoMunicipal} onChange={(e) => setInscricaoMunicipal(e.target.value)} />
                        </>
                    )}

                    <Label>Email</Label>
                    <Input value={email} onChange={(e) => setEmail(e.target.value)} />

                    <Label>Telefone</Label>
                    <Input value={telefone} onChange={(e) => setTelefone(e.target.value)} />

                    <SectionTitle>Endereço</SectionTitle>
                    <Label>Logradouro</Label>
                    <Input value={logradouro} onChange={(e) => setLogradouro(e.target.value)} />

                    <Label>Número</Label>
                    <Input value={numero} onChange={(e) => setNumero(e.target.value)} />

                    <Label>Complemento</Label>
                    <Input value={complemento} onChange={(e) => setComplemento(e.target.value)} />

                    <Label>Bairro</Label>
                    <Input value={bairro} onChange={(e) => setBairro(e.target.value)} />

                    <Label>CEP</Label>
                    <Input value={cep} onChange={(e) => setCep(e.target.value)} />

                    <Label>Município</Label>
                    <Input value={municipio} onChange={(e) => setMunicipio(e.target.value)} />

                    <Label>UF</Label>
                    <Input value={uf} onChange={(e) => setUf(e.target.value)} />

                    <Label>Código IBGE</Label>
                    <Input value={codigoIbge} onChange={(e) => setCodigoIbge(e.target.value)} />

                    <Label>País</Label>
                    <Input value={pais} onChange={(e) => setPais(e.target.value)} />

                    <SectionTitle>Emitente</SectionTitle>
                    <Label>Razão Social</Label>
                    <Input value={empresa?.razaoSocial ?? ''} readOnly />
                    <Label>Nome Fantasia</Label>
                    <Input value={empresa?.nomeFantasia ?? ''} readOnly />
                    <Label>CNPJ</Label>
                    <Input value={empresa?.cnpj ?? ''} readOnly />
                    <Label>Inscrição Estadual</Label>
                    <Input value={empresa?.inscricaoEstadual ?? ''} readOnly />

                    <ContainerButton>
                        <Button
                            type="button"
                            onClick={handleClick}
                        >
                            Emitir Nota
                        </Button>
                    </ContainerButton>

                    {isLoading && <Message>Emitindo nota...<Loader /></Message>}
                    {isSuccess && resposta && <SuccessMessage>Nota emitida com sucesso</SuccessMessage>}
                    {isError && <ErrorMessage>Erro ao emitir nota: {JSON.stringify(error)}</ErrorMessage>}
                </Form>
            )}

            {selectedType === 'Cupom Fiscal' && (
                <Form>
                    <SectionTitle>Cupom Fiscal - CF-e</SectionTitle>

                    {cliente?.pessoaFisica && (
                        <>
                            <Label>Nome</Label>
                            <Input value={nome} onChange={(e) => setNome(e.target.value)} />
                            <Label>CPF</Label>
                            <Input value={cpf} onChange={(e) => setCpf(e.target.value)} />
                            <Label>Data de Nascimento</Label>
                            <Input type="date" value={dataNascimento} onChange={(e) => setDataNascimento(e.target.value)} />
                            <Label>Inscrição Estadual</Label>
                            <Input value={inscricaoEstadual} onChange={(e) => setInscricaoEstadual(e.target.value)} />
                            <Label>Inscrição Municipal</Label>
                            <Input value={inscricaoMunicipal} onChange={(e) => setInscricaoMunicipal(e.target.value)} />
                        </>
                    )}

                    {cliente?.pessoaJuridica && (
                        <>
                            <Label>Razão Social</Label>
                            <Input value={razaoSocial} onChange={(e) => setRazaoSocial(e.target.value)} />
                            <Label>Nome Fantasia</Label>
                            <Input value={nomeFantasia} onChange={(e) => setNomeFantasia(e.target.value)} />
                            <Label>CNPJ</Label>
                            <Input value={cnpj} onChange={(e) => setCnpj(e.target.value)} />
                            <Label>Inscrição Estadual</Label>
                            <Input value={inscricaoEstadual} onChange={(e) => setInscricaoEstadual(e.target.value)} />
                            <Label>Inscrição Municipal</Label>
                            <Input value={inscricaoMunicipal} onChange={(e) => setInscricaoMunicipal(e.target.value)} />
                        </>
                    )}

                    <Label>Email</Label>
                    <Input value={email} onChange={(e) => setEmail(e.target.value)} />

                    <Label>Telefone</Label>
                    <Input value={telefone} onChange={(e) => setTelefone(e.target.value)} />

                    <SectionTitle>Endereço</SectionTitle>
                    <Label>Logradouro</Label>
                    <Input value={logradouro} onChange={(e) => setLogradouro(e.target.value)} />

                    <Label>Número</Label>
                    <Input value={numero} onChange={(e) => setNumero(e.target.value)} />

                    <Label>Complemento</Label>
                    <Input value={complemento} onChange={(e) => setComplemento(e.target.value)} />

                    <Label>Bairro</Label>
                    <Input value={bairro} onChange={(e) => setBairro(e.target.value)} />

                    <Label>CEP</Label>
                    <Input value={cep} onChange={(e) => setCep(e.target.value)} />

                    <Label>Município</Label>
                    <Input value={municipio} onChange={(e) => setMunicipio(e.target.value)} />

                    <Label>UF</Label>
                    <Input value={uf} onChange={(e) => setUf(e.target.value)} />

                    <Label>Código IBGE</Label>
                    <Input value={codigoIbge} onChange={(e) => setCodigoIbge(e.target.value)} />

                    <Label>País</Label>
                    <Input value={pais} onChange={(e) => setPais(e.target.value)} />

                    <SectionTitle>Emitente</SectionTitle>
                    <Label>Razão Social</Label>
                    <Input value={empresa?.razaoSocial ?? ''} readOnly />
                    <Label>Nome Fantasia</Label>
                    <Input value={empresa?.nomeFantasia ?? ''} readOnly />
                    <Label>CNPJ</Label>
                    <Input value={empresa?.cnpj ?? ''} readOnly />
                    <Label>Inscrição Estadual</Label>
                    <Input value={empresa?.inscricaoEstadual ?? ''} readOnly />

                    <ContainerButton>
                        <Button type="button" onClick={handleEmitirNotaFiscal}>
                            Emitir Nota
                        </Button>
                    </ContainerButton>

                    {isLoading && <Message>Emitindo nota...<Loader /></Message>}
                    {isSuccess && resposta && <SuccessMessage>Nota emitida com sucesso</SuccessMessage>}
                    {isError && <ErrorMessage>Erro ao emitir nota: {JSON.stringify(error)}</ErrorMessage>}
                </Form>
            )}
        </Container>
    );
};

export default NfContainer;