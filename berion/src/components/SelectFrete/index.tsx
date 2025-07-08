import React from 'react';
import { Container, Label, Select } from './styles';

export interface SelectFreteProps {
  value: string;
  onChange: (value: string) => void;
}


const tiposFrete = [
  { value: '0', label: '0 - Por conta do Remetente (CIF)' },
  { value: '1', label: '1 - Por conta do Destinatário (FOB)' },
  { value: '2', label: '2 - Por conta de Terceiros' },
  { value: '3', label: '3 - Transporte Próprio Remetente' },
  { value: '4', label: '4 - Transporte Próprio Destinatário' },
  { value: '9', label: '9 - Sem Ocorrência de Transporte' }
];

const SelectFrete: React.FC<SelectFreteProps> = ({ value, onChange }) => {
  return (
    <Container>
      <Label htmlFor="frete">Tipo de Frete:</Label>
      <Select
        id="frete"
        value={value}
        onChange={(e) => onChange(e.target.value)}
      >
        <option value="">Selecione o tipo de frete</option>
        {tiposFrete.map((tipo) => (
          <option key={tipo.value} value={tipo.value}>
            {tipo.label}
          </option>
        ))}
      </Select>
    </Container>
  );
};

export default SelectFrete;
