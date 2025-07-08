import React, { useEffect, useState } from 'react';
import styled from 'styled-components';

const Container = styled.div`
  width: 320px;
  margin: 60px auto;
  padding: 20px;
  border-radius: 15px;
  background: #1e1e2f;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.3);
`;

const Display = styled.div`
  background: #2c2c3c;
  color: #fff;
  font-size: 2rem;
  padding: 20px;
  border-radius: 10px;
  text-align: right;
  margin-bottom: 20px;
  min-height: 60px;
`;

const Grid = styled.div`
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
`;

const Button = styled.button<{ variant?: string }>`
  padding: 20px;
  font-size: 1.2rem;
  border: none;
  border-radius: 12px;
  background-color: ${({ variant }) =>
    variant === 'operator' ? '#f57c00' :
    variant === 'action' ? '#616161' :
    '#424242'};
  color: white;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    opacity: 0.85;
  }

  &:active {
    transform: scale(0.97);
  }
`;

const Calculator = () => {
  const [input, setInput] = useState<string>('');

  const handleClick = (value: string) => {
    if (value === 'C') {
      setInput('');
    } else if (value === '=') {
      try {
        setInput(eval(input).toString());
      } catch {
        setInput('Erro');
      }
    } else if (value === '←') {
      setInput(prev => prev.slice(0, -1));
    } else {
      setInput(prev => prev + value);
    }
  };

  // Mapeia teclas físicas para botões da calculadora
  const handleKeyDown = (e: KeyboardEvent) => {
    const key = e.key;

    if (key === 'Enter') {
      e.preventDefault();
      handleClick('=');
    } else if (key === 'Backspace') {
      handleClick('←');
    } else if (key === 'Escape') {
      handleClick('C');
    } else if (/^[0-9+\-*/.]$/.test(key)) {
      handleClick(key);
    }
  };

  useEffect(() => {
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  });

  const buttons = [
    'C', '/', '*', '←',
    '7', '8', '9', '-',
    '4', '5', '6', '+',
    '1', '2', '3', '=',
    '0', '.', '', ''
  ];

  return (
    <Container>
      <Display>{input || '0'}</Display>
      <Grid>
        {buttons.map((btn, index) =>
          btn ? (
            <Button
              key={index}
              onClick={() => handleClick(btn)}
              variant={
                ['/', '*', '-', '+', '='].includes(btn) ? 'operator'
                : ['C', '←'].includes(btn) ? 'action'
                : undefined
              }
            >
              {btn}
            </Button>
          ) : (
            <div key={index} />
          )
        )}
      </Grid>
    </Container>
  );
};

export default Calculator;
