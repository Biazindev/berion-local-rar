import styled from 'styled-components';

export const Container = styled.div`
  max-width: 700px;
  margin: 2rem auto;
  padding: 1.5rem;
  border: 1px solid #ddd;
  border-radius: 8px;
`;

export const Title = styled.h2`
  text-align: center;
  margin-bottom: 20px;
`;

export const Input = styled.input`
  width: 100%;
  padding: 8px;
  margin-bottom: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
`;

export const Button = styled.button<{ $primary?: boolean }>`
  padding: 10px 20px;
  background-color: ${props => props.$primary ? '#007bff' : '#28a745'};
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: ${props => props.$primary ? '#0056b3' : '#218838'};
  }

  &:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }
`;

export const LoadingText = styled.p`
  text-align: center;
`;

export const DetailsTable = styled.table`
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;

  td {
    padding: 8px;
    border: 1px solid #ccc;
  }
`;

export const BoldCell = styled.td`
  font-weight: bold;
`;

export const ButtonContainer = styled.div`
  display: flex;
  gap: 10px;
`;

export const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
`;

export const ModalContent = styled.div`
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  width: 90%;
  height: 90%;
  overflow: auto;
  position: relative;
`;

export const CloseButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 6px 12px;
  background-color: #ccc;
  border: none;
  border-radius: 4px;
  cursor: pointer;
`;

export const PDFViewer = styled.iframe`
  width: 100%;
  height: 100%;
  border: none;
`;

export const TabContainer = styled.div`
  margin-top: 20px;
`;

export const TabButtons = styled.div`
  display: flex;
  margin-bottom: 15px;
`;

export const TabButton = styled.button<{ $active: boolean }>`
  padding: 10px 15px;
  background: ${props => props.$active ? '#f0f0f0' : 'transparent'};
  border: 1px solid #ddd;
  border-bottom: ${props => props.$active ? '2px solid #007bff' : '1px solid #ddd'};
  cursor: pointer;
  margin-right: 5px;

  &:last-child {
    margin-right: 0;
  }
`;

export const SearchContainer = styled.div`
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
`;

export const SearchInput = styled(Input)`
  flex: 1;
  margin-bottom: 0;
`;