import React, { useState } from 'react';
import { useCriarUsuarioMutation } from '../../services/api';
import { Perfil, Usuario } from '../User/User';


const Register = () => {
  const [formData, setFormData] = useState<Usuario>({
    username: '',
    nome: '',
    email: '',
    senha: '',
    perfil: 'COMUM',
  });

  const [criarUsuario, { isLoading, isSuccess, isError }] = useCriarUsuarioMutation();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData((prev: Usuario) => ({
      ...prev,
      [name]: name === 'perfil' ? (value as Perfil) : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await criarUsuario(formData).unwrap();
      alert('Usuário criado com sucesso!');
      setFormData({
        username: '',
        nome: '',
        email: '',
        senha: '',
        perfil: 'COMUM',
      });
    } catch (err) {
      console.error('Erro ao criar usuário:', err);
      alert('Erro ao criar usuário.');
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      style={{
        maxWidth: 400,
        margin: '2rem auto',
        display: 'flex',
        flexDirection: 'column',
        gap: '12px',
        padding: '2rem',
        border: '1px solid #ddd',
        borderRadius: '8px',
        backgroundColor: '#f9f9f9',
      }}
    >
      <h2 style={{ textAlign: 'center', marginBottom: '1rem' }}>Criar Usuário</h2>

      <input
        type="text"
        name="nome"
        placeholder="Nome"
        value={formData.nome}
        onChange={handleChange}
        required
        style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
      />
      <input
        type="text"
        name="username"
        placeholder="Usuário"
        value={formData.username}
        onChange={handleChange}
        required
        style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
      />
      <input
        type="email"
        name="email"
        placeholder="E-mail"
        value={formData.email}
        onChange={handleChange}
        required
        style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
      />
      <input
        type="password"
        name="senha"
        placeholder="Senha"
        value={formData.senha}
        onChange={handleChange}
        required
        style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
      />
      <select
        name="perfil"
        value={formData.perfil}
        onChange={handleChange}
        style={{ padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
      >
        <option value="ADMIN">ADMIN</option>
        <option value="COMUM">COMUM</option>
      </select>

      <button
        type="submit"
        disabled={isLoading}
        style={{
          padding: '10px',
          backgroundColor: '#007bff',
          color: '#fff',
          border: 'none',
          borderRadius: '4px',
          cursor: 'pointer',
        }}
      >
        {isLoading ? 'Criando...' : 'Criar Usuário'}
      </button>

      {isSuccess && <p style={{ color: 'green', textAlign: 'center' }}>Usuário criado com sucesso!</p>}
      {isError && <p style={{ color: 'red', textAlign: 'center' }}>Erro ao criar usuário.</p>}
    </form>
  );
};

export default Register;
