import React, { useState } from 'react';
import {
    useGetEmitenteQuery,
    useConsultarNfeQuery,
    useLazyBaixarDanfeNfeQuery,
    useGetNfQuery,
} from '../../services/api';

export type NotaFiscal = {
    id: string;
    ambiente: string;
    status: string;
    data_emissao?: string;
    numero: number;
    serie: number;
    motivo: string;
    modelo: number;
    valor_total: number;
    idVenda: number;
    chave: string;
    autorizacao?: {
        numero_protocolo?: string;
        motivo_status?: string;
    }
    cliente?: {
        pessoaFisica?: {
            nome: string;
            cpf: string;
        };
        pessoaJuridica?: {
            razaoSocial: string;
            cnpj: string;
        };
    };
}

const TaxEnvironment: React.FC = () => {
    const { data: empresas } = useGetEmitenteQuery();
    const { data: apiResponse, isLoading: loadingNotas } = useGetNfQuery('1');
    const [notaId, setNotaId] = useState<string>('');
    const [notaDetalhes, setNotaDetalhes] = useState<NotaFiscal | null>(null);
    const [danfeUrl, setDanfeUrl] = useState<string | null>(null);
    const [modalAberto, setModalAberto] = useState<boolean>(false);
    const [paginaAtual, setPaginaAtual] = useState<number>(1);
    const [itensPorPagina] = useState<number>(5);
    const [filtro, setFiltro] = useState<string>('');

    const [getDanfe] = useLazyBaixarDanfeNfeQuery();
    const { data: consultaNfe, isFetching: loadingConsulta, refetch } = useConsultarNfeQuery(notaId, {
        skip: !notaId,
    });

    const handleBuscarNota = async (id?: string) => {
        const finalId = id || notaId;
        if (!finalId) return;

        try {
            setNotaId(finalId);
            const { data } = await refetch();
            if (data) {
                setNotaDetalhes(data as unknown as NotaFiscal);
            }
        } catch (error) {
            console.error('Erro ao buscar nota:', error);
            alert('Erro ao buscar nota fiscal.');
        }
    };

    const abrirDanfeModal = async () => {
        if (!notaId) {
            alert('Nenhuma nota selecionada');
            return;
        }

        try {
            const response = await getDanfe(notaId);
            if (response.data) {
                const url = URL.createObjectURL(response.data);
                setDanfeUrl(url);
                setModalAberto(true);
            }
        } catch (error) {
            console.error('Erro ao baixar DANFE:', error);
            alert('Erro ao abrir DANFE. Por favor, tente novamente.');
        }
    };
    const notasFiscais = apiResponse?.data || [];

    const notasFiltradas = notasFiscais.filter((nota: NotaFiscal) => {
        const nome = nota.cliente?.pessoaFisica?.nome || nota.cliente?.pessoaJuridica?.razaoSocial || '';
        const doc = nota.cliente?.pessoaFisica?.cpf || nota.cliente?.pessoaJuridica?.cnpj || '';
        return nome.toLowerCase().includes(filtro.toLowerCase()) || doc.includes(filtro);
    });

    const totalPaginas = Math.ceil(notasFiltradas.length / itensPorPagina);
    const inicio = (paginaAtual - 1) * itensPorPagina;
    const notasPaginadas = notasFiltradas.slice(inicio, inicio + itensPorPagina);

    return (
        <div style={{ maxWidth: 900, margin: '2rem auto', padding: '1.5rem', border: '1px solid #ddd', borderRadius: 8 }}>
            <h2 style={{ textAlign: 'center', marginBottom: 20 }}>Notas Fiscais Emitidas</h2>

            <input
                type="text"
                placeholder="Filtrar por nome ou documento"
                value={filtro}
                onChange={(e) => {
                    setFiltro(e.target.value);
                    setPaginaAtual(1);
                }}
                style={{ width: '100%', padding: 8, marginBottom: 20 }}
            />

            {loadingNotas ? (
                <p>Carregando notas fiscais...</p>
            ) : (
                <>
                    <table style={{ width: '100%', borderCollapse: 'collapse', marginBottom: 20 }}>
                        <thead>
                            <tr>
                                <th style={{ border: '1px solid #ccc', padding: 8 }}>Cliente</th>
                                <th style={{ border: '1px solid #ccc', padding: 8 }}>Documento</th>
                                <th style={{ border: '1px solid #ccc', padding: 8 }}>Ação</th>
                            </tr>
                        </thead>
                        <tbody>
                            {notasPaginadas.map((nota: NotaFiscal) => (
                                <tr key={nota.id}>
                                    <td style={{ border: '1px solid #ccc', padding: 8 }}>
                                        {nota.cliente?.pessoaFisica?.nome || nota.cliente?.pessoaJuridica?.razaoSocial || '—'}
                                    </td>
                                    <td style={{ border: '1px solid #ccc', padding: 8 }}>
                                        {nota.cliente?.pessoaFisica?.cpf || nota.cliente?.pessoaJuridica?.cnpj || '—'}
                                    </td>
                                    <td style={{ border: '1px solid #ccc', padding: 8 }}>
                                        <button
                                            onClick={() => handleBuscarNota(nota.id)}
                                            style={{
                                                padding: '6px 12px',
                                                backgroundColor: '#007bff',
                                                color: '#fff',
                                                border: 'none',
                                                borderRadius: 4,
                                                cursor: 'pointer',
                                            }}
                                        >
                                            Abrir Nota
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    <div style={{ display: 'flex', justifyContent: 'center', gap: 10, marginBottom: 20 }}>
                        <button
                            onClick={() => setPaginaAtual((prev) => Math.max(prev - 1, 1))}
                            disabled={paginaAtual === 1}
                        >
                            Anterior
                        </button>
                        <span>Página {paginaAtual} de {totalPaginas}</span>
                        <button
                            onClick={() => setPaginaAtual((prev) => Math.min(prev + 1, totalPaginas))}
                            disabled={paginaAtual === totalPaginas}
                        >
                            Próxima
                        </button>
                    </div>
                </>
            )}

            {loadingConsulta && <p>Consultando nota...</p>}

            {notaDetalhes && (
                <>
                    <h3>Detalhes da Nota Fiscal</h3>
                    <table style={{ width: '100%', borderCollapse: 'collapse', marginBottom: 20 }}>
                        <tbody>
                            {[
                                ['ID', notaDetalhes.id],
                                ['Ambiente', notaDetalhes.ambiente],
                                ['Status', notaDetalhes.status],
                                ['Data de Emissão', notaDetalhes.data_emissao ? new Date(notaDetalhes.data_emissao).toLocaleString() : '—'],
                                ['Número', notaDetalhes.numero],
                                ['Série', notaDetalhes.serie],
                                ['Modelo', notaDetalhes.modelo],
                                ['Valor Total', `R$ ${notaDetalhes.valor_total?.toFixed(2)}`],
                                ['Chave de Acesso', notaDetalhes.chave],
                                ['Protocolo', notaDetalhes.autorizacao?.numero_protocolo || '—'],
                            ].map(([label, value]) => (
                                <tr key={label as string}>
                                    <td style={{ padding: 8, border: '1px solid #ccc', fontWeight: 'bold' }}>{label}</td>
                                    <td style={{ padding: 8, border: '1px solid #ccc' }}>{value}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    <button
                        onClick={abrirDanfeModal}
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#28a745',
                            color: '#fff',
                            border: 'none',
                            borderRadius: 4,
                            marginBottom: 20,
                            cursor: 'pointer',
                        }}
                    >
                        Visualizar DANFE
                    </button>
                </>
            )}

            {modalAberto && danfeUrl && (
                <div style={{
                    position: 'fixed',
                    top: 0,
                    left: 0,
                    width: '100%',
                    height: '100%',
                    backgroundColor: 'rgba(0,0,0,0.5)',
                    display: 'flex',
                    justifyContent: 'center',
                    alignItems: 'center',
                    zIndex: 9999,
                }}>
                    <div style={{
                        backgroundColor: '#fff',
                        padding: 20,
                        borderRadius: 8,
                        width: '90%',
                        height: '90%',
                        overflow: 'auto',
                        position: 'relative',
                    }}>
                        <button
                            onClick={() => {
                                setModalAberto(false);
                                if (danfeUrl) {
                                    URL.revokeObjectURL(danfeUrl);
                                }
                            }}
                            style={{
                                position: 'absolute',
                                top: 10,
                                right: 10,
                                padding: '6px 12px',
                                backgroundColor: '#ccc',
                                border: 'none',
                                borderRadius: 4,
                                cursor: 'pointer'
                            }}
                        >
                            Fechar
                        </button>
                        <iframe src={danfeUrl} title="DANFE" width="100%" height="100%" style={{ border: 'none' }} />
                    </div>
                </div>
            )}
        </div>
    );
};

export default TaxEnvironment;