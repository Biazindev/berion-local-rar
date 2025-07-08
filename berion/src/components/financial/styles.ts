import { styled } from "styled-components";

export const Container = styled.div`
  padding: 3rem;
  width: 100%;
  display: grid;
  margin: 0 auto;
  color: #fff;
  background-color: #1e272e;
`;

export const Title = styled.h2`
  font-size: 2rem;
  margin-bottom: 2rem;
`;

export const Cards = styled.div`
  text-decoration: none;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 1rem;
  flex-wrap: wrap;
`;

export const Card = styled.div`
  text-decoration: none;
  background: #2f3640;
  border-radius: 10px;
  padding: 1rem 2rem;
  flex: 1;
  min-width: 250px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
`;

export const CardTitle = styled.p`
  font-size: 1rem;
  color: #dcdde1;
  text-decoration: none;
`;

export const Amount = styled.p<{ green?: boolean; red?: boolean }>`
  font-size: 1.6rem;
  font-weight: bold;
  text-decoration: none;
  color: ${({ green, red }) =>
    green ? '#44bd32' : red ? '#e84118' : '#fff'};
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
  border-collapse: collapse;
  background: #353b48;
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
