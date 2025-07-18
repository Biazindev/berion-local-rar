import styled from 'styled-components';


const breakpoints = {
  sm: '768px',
  md: '1024px',
};

export const Container = styled.div`
  display: grid;
  grid-template-columns: minmax(0, 450px) 1fr;
  gap: 18px;
  padding-left: 84px;

  @media (max-width: ${breakpoints.sm}) {
    grid-template-columns: 1fr;
  }
`;

export const Title = styled.h1`
  font-size: 1.25rem;
  font-weight: 600;
  color: ${({ theme }) => theme.colors.text};
  margin-bottom: ${({ theme }) => theme.spacing(2)};
`;

export const LeftPane = styled.div`
  flex: 1;
  background: ${({ theme }) => theme.colors.surface};
  padding: ${({ theme }) => theme.spacing(2)};
  border-radius: ${({ theme }) => theme.radii.md};
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing(2)};
  box-shadow: 0 2px 6px ${({ theme }) => theme.colors.neoShadowDark};
  font-size: 0.875rem;

  max-width: 430px;

  @media (max-width: ${breakpoints.md}) {
    max-width: 100%;
    padding: ${({ theme }) => theme.spacing(1)};
  }

  .container {
    display: flex;
    flex-wrap: wrap;
    align-items: center;

    span {
      margin: 0 8px;
      font-size: 20px;

      h3 {
        font-size: 20px;
        top: -2px;
        position: relative;
      }
    }
  }
`;

export const RightPane = styled.div`
  flex: 1;
  width: 100%;
  max-width: 800px;
  padding: 8px;
  border-radius: ${({ theme }) => theme.radii.lg};
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing(4)};

  @media (max-width: ${breakpoints.md}) {
    padding: ${({ theme }) => theme.spacing(1)};
    max-width: 100%;
  }
`;

export const TableSelector = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr;
  width: 400px;
  overflow-y: auto;
  gap: 16px;

  button {
    all: unset;
    cursor: pointer;
    padding: ${({ theme }) => theme.spacing(2)} ${({ theme }) => theme.spacing(3)};
    border-radius: ${({ theme }) => theme.radii.md};
    text-align: center;
    font-size: 0.85rem;
    font-weight: 600;
    background: ${({ theme }) => theme.colors.primaryAccent};
    color: white;
    box-shadow: ${({ theme }) => theme.colors.neoShadowDark};
    transition: all 0.25s ease;

    &:hover {
      transform: translateY(-2px);
      background: ${({ theme }) => theme.colors.secondary};
    }

    &:active {
      transform: scale(0.98);
    }

    &.ocupada {
      background: #e74c3c !important; /* vermelho vibrante */
      color: white;
      box-shadow: inset 0 0 0 2px rgba(0,0,0,0.1);
    }

    &.disponivel {
      background: ${({ theme }) => theme.colors.primaryAccent};
    }

    &.desabilitada {
      background-color: #ccc !important;
      color: ${({ theme }) => theme.colors.text};
      pointer-events: none;
      opacity: 0.6;
    }
  }
`;



export const ProductList = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 20px;
  width: 100%;
  margin: 0 auto;
  padding: 16px;
  overflow-y: auto;
  max-height: 900px;
  cursor: pointer;

  scrollbar-width: thin;
  scrollbar-color: #e5e7eb transparent;

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background-color: #cbd5e1;
    border-radius: 10px;
  }

  > div {
    background-color: #ffffff;
    border-radius: 16px;
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
    padding: 1rem;
    display: flex;
    flex-direction: column;
    align-items: center;
    transition: all 0.3s ease;
    position: relative;

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);
    }

    img {
      width: 100%;
      max-height: 120px;
      object-fit: contain;
      border-radius: 10px;
      margin-bottom: 0.75rem;
    }

    strong {
      font-size: 0.95rem;
      font-weight: 600;
      color: #111827;
      text-align: center;
      margin-bottom: 0.5rem;
      display: block;
    }

    span {
      font-size: 0.85rem;
      color: #6b7280;
      margin-bottom: auto;
    }

    .price {
      font-size: 1.05rem;
      font-weight: 700;
      color: #10b981;
      margin-top: 0.75rem;
    }

    button {
      position: absolute;
      bottom: 12px;
      right: 12px;
      background-color: #8b5cf6;
      color: white;
      border: none;
      border-radius: 50%;
      width: 36px;
      height: 36px;
      font-size: 1.2rem;
      font-weight: bold;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4px 10px rgba(139, 92, 246, 0.4);
      transition: background-color 0.3s ease, transform 0.2s ease;

      &:hover {
        background-color: #7c3aed;
        transform: scale(1.1);
      }

      &:active {
        transform: scale(0.95);
      }
    }
  }
`;




export const PdvButton = styled.button`
    background-color: #4F46E5;
    padding: 0.5rem 1rem;
    border-radius: 0.5rem;
    font-weight: 600;
    color: white;
    border: none;

    &:hover {
      background: ${({ theme }) => theme.colors.secondary};
    }

    &[style*="background-color: #ccc"] {
      background-color: #ccc !important;
      color: ${({ theme }) => theme.colors.text};

      @media (max-width: ${breakpoints.sm}) {
    width: 100%;
  }
`
export const Input = styled.input`
  background: ${({ theme }) => theme.colors.surface};
  border-radius: ${({ theme }) => theme.radii.sm};
  padding: .5rem;
  font-size: .7rem;
  width: 100%;
  border: 1px solid transparent;
  box-shadow:
    inset 2px 2px 5px ${({ theme }) => theme.colors.neoShadowDark},
    inset -2px -2px 5px ${({ theme }) => theme.colors.neoShadowLight};
  transition: all 0.25s ease-in-out;

  &:hover {
    border-color: ${({ theme }) => theme.colors.primaryAccent};
  }

  &:focus {
    outline: none;
    border-color: ${({ theme }) => theme.colors.primary};
    box-shadow:
      0 0 0 2px ${({ theme }) => theme.colors.primaryAccent},
      inset 2px 2px 5px ${({ theme }) => theme.colors.neoShadowDark},
      inset -2px -2px 5px ${({ theme }) => theme.colors.neoShadowLight};

      @media (max-width: ${breakpoints.sm}) {
    font-size: 0.9rem;
    padding: ${({ theme }) => theme.spacing(1)};
  }
  }`

export const Wrapper = styled.div`
  padding: 1rem;

  @media (max-width: ${breakpoints.sm}) {
    padding: 0.5rem;
  }
`;

export const SwitchContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-left: 62px;
  margin-bottom: 1rem;
  flex-wrap: wrap;
`;

export const ToggleSwitch = styled.label`
  position: relative;
  display: inline-block;
  width: 42px;
  height: 24px;

  input {
    opacity: 0;
    width: 0;
    height: 0;

    &:checked + span {
      background-color: #4caf50;
    }

    &:checked + span:before {
      transform: translateX(18px);
    }
  }
`;

export const Slider = styled.span`
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: 0.4s;
  border-radius: 24px;

  &:before {
    position: absolute;
    content: "";
    height: 18px;
    width: 18px;
    left: 3px;
    bottom: 3px;
    background-color: white;
    transition: 0.4s;
    border-radius: 50%;
  }
`;
export const Top = styled.div`
margin: 0 auto;
width: 100%;
display: flex;
justify-content: center;
align-items: center;
flex-wrap: wrap;

h1 {
    font-size: 1.5rem;
    text-align: center;
  }
`

export const Titulo = styled.h1`
  font-size: 2rem;
  margin-bottom: 1rem;

  @media (max-width: ${breakpoints.sm}) {
    font-size: 1.5rem;
  }
`;

export const FiltroContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2rem;
`;

export const InputBusca = styled.input`
  padding: 0.75rem 1rem;
  border-radius: 8px;
  border: 1px solid #ccc;
  font-size: 1rem;
`;

export const Filtros = styled.div`
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
`;

export const FiltroBotao = styled.button`
  padding: 0.5rem 1rem;
  border-radius: 8px;
  border: none;
  background-color: '#007bff';
  color: '#fff';
  cursor: pointer;

  &:hover {
    background-color: #007bff;
    color: white;
  }
`;

export const ListaCards = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
`;

export const Card = styled.div`
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
`;

export const HeaderCard = styled.div`
  display: flex;
  justify-content: center;
  max-width: 600px;
  align-items: center;
  font-weight: bold;
  margin-bottom: 0.5rem;
`;

export const Mesa = styled.div`
  font-size: 1rem;
  margin-bottom: 0.5rem;
  color: #555;
`;

export const Produto = styled.div`
  font-size: 0.95rem;
  color: #333;
  margin-left: 1rem;
`;

export const FinalizarBotao = styled.button`
  margin-top: 1rem;
  background-color: #28a745;
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;

  &:hover {
    background-color: #218838;
  }
`;

export const Legend = styled.p`
    margin: 0 auto;
    display: flex;
    justify-content: center;
    align-items: center;

    .container {
    background-color: #ff5252;
    }

    span {
    margin-left: 8px;

    
    
    div {
    margin: 0 auto;
    justify-content: center;
    align-items: center;
    width: 24px;
    height: 24px;
    border-radius: 50%;
    margin-right: 16px;
    background-color: #218c74;
  }

    }
`

export const ImgContainer = styled.div`
    width: 100%;
    height: 120px;
    margin-bottom: 16px;
    object-fit: cover;

    img {
    width: 100%;
    height: 100%;
    }
`

export const Description = styled.span`
  > div {
    width: 100%;
    min-height: 70px;
    background-color: #fef9c3;
    border-radius: 10px;
    box-shadow: 
      inset 0 1px 2px rgba(255, 255, 255, 0.6),
      0 1px 3px rgba(0, 0, 0, 0.05);
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0.5rem;
    margin: 0.75rem 0;
    transition: background 0.3s ease;
  }

  p {
    font-size: 0.85rem;
    font-weight: 500;
    color: #4b5563;
    text-align: center;
    line-height: 1.3;
  }
`;

export const Icon = styled.div`
  position: absolute;
  bottom: 8px;
  right: 8px;
  
  

  span {
    margin: 0 auto;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #5f27cd;
    color: #fff;
    width: 32px;
    height: 32px;
    border-radius: 8px;
    font-weight: bold;
    font-size: 18px;
    z-index: 1000;
  }
`

export const OrderList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  background-color: #f8f9fa;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);

  h4 {
    font-size: 1.4rem;
    color: #333;
    margin-bottom: 1rem;
  }

  ul {
    list-style: none;
    padding: 0;
    margin: 0;
    display: flex;
    flex-direction: column;
    gap: 0.75rem;
  }

  li {
    background-color: #ffffff;
    border: 1px solid #e0e0e0;
    border-left: 4px solid #4caf50;
    padding: 1rem;
    border-radius: 8px;
    font-size: 0.95rem;
    color: #333;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .produto-info {
      display: flex;
      gap: 0.25rem;
    }

    .remover {
      background: transparent;
      border: none;
      color: #e53935;
      font-size: 1.2rem;
      font-weight: bold;
      cursor: pointer;
      padding: 4px 8px;
      transition: 0.2s ease;

      &:hover {
        color: #b71c1c;
        transform: scale(1.2);
      }
    }
  }

  span {
  display: none;
  }

  @media (max-width: 600px) {
    padding: 1rem;

    h4 {
      font-size: 1.2rem;
    }

    li {
      font-size: 0.9rem;
      flex-direction: column;
      align-items: flex-start;
    }

    .remover {
      align-self: flex-end;
      margin-top: 0.5rem;
    }
  }
`;
export const TotaisContainer = styled.div`
  margin-top: 2rem;
  padding: 1.5rem;
  background-color: #ffffff;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  gap: 1rem;

  .linha {
    display: flex;
    justify-content: space-between;
    font-size: 1rem;
    color: #444;

    strong {
      color: #555;
    }

    span {
      font-weight: 500;
      color: #333;
    }
  }

  .total-com-desconto {
    background-color: #f1f8e9;
    padding: 0.75rem 1rem;
    border-left: 4px solid #66bb6a;
    border-radius: 8px;

    strong {
      color: #2e7d32;
    }

    span {
      color: #2e7d32;
      font-weight: bold;
    }
  }

  @media (max-width: 600px) {
    padding: 1rem;
    font-size: 0.95rem;

    .linha {
      flex-direction: column;
      gap: 0.25rem;
    }

    .total-com-desconto {
      flex-direction: column;
    }
  }
`;
export const ClienteInfoContainer = styled.div`
  background-color: #ffffff;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  margin-bottom: 2rem;

  display: flex;
  flex-direction: column;
  gap: 1.5rem;

  .info {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;

    p {
      margin: 0;
      font-size: 1rem;
      color: #333;

      strong {
        color: #555;
      }
    }
  }

  .pagamento {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: 1rem;

    label {
      display: block;
      margin-bottom: 0.5rem;
      font-weight: 600;
      color: #444;
    }

    input, .react-select__control {
      width: 100%;
    }
  }

  @media (max-width: 600px) {
    .pagamento {
      grid-template-columns: 1fr;
    }
  }
`;

export const NameProduct = styled.span`
    font-size: 12px;
`

export const ModalWrapper = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* fundo escuro translúcido */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
`;

export const ModalContainer = styled.div`
  background-color: #fff;
  padding: 2rem;
  border-radius: 1rem;
  width: 100%;
  max-width: 500px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
`;

export const TitleModal = styled.h3`
  margin-bottom: 1.5rem;
  color: #333;
  font-size: 1.4rem;
`;

export const Label = styled.label`
  display: flex;
  flex-direction: column;
  margin-bottom: 1rem;
  font-weight: 500;
  color: #444;

  input {
    margin-top: 0.4rem;
    padding: 0.5rem;
    border-radius: 6px;
    border: 1px solid #ccc;
    font-size: 1rem;
  }
`;

export const Button = styled.button`
  margin-top: 1rem;
  width: 100%;
  padding: 0.75rem;
  font-size: 1rem;
  background-color: #007aff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;

  &:hover {
    background-color: #005ecb;
  }
`;
