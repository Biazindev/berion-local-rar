import { styled } from "styled-components";

export const Container = styled.div`
  padding: 4rem;
  background: #f8f9fa;
  min-height: 100vh;
  font-family: 'Segoe UI', sans-serif;
`;

export const TitleRow = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const Title = styled.h2`
  font-size: 2rem;
  color: #343a40;
  margin-bottom: 1rem;
`;

export const Button = styled.button`
  background-color: #198754;
  color: #fff;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;

  &:hover {
    background-color: #157347;
  }
`;

export const Summary = styled.div`
  display: flex;
  gap: 2rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
`;

export const Card = styled.div`
  flex: 1;
  background: #fff;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 0 10px rgba(0,0,0,0.05);
  min-width: 250px;
`;

export const CardTitle = styled.p`
  font-size: 1rem;
  color: #6c757d;
`;

export const CardValue = styled.h3`
  font-size: 1.8rem;
  color: #198754;
  margin: 0.5rem 0 0;
`;

export const Table = styled.table`
  width: 100%;
  border-collapse: collapse;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 0 10px rgba(0,0,0,0.05);
`;

export const Th = styled.th`
  text-align: left;
  padding: 1rem;
  background: #e9ecef;
  color: #495057;
`;

export const Td = styled.td`
  padding: 1rem;
  border-top: 1px solid #dee2e6;
  color: #343a40;
`;

export const StatusBadge = styled.span<{ status: 'PENDENTE' | 'PAGO' }>`
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.875rem;
  color: #fff;
  background-color: ${({ status }) =>
    status === 'PAGO' ? '#198754' : '#ffc107'};
`;

export const ModalOverlay = styled.div`
  position: fixed;
  inset: 0;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
`;

export const ModalContent = styled.form`
  background: #fff;
  padding: 2rem;
  border-radius: 12px;
  width: 100%;
  max-width: 500px;
  box-shadow: 0 5px 20px rgba(0,0,0,0.2);
`;

export const Select = styled.select`
  padding: 8px;
  margin-bottom: 12px;
  border-radius: 4px;
  border: 1px solid #ccc;
  font-size: 14px;
`;


export const ModalTitle = styled.h3`
  margin-bottom: 1rem;
  color: #343a40;
`;

export const Input = styled.input`
  width: 100%;
  padding: 0.75rem;
  margin-bottom: 1rem;
  border: 1px solid #ced4da;
  border-radius: 8px;
`;

export const ModalActions = styled.div`
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
`;

export const CancelButton = styled.button`
  background: #6c757d;
  color: #fff;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 8px;

  &:hover {
    background: #5a6268;
  }
`;

export const SaveButton = styled.button`
  background: #198754;
  color: #fff;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 8px;

  &:hover {
    background: #157347;
  }
`;