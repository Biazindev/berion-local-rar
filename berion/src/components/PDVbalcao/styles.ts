import styled from 'styled-components';

export const Container = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
  padding: 2rem;

  scrollbar-width: thin;
  scrollbar-color: transparent transparent;

  &::-webkit-scrollbar {
    width: 8px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background-color: transparent;
  }

  &::-webkit-scrollbar-thumb:hover {
    background-color: transparent;
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
  font-size: 0.875rem; // menor
`;

export const RightPane = styled.div`
  flex: 1;
  background: ${({ theme }) => theme.colors.surface};
  padding: ${({ theme }) => theme.spacing(4)};
  border-radius: ${({ theme }) => theme.radii.lg};
  box-shadow: 0 2px 8px ${({ theme }) => theme.colors.neoShadowDark};
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing(4)};

  scrollbar-width: thin;
  scrollbar-color: transparent transparent;

  &::-webkit-scrollbar {
    width: 8px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background-color: transparent;
  }

  &::-webkit-scrollbar-thumb:hover {
    background-color: transparent;
  }
`;

export const SwitchContainer = styled.div`
  display: flex;
  align-items: center;
  margin: 0.5rem 0;
`;

export const ToggleSwitch = styled.label`
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
`;

export const Slider = styled.span`
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: .4s;
  border-radius: 34px;

  &:before {
    position: absolute;
    content: "";
    height: 26px;
    width: 26px;
    left: 4px;
    bottom: 4px;
    background-color: white;
    transition: .4s;
    border-radius: 50%;
  }
`;

export const ToggleInput = styled.input`
  opacity: 0;
  width: 0;
  height: 0;

  &:checked + ${Slider} {
    background-color: #2196F3;
  }

  &:focus + ${Slider} {
    box-shadow: 0 0 1px #2196F3;
  }

  &:checked + ${Slider}:before {
    transform: translateX(26px);
  }
`;

export const TableSelector = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: ${({ theme }) => theme.spacing(1)};

  scrollbar-width: thin;
  scrollbar-color: transparent transparent;

  &::-webkit-scrollbar {
    width: 8px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background-color: transparent;
  }

  &::-webkit-scrollbar-thumb:hover {
    background-color: transparent;
  }
  
  button {
    background: ${({ theme }) => theme.colors.primaryAccent};
    color: #fff;
    padding: ${({ theme }) => theme.spacing(1)} ${({ theme }) => theme.spacing(2)};
    border: none;
    border-radius: ${({ theme }) => theme.radii.sm};
    font-size: 0.75rem; // menor
    min-width: 80px;

    &:hover {
      background: ${({ theme }) => theme.colors.secondary};
    }

    &[style*="background-color: #ccc"] {
      background-color: #ccc !important;
      color: ${({ theme }) => theme.colors.text};
    }
  }
`;


export const ProductList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  max-height: 320px;
  overflow-y: auto;
  padding: 1rem;
  background: #ffffff;
  border-radius: 1rem;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.05);

  scrollbar-width: thin;
  scrollbar-color: #cbd5e1 transparent;

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background-color: #cbd5e1;
    border-radius: 8px;
  }

  &::-webkit-scrollbar-thumb:hover {
    background-color: #94a3b8;
  }

  div {
    background: #f9fafb;
    border: 1px solid #e5e7eb;
    border-radius: 0.75rem;
    padding: 1rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 1rem;
    font-weight: 600;
    color: #1f2937;
    box-shadow: 0 3px 6px rgba(0, 0, 0, 0.04);
    transition: all 0.2s ease;

    &:hover {
      background-color: #f3f4f6;
      transform: translateY(-2px);
    }

    button {
      background: #10b981;
      color: white;
      border: none;
      border-radius: 0.5rem;
      padding: 0.5rem 1rem;
      font-size: 0.875rem;
      font-weight: 600;
      box-shadow: 0 4px 12px rgba(16, 185, 129, 0.4);
      transition: all 0.2s ease;
      cursor: pointer;

      &:hover {
        background-color: #047857;
        box-shadow: 0 6px 16px rgba(4, 120, 87, 0.5);
        transform: scale(1.05);
      }

      &:active {
        transform: scale(1);
      }
    }
  }
`;


export const PdvButton = styled.button`
    background: ${({ theme }) => theme.colors.primaryAccent};
    color: #fff;
    padding: ${({ theme }) => theme.spacing(1)} ${({ theme }) => theme.spacing(2)};
    border: none;
    border-radius: ${({ theme }) => theme.radii.sm};
    font-size: 1rem; // menor
    font-weight: bold;
    min-width: 80px;

    &:hover {
      background: ${({ theme }) => theme.colors.secondary};
    }

    &[style*="background-color: #ccc"] {
      background-color: #ccc !important;
      color: ${({ theme }) => theme.colors.text};
`
export const Input = styled.input`
  background: ${({ theme }) => theme.colors.surface};
  border-radius: ${({ theme }) => theme.radii.sm};
  padding: ${({ theme }) => theme.spacing(2)};
  font-size: 1rem;
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
  }`

export const Wrapper = styled.div`
  padding: 1rem;
`;
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
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
`;

export const TitleModal = styled.h3`
  margin-bottom: 1.5rem;
  color: #333;
  font-size: 1.4rem;
`;