import React from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

const Overlay = styled.div`
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
`;

const ModalContainer = styled.div`
  background: #ffffffcc;
  border-radius: 16px;
  padding: 2rem;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.25);
  text-align: center;
  animation: fadeIn 0.3s ease-in-out;

  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: scale(0.95);
    }
    to {
      opacity: 1;
      transform: scale(1);
    }
  }
`;

const Title = styled.h3`
  margin-bottom: 1.5rem;
  color: #333;
`;

const ButtonGroup = styled.div`
  display: flex;
  justify-content: center;
  gap: 1rem;
`;

const ActionButton = styled.button`
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 12px;
  font-size: 1rem;
  cursor: pointer;
  background-color: #32ff7e;
  color: #000;
  transition: all 0.2s;

  &:hover {
    background-color: #2ed870;
    transform: scale(1.05);
  }
`;

const SaleNavigationMenu: React.FC = () => {
  const navigate = useNavigate();

  return (
    <Overlay>
      <ModalContainer>
        <Title>Cliente salvo com sucesso! Escolha o próximo passo:</Title>
        <ButtonGroup>
          <ActionButton onClick={() => navigate('/produtos-cadastrar')}>Produtos</ActionButton>
          <ActionButton onClick={() => navigate('/ordem-servico')}>Ordem de Serviço</ActionButton>
        </ButtonGroup>
      </ModalContainer>
    </Overlay>
  );
};

export default SaleNavigationMenu;
