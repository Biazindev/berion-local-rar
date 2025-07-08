import React, { useState } from 'react';
import styled from 'styled-components';

const plans = [
  { name: 'Essencial', base: 149, extra: 15 },
  { name: 'Pro', base: 199, extra: 20 },
  { name: 'Business', base: 259, extra: 25 },
  { name: 'Premium', base: 299, extra: 30 },
];

const Container = styled.div`
  padding: 4rem 2rem;
  font-family: 'Segoe UI', sans-serif;
  color: #1c1c1c;
  min-height: 100vh;
`;

const Header = styled.div`
  text-align: center;
  margin-bottom: 3rem;
`;

const Title = styled.h1`
  font-size: 3rem;
  color: #0d6efd;
  margin-bottom: 0.5rem;
`;

const Subtitle = styled.p`
  font-size: 1.25rem;
  color: #495057;
`;

const PlansGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
`;

const PlanCard = styled.div<{ active?: boolean }>`
  background: ${({ active }) => (active ? '#0d6efd' : '#ffffff')};
  border: 2px solid ${({ active }) => (active ? '#0a58ca' : '#dee2e6')};
  border-radius: 16px;
  padding: 2rem;
  cursor: pointer;
  color: ${({ active }) => (active ? '#ffffff' : '#212529')};
  transition: all 0.3s;
  box-shadow: ${({ active }) => (active ? '0 6px 20px rgba(13,110,253,0.4)' : '0 2px 10px rgba(0,0,0,0.05)')};

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 24px rgba(0,0,0,0.1);
  }
`;

const PlanTitle = styled.h3`
  font-size: 1.6rem;
  margin-bottom: 0.75rem;
`;

const Price = styled.p`
  font-size: 1.3rem;
  font-weight: bold;
`;

const Extra = styled.p`
  font-size: 1rem;
`;

const Controls = styled.div`
  max-width: 540px;
  margin: 0 auto;
  background: #ffffff;
  padding: 2.5rem;
  border-radius: 16px;
  box-shadow: 0 0 16px rgba(0,0,0,0.07);
  text-align: center;
`;

const Label = styled.label`
  display: block;
  margin-bottom: 0.75rem;
  font-size: 1.1rem;
  font-weight: 500;
`;

const Input = styled.input`
  width: 100px;
  padding: 0.6rem;
  margin-bottom: 1rem;
  font-size: 1.2rem;
  text-align: center;
  border: 2px solid #ced4da;
  border-radius: 8px;
  outline: none;

  &:focus {
    border-color: #0d6efd;
  }
`;

const Total = styled.p`
  font-size: 1.3rem;
  font-weight: 600;
  margin-top: 1rem;
  color: #0a58ca;
`;

const UpgradeButton = styled.button`
  margin-top: 2rem;
  background: linear-gradient(90deg, #0d6efd, #6610f2);
  color: #fff;
  padding: 0.9rem 2.5rem;
  font-size: 1.1rem;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    transform: scale(1.05);
    background: linear-gradient(90deg, #0b5ed7, #520dc2);
  }
`;

const Flat = () => {
  const [users, setUsers] = useState(2);
  const [activePlan, setActivePlan] = useState('Essencial');

  const selectedPlan = plans.find(plan => plan.name === activePlan)!;
  const additionalUsers = users > 2 ? users - 2 : 0;
  const total = selectedPlan.base + additionalUsers * selectedPlan.extra;

  return (
    <Container>
      <Header>
        <Title>Impulsione seu negócio com mais performance</Title>
        <Subtitle>Ganhe produtividade e controle total com os planos do <strong>BerionGestor</strong>.</Subtitle>
      </Header>

      <PlansGrid>
        {plans.map(plan => (
          <PlanCard
            key={plan.name}
            active={plan.name === activePlan}
            onClick={() => setActivePlan(plan.name)}
          >
            <PlanTitle>{plan.name}</PlanTitle>
            <Price>R$ {plan.base.toFixed(2)} <small>/ até 2 usuários</small></Price>
            <Extra>+ R$ {plan.extra.toFixed(2)} por usuário adicional</Extra>
          </PlanCard>
        ))}
      </PlansGrid>

      <Controls>
        <Label>Quantos usuários usarão o sistema?</Label>
        <Input
          type="number"
          min={2}
          value={users}
          onChange={(e) => setUsers(parseInt(e.target.value) || 2)}
        />
        <Total>
          Total Mensal: <strong>R$ {total.toFixed(2)}</strong>
        </Total>
        <UpgradeButton>
          Fazer Upgrade para {activePlan === 'Premium' ? 'o Plano Premium 🚀' : `o Plano ${activePlan}`}
        </UpgradeButton>
      </Controls>
    </Container>
  );
};

export default Flat;
