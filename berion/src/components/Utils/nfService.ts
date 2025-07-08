import { NfDTO } from '../NotaFiscal/NFComponents'; 
import { api } from '../../services/api';

const enviarNf = api.endpoints.addNf.initiate;

export const emitirNotaFiscal = async (nfeData: NfDTO) => {
  try {
    const response = await enviarNf(nfeData);
    if ('error' in response) {
      console.error('Erro ao emitir NF-e:', response.error);
    } else {
      console.log('NF-e emitida com sucesso!');
    }
  } catch (error) {
    console.error('Erro na emissão da nota:', error);
  }
};
