# 💼 Simplifica Contábil - Frontend

📄 **Licença**: Distribuído sob a licença MIT. 🔒 [Veja a licença completa](LICENSE)

Frontend do sistema ERP **Simplifica Contábil**, desenvolvido em **React + TypeScript**, com foco em usabilidade, escalabilidade e integração com o backend Java Spring Boot. Esta aplicação web permite gerenciar clientes, produtos, pedidos, finanças, emissão de DARFs e pedidos de delivery/pizzaria.

---

## 🚀 Tecnologias utilizadas

- ⚛️ React
- 💬 TypeScript
- 🔄 Redux Toolkit (incluindo RTK Query)
- 💅 Styled Components
- ⚙️ Vite
- 💡 React Router DOM
- 🔐 Autenticação com JWT + refresh token
- 📈 Recharts (gráficos)
- 📸 @zxing/browser (leitura de código de barras)
- 📁 Arquitetura modular

---

## 🔧 Funcionalidades principais

- 👤 **Gestão de Clientes**
  - Pessoa Física e Jurídica
  - Consulta automática de CNPJ/CPF
  - CRUD completo

- 📦 **Produtos e Estoque**
  - Cadastro manual e via leitura de código de barras
  - Importação por XML de nota/cupom fiscal
  - Alerta de estoque mínimo

- 🍕 **Gestão de Pedidos (Pizzaria / Delivery)**
  - Por mesa, balcão e entrega
  - Comandas impressas e organização por status
  - Motoboy e controle de entregas

- 💰 **Controle Financeiro**
  - Contas a pagar e a receber
  - Emissão de recibos em PDF com QR Code Pix
  - Visualização por gráficos (diário, semanal, mensal, anual)

- 🔐 **Autenticação Segura**
  - Login com JWT
  - Sessão com refresh token
  - Proteção de rotas via middleware

---

## 🛠️ Como rodar localmente

### ✅ Pré-requisitos

- Node.js 18+
- npm ou yarn
- Backend rodando (Spring Boot API)

---

### 🔄 Clone o repositório

```bash
git clone https://github.com/seu-usuario/simplifica-contabil-frontend.git
cd simplifica-contabil-frontend

##📦 Instale as dependências
npm install
# ou
yarn install

##⚙️Variaveis de ambiente
VITE_API_BASE_URL=http://localhost:8080/api

##▶️ Execute o projeto

npm run dev
# ou
yarn dev
##📁 Estrutura de Pastas

src/
├── api/             # Endpoints RTK Query
├── assets/          # Logos, ícones, imagens
├── components/      # Componentes reutilizáveis
├── features/        # Domínios (clientes, produtos, pedidos, etc)
├── hooks/           # Custom hooks
├── pages/           # Páginas principais
├── redux/           # Store e slices
├── routes/          # Rotas e proteção de rotas
├── services/        # Serviços auxiliares (auth, localStorage)
├── styles/          # Estilização global e tema
└── main.tsx         # Ponto de entrada

##🔐 Autenticação

const { data: user, error } = useLoginMutation({
  username: 'admin',
  password: 'admin'
});

// Headers para requisições autenticadas:
Authorization: Bearer <token>
💬 Contato
Desenvolvido com 💙 por:

👨‍💻 Lucas Biazin

👔 Tiago Biazin
