import { useState, useEffect } from "react";
import { MdPersonAddAlt } from "react-icons/md";
import { MdProductionQuantityLimits } from "react-icons/md";
import { useNavigate, Outlet } from "react-router-dom";
import { BiSupport } from "react-icons/bi";
import logo from '../../assets/image/logo.png'
import {
  RxAvatar, RxDashboard, RxBox, RxPerson, RxExit
} from "react-icons/rx";
import { VscTools } from "react-icons/vsc";
import { BsCalculator } from "react-icons/bs";
import { LuListTodo } from "react-icons/lu";
import { TfiAgenda } from "react-icons/tfi";
import { Banknote } from 'lucide-react';
import { IoIosHome } from "react-icons/io";
import { BsGraphUpArrow } from "react-icons/bs";

import {
  PiCodesandboxLogoLight, PiSuitcaseSimpleLight
} from "react-icons/pi";
import { IoReceiptOutline } from "react-icons/io5";
import { FaMotorcycle, FaFileInvoiceDollar } from "react-icons/fa6";
import { useTheme } from "styled-components";
import { FaSun, FaMoon, FaBars } from "react-icons/fa";
import { MdOutlineAttachMoney } from "react-icons/md";

import {
  Layout, Sidebar, SidebarItem, HeaderContainer, Main, Avatar,
  UserProfile, UserName, HeaderRight, ToggleSidebarButton, SidebarOverlay, Logo, Baloon
} from "./styles";
import {
  useBuscarUsuarioPorIdQuery,
  useLogoutMutation
} from "../../services/api";
import KeyboardShortcutHandler from "../shortcuts/KeyboardShortcutHandler";
import Footer from "../Footer";
import Loader from "../Loader";

interface HeaderProps {
  toggleTheme: () => void;
  isLightTheme: boolean;
}

const Header = ({ toggleTheme, isLightTheme }: HeaderProps) => {
  const navigate = useNavigate();
  const theme = useTheme();

  const [showUserInfo, setShowUserInfo] = useState(false);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [cadastroAberto, setCadastroAberto] = useState(false);
  const [vendasAberto, setVendasAberto] = useState(false);
  const [relatorioAberto, setRelatorioAberto] = useState(false);
  const [toolsAberto, setToolsAberto] = useState(false);
  const [financialAberto, setfinancialAberto] = useState(false);


  const [logout] = useLogoutMutation();

  const userId = Number(localStorage.getItem("USER_ID"));

  const {
    data: usuario,
    isLoading,
    error,
  } = useBuscarUsuarioPorIdQuery(userId);

  useEffect(() => {
    const script1 = document.createElement('script');
    script1.src = "https://cdn.botpress.cloud/webchat/v3.0/inject.js";
    script1.defer = true;

    const script2 = document.createElement('script');
    script2.src = "https://files.bpcontent.cloud/2025/07/03/15/20250703150519-HU52UK6C.js";
    script2.defer = true;

    script1.onload = () => {
      document.body.appendChild(script2);
    };

    document.body.appendChild(script1);

    return () => {
      document.body.removeChild(script1);
      if (document.body.contains(script2)) {
        document.body.removeChild(script2);
      }
    };
  }, []);


  useEffect(() => {
    if (!userId || isNaN(userId) || userId <= 0) {
      navigate("/login");
    }
  }, [userId, navigate]);

  const handleLogout = async () => {
    try {
      await logout().unwrap();
    } catch (error) {
      console.error("Erro ao fazer logout:", error);
    } finally {
      localStorage.removeItem("ACCESS_TOKEN");
      localStorage.removeItem("USER_ID");
      navigate("/login");
    }
  };

  const toggleUserInfo = () => {
    setShowUserInfo((prev) => !prev);
  };

  const toggleSidebar = () => {
    setIsSidebarOpen((prev) => !prev);
  };

  const closeSidebar = () => {
    setIsSidebarOpen(false);
  };

  const toggleMenu = (menu: string) => {
    setCadastroAberto(menu === "cadastro" ? !cadastroAberto : false);
    setVendasAberto(menu === "vendas" ? !vendasAberto : false);
    setRelatorioAberto(menu === "relatorio" ? !relatorioAberto : false);
    setToolsAberto(menu === "tools" ? !toolsAberto : false);
    setfinancialAberto(menu === "financial" ? !financialAberto : false);
  };


  return (
    <>
      <Layout>
        <KeyboardShortcutHandler closeSidebar={closeSidebar} />
        {/* Botão de hambúrguer visível apenas em telas pequenas */}
        <ToggleSidebarButton onClick={toggleSidebar}>
          <FaBars />
        </ToggleSidebarButton>

        {/* Overlay quando sidebar está aberto */}
        {isSidebarOpen && <SidebarOverlay onClick={closeSidebar} />}
        {/* Sidebar */}
        <Sidebar isOpen={isSidebarOpen}>
          <SidebarItem href="/home">
            <IoIosHome /> Home
          </SidebarItem>
          {/* Cadastro com submenu */}
          <SidebarItem onClick={() => toggleMenu("cadastro")}>
            <RxPerson /> Cadastro
          </SidebarItem>

          {cadastroAberto && (
            <div style={{ marginLeft: "1.5rem", display: "flex", flexDirection: "column", gap: "0.5rem" }}>
              <SidebarItem href="/cadastro-clientes" onClick={closeSidebar}>
                <MdPersonAddAlt /> Clientes
              </SidebarItem>
              <SidebarItem href="/produtos-cadastrar" onClick={closeSidebar}>
                <MdProductionQuantityLimits /> Produtos
              </SidebarItem>
            </div>
          )}

          <SidebarItem onClick={() => toggleMenu("vendas")}>
            <RxBox /> Vendas
          </SidebarItem>

          {vendasAberto && (
            <div style={{ marginLeft: "1.5rem", display: "flex", flexDirection: "column", gap: "0.5rem" }}>
              <SidebarItem href="/pdv-mesa" onClick={closeSidebar}>
                <PiSuitcaseSimpleLight /> PDV
              </SidebarItem>
              <SidebarItem href="/ordem-servico" onClick={closeSidebar}>
                <PiSuitcaseSimpleLight /> Ordem de Serviço
              </SidebarItem>
              <SidebarItem href="/ordem-list" onClick={closeSidebar}>
                <PiSuitcaseSimpleLight /> Lista Ordem de Serviço
              </SidebarItem>
            </div>
          )}

          <SidebarItem href="/stock" onClick={closeSidebar}>
            <PiCodesandboxLogoLight /> Estoque
          </SidebarItem>

          <SidebarItem onClick={() => toggleMenu("relatorio")}>
            <IoReceiptOutline /> Relatório
          </SidebarItem>

          {relatorioAberto && (
            <div style={{ marginLeft: "1.5rem", display: "flex", flexDirection: "column", gap: "0.5rem" }}>
              <SidebarItem href="/sale-list" onClick={closeSidebar}>
                <PiSuitcaseSimpleLight /> Relatório de Vendas
              </SidebarItem>
              <SidebarItem href="/get" onClick={closeSidebar}>
                <PiSuitcaseSimpleLight /> Relação de Clientes
              </SidebarItem>
              <SidebarItem href="/taxenvironment" onClick={closeSidebar}>
                <PiSuitcaseSimpleLight /> Relatório fiscal
              </SidebarItem>
            </div>
          )}

          <SidebarItem href="/delivery" onClick={closeSidebar}>
            <FaMotorcycle /> Entregas
          </SidebarItem>

          <SidebarItem onClick={() => toggleMenu("tools")}>
            <VscTools /> Ferramentas
          </SidebarItem>
          {toolsAberto && (
            <div style={{ marginLeft: "1.5rem", display: "flex", flexDirection: "column", gap: "0.5rem" }}>
              <SidebarItem href="/calculator" onClick={closeSidebar}>
                <BsCalculator /> Calculadora
              </SidebarItem>
              <SidebarItem href="/todolist" onClick={closeSidebar}>
                <LuListTodo /> Lista de tarefas
              </SidebarItem>
              <SidebarItem href="/agenda" onClick={closeSidebar}>
                <TfiAgenda />Agenda
              </SidebarItem>
            </div>
          )}


          <SidebarItem onClick={() => toggleMenu("financial")}>
            <MdOutlineAttachMoney /> Financeiro
          </SidebarItem>
          {financialAberto && (
            <div style={{ marginLeft: "1.5rem", display: "flex", flexDirection: "column", gap: "0.5rem" }}>
              <SidebarItem href="/financial" onClick={closeSidebar}>
                <MdOutlineAttachMoney /> Painel Financeiro
              </SidebarItem>
              <SidebarItem href="/dashboard" onClick={closeSidebar}>
                <RxDashboard /> Dashboard
              </SidebarItem>
              <SidebarItem href="/accounts-payable" onClick={closeSidebar}>
                <Banknote /> Contas a pagar
              </SidebarItem>

              <SidebarItem href="/accounts-receivable" onClick={closeSidebar}>
                <Banknote /> Contas a receber
              </SidebarItem>

              <SidebarItem href="/recibo" onClick={closeSidebar}>
                <IoReceiptOutline /> Recibo
              </SidebarItem>

              <SidebarItem href="/nota-fiscal" onClick={closeSidebar}>
                <FaFileInvoiceDollar /> Nota fiscal
              </SidebarItem>

            </div>
          )}

          <SidebarItem href="/flat" onClick={closeSidebar}>
            <BsGraphUpArrow /> Plano
          </SidebarItem>

          <SidebarItem href="/support" onClick={closeSidebar}>
            <BiSupport /> Suporte
          </SidebarItem>

          <SidebarItem
            onClick={() => {
              closeSidebar();
              handleLogout();
            }}
            style={{ cursor: "pointer" }}
          >
            <RxExit /> Sair
          </SidebarItem>
        </Sidebar>

        {/* Cabeçalho */}
        <HeaderContainer>
          <Logo>
            <div style={{ position: 'relative', width: '100px', height: '100px' }}>
              <img src={logo} alt="logoBerion" style={{ width: '100%', height: '100%' }} />
              <div id="botpress-webchat" style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%' }} />
            </div>
          </Logo>
          <HeaderRight onClick={toggleUserInfo} style={{ cursor: "pointer", position: "relative" }}>
            <UserProfile>
              <UserName>
                {isLoading
                  ? <Loader />
                  : error
                    ? "Erro ao carregar usuário"
                    : usuario?.nome ?? "Usuário"}
              </UserName>
              <Avatar>
                <RxAvatar size={24} />
              </Avatar>
            </UserProfile>
            {/* Alternar tema */}
            <button
              onClick={(e) => {
                e.stopPropagation();
                toggleTheme();
              }}
              style={{
                marginLeft: "1rem",
                background: "transparent",
                border: "none",
                color: theme.colors.text,
                cursor: "pointer",
              }}
              aria-label="Alternar tema"
            >
              {isLightTheme ? <FaSun size={20} /> : <FaMoon size={20} />}
            </button>

            {/* Informações do usuário */}
            {showUserInfo && usuario && (
              <div
                style={{
                  position: "absolute",
                  top: "100%",
                  right: 0,
                  background: theme.colors.surface,
                  padding: "1rem",
                  boxShadow: `0px 4px 10px ${theme.colors.glassShadow}`,
                  borderRadius: "8px",
                  zIndex: 10,
                  minWidth: "250px",
                  color: theme.colors.text,
                }}
              >
                <p><strong>ID:</strong> {usuario.id}</p>
                <p><strong>Username:</strong> {usuario.username}</p>
                <p><strong>Nome:</strong> {usuario.nome}</p>
                <p><strong>Email:</strong> {usuario.email}</p>
                <p><strong>Perfil:</strong> {usuario.perfil}</p>
                <SidebarItem onClick={handleLogout} style={{ cursor: "pointer" }}>
                  <RxExit /> Sair
                </SidebarItem>
              </div>
            )}
          </HeaderRight>
        </HeaderContainer>
        <Main>
          <Outlet />
        </Main>
      </Layout>
      <Baloon><img src={logo} alt="logoBerion" /></Baloon>
      <Footer />
    </>
  );
};

export default Header;
