import React from "react";
import CardTarefa from "./CardTarefa";

interface Tarefa {
  id: number;
  titulo: string;
  descricao?: string;
  prioridade: "Alta" | "Média" | "Baixa";
  entrega: string;
  responsavel: string;
}

interface ColunaKanbanProps {
  titulo: string;
  tarefas: Tarefa[];
  corClasse: string;
  corFundo: string;
}

export default class ColunaKanban extends React.Component<ColunaKanbanProps> {
  render() {
    const { titulo, tarefas, corClasse, corFundo } = this.props;

    const mapaDeCoresBorda = {
      "orange-400": "border-orange-400",
      "blue-400": "border-blue-400",
      "green-500": "border-green-500",
      "gray-400": "border-gray-400",
    };

    const classeBordaHeader =
      mapaDeCoresBorda[corClasse as keyof typeof mapaDeCoresBorda] || "border-gray-400";

    return (
      <div className={`flex flex-col w-full lg:w-93 lg:flex-shrink-0 bg-gray-100 rounded-lg max-h-[80vh] lg:h-full`}>
        <div className={`p-3 border-t-4 ${classeBordaHeader} rounded-t-lg ${corFundo}`}>
          <h2 className="text-xl font-bold tracking-wider text-gray-700">
            {titulo}
          </h2>
        </div>
        <div className="p-4 flex flex-col gap-4 overflow-y-auto">
          {tarefas.map((t) => (
            <CardTarefa
              key={t.id}
              id={t.id}
              titulo={t.titulo}
              descricao={t.descricao}
              prioridade={t.prioridade}
              prazo={t.entrega}
              responsavel={{ nome: t.responsavel }}
              corClasse={corClasse}
            />
          ))}
          <button className="mt-2 text-left p-2 text-gray-500 hover:bg-gray-200 rounded-md transition-colors">
            + Adicionar tarefa
          </button>
        </div>
      </div>
    );
  }
}

