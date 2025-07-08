import { Routes, Route } from "react-router-dom"
import Credentials from "./components/Credentials"
import Header from "./components/Header"
import Cliente from "./components/Clientes"
import Stock from "./components/Stock"
import Dashboard from "./components/Dashboard"
import { ForgotPassword } from './components/Credentials/ForgotPassword'
import { ResetPassword } from './components/Credentials/ResetPassword'
import GetClientes from "./components/Get"
import { PrivateRoute } from '../src/PrivateRoute'
import Venda from "./components/Venda"
import User from "./components/User/User"
import SaleList from "./components/SaleList"
import Delivery from "./components/Delivery"
import VendaMesa from "./components/PDVmesa/index"
import VendaBalcao from "./components/PDVbalcao"
import ProdutosCadastrar from "./components/Produtos/ProdutosCadastrar"
import CadastroCliente from "./components/Cadastro/Clientes"
import OrdemServicoForm from "./components/OrderList"
import OrdemServicoList from "./components/OrderList/OrderServiceList"
import Suporte from "./components/Support"
import Info from "./components/Delivery/info"
import SaleNavigationMenu from "./menu"
import Calculator from "./components/tools/calculator"
import TodoList from "./components/tools/toDoList"
import Agenda from "./components/tools/agenda"
import Financial from "./components/financial"
import AccountsPayable from "./components/accountsPayable"
import AccountsReceivable from "./components/accountsReceivable"
import Flat from "./components/flat"
import Home from "./components/pages/home"
import NFComponent from "./components/NotaFiscal/NFComponents"
import NFSComponent from "./components/NotaFiscal/NFSComponent"
import Register from "./components/register"
import TaxEnvironment from "./components/TaxEnvironment"
import VendaDetalhes from "./components/Utils/VendaRow/vendaDetalhes"

interface AppRoutesProps {
  toggleTheme: () => void;
  isLightTheme: boolean;
}

const Rotas = ({ toggleTheme, isLightTheme }: AppRoutesProps) => (
  <Routes>
    <Route path="/" element={<Credentials />} />
    <Route path="/recuperar-senha" element={<ForgotPassword />} />
    <Route path="/resetar-senha" element={<ResetPassword />} />
    <Route path="/login" element={<Credentials />} />

    <Route element={<PrivateRoute />}>
      <Route element={<Header toggleTheme={toggleTheme} isLightTheme={isLightTheme} />}>
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/produtos-cadastrar" element={<ProdutosCadastrar />} />
        <Route path="/vendas-loja" element={<Cliente />} />
        <Route path="/cadastro-clientes" element={<CadastroCliente />} />
        <Route path="/stock" element={<Stock />} />
        <Route path="/get" element={<GetClientes />} />
        <Route path="/venda" element={<Venda vendaId={""} statusVenda={"pendente"} />} />
        <Route path="/user" element={<User />} />
        <Route path="/sale-list" element={<SaleList />} />
        <Route path="/delivery" element={<Delivery />} />
        <Route path="/nf" element={<NFComponent />} />
        <Route path="/nfs" element={<NFSComponent />} />
        <Route path="/pdv-mesa" element={<VendaMesa />} />
        <Route path="/pdv-balcao" element={<VendaBalcao />} />
        <Route path="/ordem-servico" element={<OrdemServicoForm />} />
        <Route path="/ordem-list" element={<OrdemServicoList />} />
        <Route path="/support" element={<Suporte />} />
        <Route path="delivery/entrega/:id" element={<Info />} />
        <Route path='/sale/navigation/menu' element={<SaleNavigationMenu />} />
        <Route path='/calculator' element={<Calculator />} />
        <Route path='/todolist' element={<TodoList />} />
        <Route path='/agenda' element={<Agenda />} />
        <Route path='/financial' element={<Financial />} />
        <Route path='/accounts-payable' element={<AccountsPayable />} />
        <Route path='/accounts-receivable' element={<AccountsReceivable />} />
        <Route path='/flat' element={<Flat />} />
        <Route path='/home' element={<Home />} />
        <Route path='/register' element={<Register />} />
        <Route path='/taxenvironment' element={<TaxEnvironment />} />
        <Route path="/venda/:id" element={<VendaDetalhes />} />
      </Route>
    </Route>
  </Routes>
)


export default Rotas