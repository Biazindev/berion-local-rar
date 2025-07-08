import { create } from "zustand";
import { persist } from "zustand/middleware";

type Todo = {
  text: string;
  done: boolean;
};

type TodoStore = {
  todos: Todo[];
  add: (text: string) => void;
  toggle: (index: number) => void;
  remove: (index: number) => void;
};

export const useTodoStore = create<TodoStore>()(
  persist(
    (set) => ({
      todos: [],
      add: (text) =>
        set((state) => ({
          todos: [...state.todos, { text, done: false }],
        })),
      toggle: (index) =>
        set((state) => {
          const updated = [...state.todos];
          updated[index].done = !updated[index].done;
          return { todos: updated };
        }),
      remove: (index) =>
        set((state) => ({
          todos: state.todos.filter((_, i) => i !== index),
        })),
    }),
    {
      name: "todo-storage", // nome da chave no localStorage
    }
  )
);
