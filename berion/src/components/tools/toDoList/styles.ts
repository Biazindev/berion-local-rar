import { styled } from "styled-components";

export const Container = styled.div`
  max-width: 500px;
  margin: 60px auto;
  padding: 40px;
  border-radius: 16px;
  background: #fefefe;
  box-shadow: 0 4px 16px rgba(0,0,0,0.1);
`;

export const Title = styled.h2`
  text-align: center;
  margin-bottom: 24px;
  color: #333;
`;

export const Form = styled.form`
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
`;

export const Input = styled.input`
  flex: 1;
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #ccc;
  font-size: 16px;
`;

export const Button = styled.button`
  padding: 12px 18px;
  background: #4caf50;
  color: white;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.3s;

  &:hover {
    background: #43a047;
  }
`;

export const List = styled.ul`
  list-style: none;
  padding: 0;
`;

export const ListItem = styled.li<{ done: boolean }>`
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: ${({ done }) => (done ? "#dcedc8" : "#f0f0f0")};
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 10px;
  text-decoration: ${({ done }) => (done ? "line-through" : "none")};
`;

export const RemoveBtn = styled.button`
  background: transparent;
  border: none;
  color: #e53935;
  font-size: 18px;
  cursor: pointer;

  &:hover {
    color: #b71c1c;
  }
`;