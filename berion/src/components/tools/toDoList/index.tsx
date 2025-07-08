import React, { useEffect, useState } from "react";
import { useTodoStore } from "../../../store/useTodoStore";
import {
  Container,
  Title,
  Form,
  Input,
  Button,
  List,
  ListItem,
  RemoveBtn,
} from "./styles";


const LOCAL_STORAGE_KEY = "todo_list";

export const getPendingTodoCount = (): number => {
  const saved = localStorage.getItem(LOCAL_STORAGE_KEY);
  if (!saved) return 0;
  const todos = JSON.parse(saved);
  return todos.filter((t: { done: boolean }) => !t.done).length;
};

const TodoList = () => {
  const [input, setInput] = useState("");

  const todos = useTodoStore((state) => state.todos);
  const add = useTodoStore((state) => state.add);
  const toggle = useTodoStore((state) => state.toggle);
  const remove = useTodoStore((state) => state.remove);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (input.trim()) {
      add(input.trim());
      setInput("");
    }
  };

  return (
    <Container>
      <Title>📋 Lista de Tarefas</Title>
      <Form onSubmit={handleSubmit}>
        <Input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Digite uma tarefa..."
        />
        <Button type="submit">Adicionar</Button>
      </Form>
      <List>
        {todos.map((todo, index) => (
          <ListItem
            key={index}
            done={todo.done}
            onClick={() => toggle(index)}
          >
            {todo.text}
            <RemoveBtn onClick={(e) => {
              e.stopPropagation();
              remove(index);
            }}>🗑️</RemoveBtn>
          </ListItem>
        ))}
      </List>
    </Container>
  );
};

export default TodoList;
