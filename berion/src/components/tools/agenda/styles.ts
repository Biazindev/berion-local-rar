import { styled } from "styled-components";

export const Container = styled.div`
  padding: 2rem;
  background-color: #1e272e;
  border-radius: 10px;
  color: #fff;
  max-width: 700px;
  margin: 0 auto;
`;

export const Title = styled.h2`
  font-size: 2rem;
  margin-bottom: 1rem;
`;

export const EventList = styled.ul`
  list-style: none;
  padding: 0;
`;

export const EventItem = styled.li`
  background-color: #485460;
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
`;
