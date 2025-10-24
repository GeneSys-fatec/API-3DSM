import { ResponsiveContainer, BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from "recharts";

export default function GraficoProdutividade({ dados, isCompact }: any) {
  

  const { usuarios, dadosMensais } = dados;


  const colors = ["#60a5fa", "#34d399", "#fbbf24", "#f87171", "#a78bfa", "#fb923c", "#38bdf8", "#f472b6"];

  if (!usuarios || usuarios.length === 0) {
    return (
      <div className="bg-white p-4 rounded-2xl shadow-md h-[300px] flex items-center justify-center">
        <p className="text-gray-500">Sem dados de produtividade</p>
      </div>
    );
  }

  return (
    <div className="bg-white p-4 rounded-2xl shadow-md">
      <h2 className="text-lg font-semibold mb-3 text-gray-800">Produtividade (Últimos 6 Meses)</h2>

      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={dadosMensais}>
          <CartesianGrid strokeDasharray="3 3" />
          

          <XAxis dataKey="mes" />

          <YAxis domain={[0, 100]} />
          
          <Tooltip formatter={(value: any) => `${value}%`} />
          
          <Legend />

          {usuarios.map((usuario: string, index: number) => (
            <Bar
              key={usuario}
              dataKey={usuario} 
              name={usuario}
              fill={colors[index % colors.length]} 
            />
          ))}
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}