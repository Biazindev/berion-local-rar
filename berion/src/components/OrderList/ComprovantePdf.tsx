import React from 'react';
import {
    Page,
    Text,
    View,
    Document,
    StyleSheet,
    Font
} from '@react-pdf/renderer';

const styles = StyleSheet.create({
    page: {
        padding: 30,
        fontSize: 12,
        fontFamily: 'Helvetica',
        color: '#333',
    },
    header: {
        borderBottom: '2px solid #1890ff',
        marginBottom: 20,
        paddingBottom: 10,
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    title: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#1890ff',
    },
    empresa: {
        fontSize: 12,
    },
    section: {
        marginBottom: 15,
    },
    label: {
        fontWeight: 'bold',
        marginBottom: 2,
    },
    valor: {
        textAlign: 'right',
        fontSize: 14,
        color: '#1890ff',
        marginTop: 10,
    },
    footer: {
        borderTop: '1px dashed #ccc',
        marginTop: 20,
        paddingTop: 10,
        textAlign: 'center',
        fontSize: 10,
        color: '#999',
    },
});

interface Endereco {
    cep: string;
    logradouro: string;
    numero: string;
    bairro: string;
    municipio: string;
    uf: string;
    complemento?: string;
}

interface Props {
    ordem: {
        id: number;
        nomeCliente: string;
        documento: string;
        email: string;
        telefone: string;
        endereco: Endereco;
        descricao: string;
        status: string;
        dataAbertura: string;
        dataConclusao?: string | null;
        valor: number;
    };
}

const ComprovantePDF: React.FC<Props> = ({ ordem }) => (
    <Document>
        <Page size="A4" style={styles.page}>
            <View style={styles.header}>
                <View style={styles.empresa}>
                    <Text>Empresa XPTO</Text>
                    <Text>CNPJ: 00.000.000/0000-00</Text>
                    <Text>contato@xpto.com.br</Text>
                </View>
                <View>
                    <Text style={styles.title}>Comprovante de Ordem de Serviço</Text>
                    <Text>OS Nº {ordem.id}</Text>
                    <Text>Data: {ordem.dataAbertura}</Text>
                </View>
            </View>

            <View style={styles.section}>
                <Text style={styles.label}>Cliente:</Text>
                <Text>{ordem.nomeCliente}</Text>
                <Text>{ordem.documento}</Text>
                <Text>{ordem.email}</Text>
                <Text>{ordem.telefone}</Text>
                <Text>
                    {ordem.endereco.logradouro}, {ordem.endereco.numero}{' '}
                    {ordem.endereco.complemento && `(${ordem.endereco.complemento})`}
                </Text>
                <Text>
                    {ordem.endereco.bairro}, {ordem.endereco.municipio} - {ordem.endereco.uf},{' '}
                    CEP: {ordem.endereco.cep}
                </Text>
                <Text style={styles.label}>Descrição:</Text>
                <Text>{ordem.descricao}</Text>

                <Text style={styles.label}>Status:</Text>
                <Text>{ordem.status.replace('_', ' ')}</Text>

                {ordem.dataConclusao && (
                    <>
                        <Text style={styles.label}>Data de Conclusão:</Text>
                        <Text>{ordem.dataConclusao}</Text>
                    </>
                )}

                <Text style={styles.valor}>Total: R$ {ordem.valor.toFixed(2)}</Text>
            </View>

            <View style={styles.footer}>
                <Text>Este comprovante não possui valor fiscal.</Text>
                <Text>Obrigado por confiar na Berion Gestor!</Text>
            </View>
        </Page>
    </Document>
);

export default ComprovantePDF;
