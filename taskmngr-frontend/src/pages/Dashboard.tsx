import React, { useState } from "react";
import {
  BarChart,
  Bar,
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

interface DashboardProps {
  prazosData: { mes: string; dentroPrazo: number; foraPrazo: number }[];
  tarefasData: { nome: string; concluidas: number }[];
  produtividadeData: { usuario: string; produtividade: number }[];
}

export default function Dashboard({
  prazosData,
  tarefasData,
  produtividadeData,
}: DashboardProps) {

  // Dados de exemplo (mock)
  const mockPrazos = [
    { mes: 'Jan', dentroPrazo: 12, foraPrazo: 3 },
    { mes: 'Fev', dentroPrazo: 9, foraPrazo: 4 },
    { mes: 'Mar', dentroPrazo: 14, foraPrazo: 2 },
    { mes: 'Abr', dentroPrazo: 7, foraPrazo: 6 },
  ];

  const mockTarefas = [
    { nome: 'Projeto A', concluidas: 24 },
    { nome: 'Projeto B', concluidas: 18 },
    { nome: 'Projeto C', concluidas: 12 },
  ];

  const mockTarefasSemanal = [
    { nome: 'Projeto A', concluidas: 6 },
    { nome: 'Projeto B', concluidas: 4 },
    { nome: 'Projeto C', concluidas: 3 },
  ];


  const mockProdutividadeMensalGrouped = [
    { mes: 'Jan', Alice: 90, Bob: 20, Carol: 10 },
    { mes: 'Fev', Alice: 50, Bob: 75, Carol: 30 },
    { mes: 'Mar', Alice: 60, Bob: 40, Carol: 55 },
    { mes: 'Apr', Alice: 30, Bob: 20, Carol: 70 },
    { mes: 'May', Alice: 45, Bob: 50, Carol: 65 },
    { mes: 'Jun', Alice: 55, Bob: 35, Carol: 20 },
    { mes: 'Jul', Alice: 10, Bob: 5, Carol: 15 },
    { mes: 'Aug', Alice: 80, Bob: 60, Carol: 90 },
    { mes: 'Sep', Alice: 85, Bob: 90, Carol: 75 },
    { mes: 'Oct', Alice: 70, Bob: 80, Carol: 60 },
    { mes: 'Nov', Alice: 65, Bob: 55, Carol: 85 },
    { mes: 'Dec', Alice: 95, Bob: 70, Carol: 95 },
  ];

  const mockProdutividadeSemanalGrouped = [
    { mes: 'S1', Alice: 20, Bob: 10, Carol: 5 },
    { mes: 'S2', Alice: 40, Bob: 30, Carol: 15 },
    { mes: 'S3', Alice: 60, Bob: 45, Carol: 35 },
    { mes: 'S4', Alice: 30, Bob: 20, Carol: 25 },
  ];

  const finalPrazos = prazosData && prazosData.length ? prazosData : mockPrazos;
  const finalTarefas = tarefasData && tarefasData.length ? tarefasData : mockTarefas;

  const mockPrazosSemanal = [
    { mes: 'S1', dentroPrazo: 1, foraPrazo: 3 },
    { mes: 'S2', dentroPrazo: 3, foraPrazo: 2 },
    { mes: 'S3', dentroPrazo: 4, foraPrazo: 1 },
    { mes: 'S4', dentroPrazo: 2, foraPrazo: 2 },
  ];

  const [periodoPrazos, setPeriodoPrazos] = useState<'semanal' | 'mensal'>('semanal');
  const dadosPrazosExibidos = periodoPrazos === 'semanal' ? mockPrazosSemanal : finalPrazos;
  const [periodoTarefas, setPeriodoTarefas] = useState<'semanal' | 'mensal'>('semanal');
  const dadosTarefasExibidos = periodoTarefas === 'semanal' ? mockTarefasSemanal : finalTarefas;
  const [periodoProd, setPeriodoProd] = useState<'semanal' | 'mensal'>('semanal');
  const groupedFromProps = (produtividadeData as any[]) && produtividadeData.length && (produtividadeData as any)[0] && (produtividadeData as any)[0].mes ? (produtividadeData as any[]) : null;
  const dadosProdutividadeExibidos = groupedFromProps ? groupedFromProps : (periodoProd === 'semanal' ? mockProdutividadeSemanalGrouped : mockProdutividadeMensalGrouped);
  return (
    <div className="flex flex-col gap-6 p-6 w-full h-full bg-slate-50 overflow-y-auto">
      {/*Grid*/}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/*RN.5: As métricas de prazos devem ser exibidas em um gráfico de linha.*/}

        <div className="bg-white p-4 rounded-2xl shadow-md">
          <div className="flex items-start justify-between">
            <h2 className="text-lg font-semibold mb-3 text-gray-800">Cumprimento de Prazos</h2>
            <div className="inline-flex rounded-md shadow-sm bg-gray-100 p-1">
              <button
                onClick={() => setPeriodoPrazos('semanal')}
                className={`px-3 py-1 text-sm rounded ${periodoPrazos === 'semanal' ? 'bg-white font-semibold' : 'text-gray-600'}`}
              >
                Semanal
              </button>
              <button
                onClick={() => setPeriodoPrazos('mensal')}
                className={`px-3 py-1 text-sm rounded ${periodoPrazos === 'mensal' ? 'bg-white font-semibold' : 'text-gray-600'}`}
              >
                Mensal
              </button>
            </div>
            </div>
            <ResponsiveContainer width="100%" height={300}>
            <LineChart
              width={500}
              height={300}
              data={dadosPrazosExibidos}
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="mes" />
              <YAxis />
              <Tooltip />
              <Legend verticalAlign="bottom" align="center" wrapperStyle={{ overflowX: 'auto', whiteSpace: 'nowrap' }} />
              <Line type="monotone" dataKey="dentroPrazo" stroke="#8884d8" name="Dentro do prazo"/>
              <Line type="monotone" dataKey="foraPrazo" stroke="#82ca9d" name="Fora do prazo" />
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/*RN.4: As métricas de tarefas concluídas devem ser exibidas em um gráfico de barras horizontais.*/}
        <div className="bg-white p-4 rounded-2xl shadow-md">
          <div className="flex items-start justify-between">
            <h2 className="text-lg font-semibold mb-3 text-gray-800">Tarefas Concluídas</h2>
            <div className="inline-flex rounded-md shadow-sm bg-gray-100 p-1">
              <button
                onClick={() => setPeriodoTarefas('semanal')}
                className={`px-3 py-1 text-sm rounded ${periodoTarefas === 'semanal' ? 'bg-white font-semibold' : 'text-gray-600'}`}
              >
                Semanal
              </button>
              <button
                onClick={() => setPeriodoTarefas('mensal')}
                className={`px-3 py-1 text-sm rounded ${periodoTarefas === 'mensal' ? 'bg-white font-semibold' : 'text-gray-600'}`}
              >
                Mensal
              </button>
            </div>
          </div>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart
              data={dadosTarefasExibidos}
              layout="vertical"
            >
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis type="number" />
              <YAxis dataKey="nome" type="category" />
              <Tooltip />
              <Legend verticalAlign="bottom" align="center" wrapperStyle={{ overflowX: 'auto', whiteSpace: 'nowrap' }} />
              <Bar dataKey="concluidas" fill="#34d399" name="Concluídas" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/*RN.6: As métricas de produtividade devem ser exibidas em um gráfico de barras verticais.*/}
      <div className="bg-white p-4 rounded-2xl shadow-md">
        <div className="flex items-start justify-between">
          <h2 className="text-lg font-semibold mb-3 text-gray-800">Produtividade da Equipe</h2>
          <div className="inline-flex rounded-md shadow-sm bg-gray-100 p-1">
            <button
              onClick={() => setPeriodoProd('semanal')}
              className={`px-3 py-1 text-sm rounded ${periodoProd === 'semanal' ? 'bg-white font-semibold' : 'text-gray-600'}`}
            >
              Semanal
            </button>
            <button
              onClick={() => setPeriodoProd('mensal')}
              className={`px-3 py-1 text-sm rounded ${periodoProd === 'mensal' ? 'bg-white font-semibold' : 'text-gray-600'}`}
            >
              Mensal
            </button>
          </div>
        </div>
        <ResponsiveContainer width="100%" height={300}>
          <BarChart
            width={500}
            height={300}
            data={dadosProdutividadeExibidos}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="mes" />
            <YAxis domain={[0, 100]} />
            <Tooltip formatter={(value: any) => `${value}%`} />
            <Legend verticalAlign="bottom" align="center" wrapperStyle={{ display: 'flex', justifyContent: 'center', width: '100%', overflowX: 'auto', whiteSpace: 'nowrap' }} />
            {
              // Ta criando uma barra dinamica pra cada usuario
              (dadosProdutividadeExibidos[0] ? Object.keys(dadosProdutividadeExibidos[0]).filter(k => k !== 'mes') : [])
                .map((userKey, idx) => {
                  const colors = ['#8884d8', '#82ca9d', '#ffc658', '#ff7f7f', '#7dd3fc'];
                  return <Bar key={userKey} dataKey={userKey} name={userKey} fill={colors[idx % colors.length]} />;
                })
            }
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
}