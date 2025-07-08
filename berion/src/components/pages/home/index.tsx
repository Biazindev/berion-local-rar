import React from 'react';
import styled from 'styled-components';
import { FiUsers, FiShoppingCart, FiFileText, FiSettings } from 'react-icons/fi';
import { ContainerDashTodo, InfoBox } from '../../Dashboard/styles';
import { Link } from 'react-router-dom';
import { useTodoStore } from "../../../store/useTodoStore";

const mockUser = 'Usuario'

const Container = styled.div`
  padding: 3rem;
  background: linear-gradient(to right, #f8f9fa, #e9ecef);
  min-height: 100vh;
  font-family: 'Segoe UI', sans-serif;
  color: #212529;
`;

const Welcome = styled.div`
  margin-bottom: 2rem;
  text-align: center;
`;

const Greeting = styled.h1`
  font-size: 2.5rem;
  color: #198754;
`;

const SubGreeting = styled.p`
  font-size: 1.2rem;
  color: #6c757d;
`;

const QuickAccess = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(230px, 1fr));
  gap: 2rem;
  margin-top: 2rem;
`;

const Card = styled.div`
  background: #fff;
  border-radius: 16px;
  padding: 2rem;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  text-align: center;
  transition: transform 0.2s ease;
  cursor: pointer;

  &:hover {
    transform: translateY(-5px);
  }

  svg {
    font-size: 2rem;
    color: #198754;
    margin-bottom: 1rem;
  }

  h3 {
    font-size: 1.3rem;
    color: #343a40;
    margin-bottom: 0.5rem;
  }

  p {
    font-size: 0.95rem;
    color: #6c757d;
  }
`;

const Home = () => {
  const user = mockUser;
  const todos = useTodoStore((state: { todos: any; }) => state.todos);
  const pendingCount = todos.filter((t: { done: any; }) => !t.done).length;

  return (
    <Container>
      <ContainerDashTodo>
        <Link to={'/todolist'} style={{ textDecoration: 'none' }}>
          <InfoBox>
            Você tem <strong>{pendingCount}</strong> tarefa{pendingCount !== 1 ? "s" : ""} pendente{pendingCount !== 1 ? "s" : ""}.
          </InfoBox>
        </Link>
      </ContainerDashTodo>
      <Welcome>
        <Greeting>Olá, {user}! Seja bem-vindo</Greeting>
        <SubGreeting>Gerencie seu negócio de forma prática, inteligente e eficiente.</SubGreeting>
      </Welcome>
      <QuickAccess>
        <Link to={'/get'} style={{ textDecoration: 'none' }}>
          <Card>
            <FiUsers />
            <h3>Clientes</h3>
            <p>Gerencie sua base de clientes com facilidade.</p>
          </Card>
        </Link>
        <Link to={'/pdv-mesa'} style={{ textDecoration: 'none' }}>
          <Card>
            <FiShoppingCart />
            <h3>Vendas</h3>
            <p>Visualize e controle suas vendas rapidamente.</p>
          </Card>
        </Link>
        <Link to={'/#'} style={{ textDecoration: 'none' }}>
          <Card>
            <FiFileText />
            <h3>Notas Fiscais</h3>
            <p>Emita NF-e, NFC-e e acompanhe seu histórico fiscal.</p>
          </Card>
        </Link>
        <Link to={'/#'} style={{ textDecoration: 'none' }}>
          <Card>
            <FiSettings />
            <h3>Configurações</h3>
            <p>Personalize sua experiência no sistema.</p>
          </Card>
        </Link>
      </QuickAccess>
    </Container>
  );
};

export default Home;
