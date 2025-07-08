import { styled } from "styled-components";

// Container principal com um fundo sutil e espaçamento generoso
export const Container = styled.div`
  padding: 0 40px 0 56px;
  background-color: #f9fafb;
  min-height: 100vh;
  font-family: 'Inter', sans-serif;
`;

// Título com destaque elegante
export const Title = styled.h1`
  font-size: 32px;
  color: #1e293b;
  margin-bottom: 32px;
  font-weight: 700;
`;

// Tabela estilizada com bordas suaves e visual moderno
export const StyledTable = styled.table`
  width: 100%;
  border-collapse: collapse;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.06);
  overflow: hidden;
`;

// Cabeçalho com cores suaves e tipografia clara
export const Th = styled.th`
  background: #f1f5f9;
  color: #1e293b;
  padding: 16px 20px;
  text-align: left;
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  border-bottom: 1px solid #e2e8f0;
`;


// Dados com cor neutra e espaçamento adequado
export const Td = styled.td`
  padding: 16px 20px;
  border-bottom: 1px solid #e2e8f0;
  color: #000;
  font-size: 15px;
`;

// Linha com efeito hover chamativo, ideal para UX moderno
export const Tr = styled.tr`
  transition: background-color 0.25s ease;

  &:hover {
    background-color: #f8fafc;
  }
`;

// Botão de exclusão com destaque visual moderno
export const DeleteButton = styled.button`
  background-color: #ef4444;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
  transition: background-color 0.25s ease, transform 0.1s ease;

  &:hover {
    background-color: #dc2626;
    transform: scale(1.05);
  }

  &:active {
    transform: scale(0.98);
  }
`;
export const Table = styled.table`
  width: 100%;
  border-collapse: collapse;
  background-color: white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
`

// Mensagem de feedback discreta
export const Message = styled.p`
  color: #64748b;
  font-size: 14px;
  margin-top: 16px;
  text-align: center;
`;
