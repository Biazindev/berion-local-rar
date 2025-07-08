import styled from 'styled-components';

export const Container = styled.div`
  max-width: 900px;  
  width: 100%;  
  margin: 3rem auto;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
`;

export const Title = styled.h2`
  font-size: 1.8rem;
  color: #333;
  margin-bottom: 2rem;
`;

export const Info = styled.div`
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
  align-items: center;
`;

export const Label = styled.span`
  font-weight: 600;
  color: #555;
`;

export const Value = styled.span`
  color: #222;
`;

export const StatusBadge = styled.span<{ status: string }>`
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-weight: 600;
  font-size: 0.875rem;
  text-transform: uppercase;
  color: white;
  background-color: ${({ status }) =>
    status === 'CANCELADA'
      ? '#dc3545'
      : status === 'EMITIDA'
      ? '#28a745'
      : status === 'EM_PREPARO'
      ? '#ffc107'
      : '#6c757d'};
`;

export const ButtonGroup = styled.div`
  margin-top: 2rem;
  display: flex;
  gap: 1rem;
`;

export const ButtonAction = styled.button`
  background-color: #0d6efd;
  color: white;
  border: none;
  padding: 0.7rem 1.4rem;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.3s;

  &:hover {
    background-color: #084298;
  }

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
`;

export const CancelButton = styled(ButtonAction)`
  background-color: #dc3545;

  &:hover {
    background-color: #a71d2a;
  }
`;

export const Loading = styled.p`
  text-align: center;
  font-size: 1.2rem;
  color: #777;
  padding: 2rem;
`;
