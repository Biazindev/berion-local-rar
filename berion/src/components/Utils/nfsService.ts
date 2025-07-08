import { EmitirNotaPayload } from '../NotaFiscal/NFSComponent';
import { api } from '../../services/api';

const enviarNota = api.endpoints.addNfe.initiate;

export const emitirNotaFiscalServico = async (
  cliente: any,
  produtos: any[],
  empresa: any
): Promise<void> => {
  if (!cliente || produtos.length === 0 || !empresa) return;

  const enderecoCliente = cliente.endereco || {};
  const total = produtos.reduce((acc, p) => acc + parseFloat(p.precoUnitario) * p.quantidade, 0);

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
        complemento: empresa.endereco.complemento ?? null,
      },
    },
  };

  try {
    await enviarNota(payload);
    console.log('NFS-e emitida com sucesso');
  } catch (e) {
    console.error('Erro ao emitir NFS-e:', e);
  }
};
