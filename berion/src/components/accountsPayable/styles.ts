import { styled } from "styled-components";

export const Container = styled.div`
  padding: 3rem;
  background: #1e272e;
  color: #fff;
  width: 100%;
  margin: 0 auto;
`;

export const Title = styled.h2`
  font-size: 2rem;
  margin-bottom: 2rem;
`;

export const Cards = styled.div`
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
`;

export const Card = styled.div`
  background: #2f3640;
  padding: 1rem 2rem;
  border-radius: 10px;
  min-width: 200px;
  flex: 1;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.3);
`;

export const CardTitle = styled.p`
  color: #dcdde1;
`;

export const CardValue = styled.p<{ red?: boolean; green?: boolean }>`
  font-size: 1.6rem;
  font-weight: bold;
  color: ${({ red, green }) =>
    red ? '#e84118' : green ? '#44bd32' : '#00cec9'};
`;

export const Section = styled.section`
  margin-top: 2rem;
`;

export const SectionTitle = styled.h3`
  font-size: 1.4rem;
  margin-bottom: 1rem;
`;

export const Table = styled.table`
  width: 100%;
  background: #353b48;
  border-collapse: collapse;
  border-radius: 8px;
  overflow: hidden;

  th, td {
    padding: 0.75rem 1rem;
    text-align: left;
  }

  th {
    background-color: #2f3640;
    color: #f5f6fa;
    font-weight: 600;
  }

  td {
    border-top: 1px solid #485460;
    color: #f1f2f6;
  }

  tr:hover {
    background-color: #2f3640;
  }
`;

export const StatusTag = styled.span<{ status: string }>`
  padding: 0.25rem 0.5rem;
  display: flex;
  justify-content: center;
  width: 80px;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: 500;
  color: #fff;
  background-color: ${({ status }) =>
    status === 'Vencida' ? '#e84118' : '#44bd32'};
`;
export const ModalOverlay = styled.div`
  position: fixed;
  top: 0; left: 0;
  width: 100vw; height: 100vh;
  background: rgba(0,0,0,0.4);
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const ModalContent = styled.div`
  background: #fff;
  border-radius: 12px;
  padding: 2rem;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 0 20px rgba(0,0,0,0.2);
`;

export const ModalTitle = styled.h2`
  font-size: 1.5rem;
  color: #343a40;
  margin-bottom: 1rem;
`;

export const ModalForm = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const Input = styled.input`
  padding: 0.75rem;
  border-radius: 8px;
  border: 1px solid #ced4da;
  font-size: 1rem;
`;

export const Select = styled.select`
  padding: 0.75rem;
  border-radius: 8px;
  border: 1px solid #ced4da;
  font-size: 1rem;
`;

export const Button = styled.button`
  background-color: #0d6efd;
  color: #fff;
  padding: 0.75rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;

  &:hover {
    background-color: #0b5ed7;
  }
`;
