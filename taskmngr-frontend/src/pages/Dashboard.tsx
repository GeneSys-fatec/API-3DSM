import { useState, useEffect } from "react";
import { useOutletContext } from "react-router-dom";
import { authFetch } from "../utils/api";
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
  Cell,
} from "recharts";

export default function Dashboard() {
  const { selectedProjectId } = useOutletContext<{ selectedProjectId: string | null }>();

  const [prazosData, setPrazosData] = useState<any[]>([]);
  const [tarefasData, setTarefasData] = useState<any[]>([]);
  const [produtividadeData, setProdutividadeData] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  const [periodoPrazos, setPeriodoPrazos] = useState<'semanal' | 'mensal'>('semanal');
  const [periodoTarefas, setPeriodoTarefas] = useState<'semanal' | 'mensal'>('semanal');
  const [periodoProdutividade, setPeriodoProdutividade] = useState<'semanal' | 'mensal'>('semanal');
  const [isCompact, setIsCompact] = useState(false);

  const dadosPrazosExibidos = prazosData.map((item) => ({
    mes: item.mes || item.nomeMes || "Mês",
    dentroPrazo: item.dentroPrazo || 0,
    foraPrazo: item.foraPrazo || 0,
  }));

  const dadosTarefasExibidos = tarefasData.map((item) => {
    const nomeOriginal = item.usuNome || item.nome || "Membro";
    const nomeCurto = nomeOriginal.length > 7 ? nomeOriginal.slice(0, 7) + "..." : nomeOriginal;
    return {
      nome: nomeCurto,
      concluidas: item.tarefasConcluidas || item.concluidas || 0,
    };
  });


  const dadosProdutividadeExibidos = produtividadeData.map((item) => ({
    nome: item.usuNome || "Membro",
    produtividade: item.produtividade || 0,
  }));

  const CompactLegend = ({ payload }: any) => {
    if (!payload || !Array.isArray(payload)) return null;
    const fontSize = isCompact ? 11 : 13;
    return (
      <div style={{ width: '100%', display: 'flex', justifyContent: 'center', marginTop: 8 }}>
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', justifyContent: 'center' }}>
          {payload.map((entry: any) => (
            <div key={entry.value} style={{ display: 'flex', alignItems: 'center', gap: 6, color: '#374151', fontSize }}>
              <span style={{ width: 12, height: 12, background: entry.color, display: 'inline-block', borderRadius: 2 }} />
              <span style={{ whiteSpace: 'nowrap' }}>{entry.value}</span>
            </div>
          ))}
        </div>
      </div>
    );
  };

  useEffect(() => {
    if (!selectedProjectId) return;

    const fetchData = async () => {
      try {
        setLoading(true);
        const prazosRes = await authFetch(`http://localhost:8080/tarefa/prazos-geral/${selectedProjectId}`);
        const tarefasRes = await authFetch(`http://localhost:8080/tarefa/tarefas-concluidas/${selectedProjectId}`);
        const produtividadeRes = await authFetch(`http://localhost:8080/tarefa/produtividade/${selectedProjectId}`);

        if (!prazosRes.ok || !tarefasRes.ok || !produtividadeRes.ok) {
          throw new Error(`Erro HTTP: ${prazosRes.status} / ${tarefasRes.status} / ${produtividadeRes.status}`);
        }

        const prazosJson = await prazosRes.json();
        const tarefasJson = await tarefasRes.json();
        const produtividadeJson = await produtividadeRes.json();

        // Garante que o gráfico receba array
        const prazosFormatado = Array.isArray(prazosJson)
          ? prazosJson
          : [{ mes: "Total", ...prazosJson }];




        setPrazosData(prazosFormatado);
        setTarefasData(tarefasJson);
        setProdutividadeData(produtividadeJson);
      } catch (error) {
        console.error("Erro ao buscar dados do dashboard:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [selectedProjectId]);


  useEffect(() => {
    let tid: any = null;
    const onResize = () => {
      if (tid) clearTimeout(tid);
      tid = setTimeout(() => setIsCompact(window.innerWidth < 640), 100);
    };
    onResize();
    window.addEventListener('resize', onResize);
    return () => { window.removeEventListener('resize', onResize); if (tid) clearTimeout(tid); };
  }, []);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-full">
        <p className="text-gray-500">Carregando dados do dashboard...</p>
      </div>
    );
  }

  return (
    <div className="flex flex-col gap-6 p-6 pb-20 md:pb-16 lg:pb-8 w-full h-full bg-slate-50 overflow-y-auto overflow-x-hidden">
      {/*Grid*/}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/*RN.5: As métricas de prazos devem ser exibidas em um gráfico de linha.*/}

        <div className="bg-white p-4 rounded-2xl shadow-md">
          <div className="flex items-start justify-between flex-wrap gap-2">
            <h2 className="text-lg font-semibold mb-3 text-gray-800 min-w-0 text-center sm:text-left">Cumprimento de Prazos</h2>
            <div className="inline-flex rounded-md shadow-sm bg-gray-100 p-1 flex-shrink-0">
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
              <Legend content={<CompactLegend />} />
              <Line type="monotone" dataKey="dentroPrazo" stroke="#8884d8" name="Dentro do prazo" />
              <Line type="monotone" dataKey="foraPrazo" stroke="#82ca9d" name="Fora do prazo" />
            </LineChart>
          </ResponsiveContainer>
        </div>

        {/*RN.4: As métricas de tarefas concluídas devem ser exibidas em um gráfico de barras horizontais.*/}
        <div className="bg-white p-4 rounded-2xl shadow-md">
          <div className="flex items-start justify-between flex-wrap gap-2">
            <h2 className="text-lg font-semibold mb-3 text-gray-800 min-w-0 text-center sm:text-left">Tarefas Concluídas</h2>
            <div className="inline-flex rounded-md shadow-sm bg-gray-100 p-1 flex-shrink-0">
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
              <YAxis dataKey="nome" type="category" width={120} />
              <Tooltip />
              <Legend content={<CompactLegend />} />
              <Bar dataKey="concluidas" fill="#34d399" name="Concluídas" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/*RN.6: As métricas de produtividade devem ser exibidas em um gráfico de barras verticais.*/}
      <div className="bg-white p-4 rounded-2xl shadow-md">
        <div className="flex items-start justify-between flex-wrap gap-2">
          <h2 className="text-lg font-semibold mb-3 text-gray-800 min-w-0 text-center sm:text-left">Produtividade da Equipe</h2>
          <div className="inline-flex rounded-md shadow-sm bg-gray-100 p-1 flex-shrink-0">
            <button
              onClick={() => setPeriodoProdutividade('semanal')}
              className={`px-3 py-1 text-sm rounded ${periodoProdutividade === 'semanal' ? 'bg-white font-semibold' : 'text-gray-600'}`}
            >
              Semanal
            </button>
            <button
              onClick={() => setPeriodoProdutividade('mensal')}
              className={`px-3 py-1 text-sm rounded ${periodoProdutividade === 'mensal' ? 'bg-white font-semibold' : 'text-gray-600'}`}
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
            <XAxis dataKey="nome" />
            <YAxis domain={[0, 100]} />
            <Tooltip formatter={(value: any) => `${value}%`} />
            <Legend content={<CompactLegend />} />
            <Bar dataKey="produtividade" name="Produtividade (%)">
              {dadosProdutividadeExibidos.map((_, index) => {
                const colors = [
                  "#60a5fa",
                  "#34d399",
                  "#fbbf24",
                  "#f87171",
                  "#a78bfa",
                  "#fb923c",
                  "#38bdf8",
                  "#f472b6",
                ];
                return (
                  <Cell key={`cell-${index}`} fill={colors[index % colors.length]} />
                );
              })}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      </div>
      {/* spacer para garantir que o conteúdo não seja cortado pela bottom navbar fixa */}
      <div className="h-20 md:h-12 lg:h-4" />
    </div>
  );
}