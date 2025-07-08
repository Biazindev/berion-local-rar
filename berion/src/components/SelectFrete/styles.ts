import { styled } from "styled-components";


export const Container = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 1rem;
`;

export const Label = styled.label`
  font-weight: 600;
  margin-bottom: 0.5rem;
`;

export const Select = styled.select`
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1rem;
  background-color: white;
  transition: border 0.2s ease;

  &:focus {
    outline: none;
    border-color: #0077cc;
  }
`;
