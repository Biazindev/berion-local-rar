import { Briefcase, UserPlus } from "lucide-react";
import { CodesandboxLogo } from "phosphor-react";
import { PiMoneyBold } from "react-icons/pi";
import { format, isToday } from 'date-fns';
import {
  ComposedChart,
  Bar,
  Line,
  Area,
  Tooltip,
  XAxis,
  YAxis,
  CartesianGrid,
  ResponsiveContainer,
  Legend,
  Cell,
} from "recharts";
import { Card, CardContent } from "../ui/card";
import {
  useGetTotalAnoAtualQuery,
  useGetTotalSemanaQuery,
  useGetTotalMesQuery,
  useGetTotalDiaSingQuery,
  useGetTotalSemanasQuery,
  useGetTotalMesesQuery,
  useGetTotalDiaSingleQuery,
  useGetTotalAnoQuery,
  useGetVendasQuery
} from "../../services/api";
import {
  Container,
  DashboardContainer,
  ContainerDash,
  CardContainer,
} from "./styles";
import Loader from '../Loader/index';

const COLORS = [
  "#EF4444", // Vermelho forte
  "#3B82F6", // Azul vivo
  "#F59E0B", // Amarelo brilhante
  "#10B981", // Verde vibrante
  "#8B5CF6", // Roxo intenso
  "#EC4899", // Rosa forte
  "#F97316", // Laranja vibrante
];

const formatCurrency = (value: number) =>
  value.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });

type ChartData = {
  name: string;
  vendas: number;
};

type DataItem = {
  label: string;
  totais?: { [key: string]: number };
};

const Dashboard = () => {
  const { data: dailySingData } = useGetTotalDiaSingQuery();
  const { data: dailySingleData, isLoading, error } = useGetTotalDiaSingleQuery();
  const { data: weeklySingData } = useGetTotalSemanasQuery();
  const { data: weeklyData } = useGetTotalSemanaQuery();
  const { data: monthlySingData } = useGetTotalMesesQuery();
  const { data: monthlyData } = useGetTotalMesQuery();
  const { data: yearData } = useGetTotalAnoQuery();
  const {data: yearNewData } = useGetTotalAnoAtualQuery();
  const { data: vendas } = useGetVendasQuery();
  
  const hoje = new Date();
  const inicioDia = new Date(hoje.getFullYear(), hoje.getMonth(), hoje.getDate());
  const fimDia = new Date(hoje.getFullYear(), hoje.getMonth(), hoje.getDate() + 1);

  const vendasHoje = vendas?.filter(venda => {
    const dataVenda = new Date(venda.dataVenda);
    return dataVenda >= inicioDia && dataVenda < fimDia;
  });

  const formatChartDataFromObject = (
    dataObj: Record<string, number> | number | undefined,
    fallbackLabel: string
  ): ChartData[] => {
    if (typeof dataObj === "number") {
      return [{ name: fallbackLabel, vendas: dataObj }];
    }

    if (!dataObj || typeof dataObj !== "object" || Array.isArray(dataObj)) {
      return [{ name: fallbackLabel, vendas: 0 }];
    }

    return Object.entries(dataObj).map(([label, valor]) => ({
      name: label,
      vendas: typeof valor === 'number' ? valor : 0,
    }));
  };

  const formatArrayData = (data: DataItem[] | undefined): ChartData[] => {
    if (!data || !Array.isArray(data)) return [{ name: "Nenhum dado", vendas: 0 }];

    return data.map((item) => {
      const totais = item.totais ?? {};
      const valores = Object.values(totais).filter(v => typeof v === 'number') as number[];
      const vendas = valores.reduce((sum, val) => sum + val, 0);
      return {
        name: item.label || "Sem label",
        vendas,
      };
    });
  };

  if (isLoading) return <Loader />;

  if (error) {
    console.error('Dashboard error:', error);
    return <p>Erro ao carregar dados. Tente recarregar a página.</p>;
  }

  // Valores com fallback seguro
  const valorPix = dailySingleData?.[0]?.totais?.PIX ?? 0;
  const vendasSemanas = typeof weeklyData === "number" ? weeklyData : 0;
  const vendasMes = typeof monthlyData === "number" ? monthlyData : 0;
  const vendasAno = typeof yearData === "number" ? yearData : 0;

  return (
    <ContainerDash>
      <DashboardContainer>
        <CardContainer>
          <p>{vendasHoje?.length ?? 0}</p>
          <div>
            <span>
              <Briefcase size={40} />
              <p>Novas Vendas</p>
            </span>
          </div>
        </CardContainer>
        <CardContainer>
          <p>1890</p>
          <div>
            <span>
              <CodesandboxLogo size={40} />
              <p>Produtos em estoque</p>
            </span>
          </div>
        </CardContainer>
        <CardContainer>
          <p>38</p>
          <div>
            <span>
              <UserPlus size={40} />
              <p>Novos Clientes</p>
            </span>
          </div>
        </CardContainer>
        <CardContainer>
          <p>
            {formatCurrency(typeof dailySingleData === 'number' ? dailySingleData : 0)}
          </p>
          <div>
            <span>
              <PiMoneyBold size={40} />
              <p>Total hoje</p>
            </span>
          </div>
        </CardContainer>
      </DashboardContainer>

      <Container>
        {/* Vendas Diárias */}
        <Card>
          <CardContent>
            <h4 style={{width: '100%', 
              backgroundColor: '#fff', 
              padding: '24px', borderRadius: '8px',
              boxShadow: '0 4px 24px rgba(0, 0, 0, 0.05)',
              transition: 'transform 0.3s ease, box-shadow 0.3s ease'
              }}>
              Valor total de vendas diárias {" "}
              <span style={{color: '#6c5ce7'}}>
                {formatCurrency(typeof dailySingleData === 'number' ? dailySingleData : 0)}
              </span>
            </h4>
            <ResponsiveContainer width="100%" height={300}>
              <ComposedChart data={formatChartDataFromObject(dailySingData, "Hoje")}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip formatter={(value: number) => formatCurrency(value)} />
                <Legend /><Legend verticalAlign="bottom" content={() => null} />
                <Area dataKey="vendas" fill={COLORS[2]} stroke={COLORS[2]} />
                <Bar dataKey="vendas">
                  {formatChartDataFromObject(dailySingData, "Hoje").map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Bar>
                <Line type="monotone" dataKey="vendas" stroke={COLORS[1]} />
              </ComposedChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Vendas Semanais */}
        <Card>
          <CardContent>
            <h4 style={{width: '100%', 
              backgroundColor: '#fff', 
              padding: '24px', borderRadius: '8px',
              boxShadow: '0 4px 24px rgba(0, 0, 0, 0.05)',
              transition: 'transform 0.3s ease, box-shadow 0.3s ease'
              }}>
              Valor total de vendas semanais {" "}
              <span style={{color: '#6c5ce7'}}>{formatCurrency(vendasSemanas)}</span>
            </h4>
            <ResponsiveContainer width="100%" height={300}>
              <ComposedChart data={formatChartDataFromObject(weeklySingData, "Semana")}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip formatter={(value: number) => formatCurrency(value)} />
                <Legend />
                <Area dataKey="vendas" fill={COLORS[5]} stroke={COLORS[5]} />
                <Bar dataKey="vendas">
                  {formatChartDataFromObject(weeklySingData, "Semana").map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Bar>
                <Line type="monotone" dataKey="vendas" stroke={COLORS[4]} />
              </ComposedChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>

        {/* Vendas Mensais */}
        <Card>
          <CardContent>
            <h4 style={{width: '100%', 
              backgroundColor: '#fff', 
              padding: '24px', borderRadius: '8px',
              boxShadow: '0 4px 24px rgba(0, 0, 0, 0.05)',
              transition: 'transform 0.3s ease, box-shadow 0.3s ease'
              }}>
              Valor total de vendas mensais {" "}
              <span style={{color: '#6c5ce7'}}>{formatCurrency(vendasMes)}</span>
            </h4>
            <ResponsiveContainer width="100%" height={300}>
              <ComposedChart data={formatChartDataFromObject(monthlySingData, "Mês")}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip formatter={(value: number) => formatCurrency(value)} />
                <Legend />
                <Area dataKey="vendas" fill={COLORS[6]} stroke={COLORS[6]} />
                <Bar dataKey="vendas">
                  {formatChartDataFromObject(monthlySingData, "Mês").map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Bar>
                <Line type="monotone" dataKey="vendas" stroke={COLORS[1]} />
              </ComposedChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
        {/* Vendas Anuais */}
        <Card>
          <CardContent>
            <h4 style={{width: '100%', 
              backgroundColor: '#fff', 
              padding: '24px', borderRadius: '8px',
              boxShadow: '0 4px 24px rgba(0, 0, 0, 0.05)',
              transition: 'transform 0.3s ease, box-shadow 0.3s ease'
              }}>
              Valor total de vendas anuais {" "}
              <span style={{color: '#6c5ce7'}}>
              {formatCurrency(typeof yearNewData === 'number' ? yearNewData : 0)}
              </span>
            </h4>
            <ResponsiveContainer width="100%" height={300}>
              <ComposedChart data={formatArrayData(yearData)}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip formatter={(value: number) => formatCurrency(value)} />
                <Legend />
                <Area dataKey="vendas" fill={COLORS[6]} stroke={COLORS[6]} />
                <Bar dataKey="vendas">
                  {formatArrayData(yearData).map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Bar>
                <Line type="monotone" dataKey="vendas" stroke={COLORS[1]} />
              </ComposedChart>
            </ResponsiveContainer>
          </CardContent>
        </Card>
      </Container>
    </ContainerDash>
  );
};

export default Dashboard;