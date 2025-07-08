import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface EmpresaState {
  inscricaoEstadual: string;
  regimeTributario: string;
  crt: any;
  fone: string;
  cnae: string;
  endereco: any;
  inscricaoMunicipal: string;
  cnpj: string;
  nomeFantasia: string;
  razaoSocial: string;
  id: number;
  nome: string;
}

const initialState: EmpresaState = {
  id: 1,
  nome: 'Minha Empresa',
  inscricaoEstadual: '',
  regimeTributario: '',
  crt: undefined,
  fone: '',
  cnae: '',
  endereco: undefined,
  inscricaoMunicipal: '',
  cnpj: '',
  nomeFantasia: '',
  razaoSocial: ''
};

const empresaSlice = createSlice({
  name: 'empresa',
  initialState,
  reducers: {
    setEmpresa(state, action: PayloadAction<EmpresaState>) {
      return action.payload;
    }
  }
});

export const { setEmpresa } = empresaSlice.actions;
export default empresaSlice.reducer;
