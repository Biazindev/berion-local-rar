import React from "react";
import styled from "styled-components";

type Props = {
  isOpen: boolean;
  onClose: () => void;
  onSelect: (tipo: "NFS" | "NFE") => void;
};

export const NotaFiscalModal: React.FC<Props> = ({ isOpen, onClose, onSelect }) => {
  if (!isOpen) return null;

  return (
    <Backdrop>
      <ModalContainer>
        <h2>Selecionar tipo de Nota Fiscal</h2>
        <Button onClick={() => onSelect("NFS")}>NFS-e (Serviço)</Button>
        <Button onClick={() => onSelect("NFE")}>NF-e (Produto)</Button>
        <CancelButton onClick={onClose}>Cancelar</CancelButton>
      </ModalContainer>
    </Backdrop>
  );
};

// === Styled ===
const Backdrop = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
`;

const ModalContainer = styled.div`
  background: white;
  padding: 2rem;
  border-radius: 12px;
  text-align: center;
  max-width: 400px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
`;

const Button = styled.button`
  background-color: #007bff;
  color: white;
  margin: 0.5rem 0;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;
  width: 100%;

  &:hover {
    background-color: #0056b3;
  }
`;

const CancelButton = styled(Button)`
  background-color: #999;

  &:hover {
    background-color: #666;
  }
`;
