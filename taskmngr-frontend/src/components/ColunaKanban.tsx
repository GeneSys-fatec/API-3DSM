import React from "react";
import CardTarefa from "./CardTarefa";
import { SortableContext, verticalListSortingStrategy } from "@dnd-kit/sortable";
import { useDroppable } from "@dnd-kit/core";
import type { Tarefa } from "../pages/Home";

// 1. A interface de props foi atualizada para receber as funções de evento
interface ColunaKanbanProps {
  id: string; // O ID do banco de dados da coluna
  titulo: string;
  tarefas: Tarefa[];
  corClasse: string;
  corFundo: string;
  onAbrirModalCriacao: () => void;
  onAbrirModalEdicao: (tarefa: Tarefa) => void;
}

export default function ColunaKanban(props: ColunaKanbanProps) {
  // 2. Desestruturamos as novas props
  const { id, titulo, tarefas, corClasse, corFundo, onAbrirModalCriacao, onAbrirModalEdicao } = props;

  // O 'id' da prop (ID do banco) é usado para registrar a área "droppable"
  const { setNodeRef } = useDroppable({ id });

  // Pega os IDs das tarefas para o SortableContext
  const tarefasIds = tarefas.map((t) => t.tar_id);

  // Define a classe da borda baseada na cor da coluna
  const mapaDeCoresBorda: { [key: string]: string } = {
    "orange-400": "border-orange-400",
    "blue-400": "border-blue-400",
    "green-500": "border-green-500",
    "purple-400": "border-purple-400",
    "red-400": "border-red-400",
  };
  const classeBordaHeader = mapaDeCoresBorda[corClasse] || "border-gray-400";

  return (
    <div
      ref={setNodeRef}
      className="flex flex-col w-full lg:w-80 flex-shrink-0 bg-gray-100/80 rounded-lg max-h-[calc(100vh-120px)]"
    >
      {/* 3. Cabeçalho da Coluna agora tem o título e o botão de adicionar */}
      <div className={`p-3 border-t-4 ${classeBordaHeader} rounded-t-lg ${corFundo} flex justify-between items-center`}>
        <h2 className="text-lg font-bold tracking-wider text-gray-700">{titulo}</h2>
        <button
          onClick={onAbrirModalCriacao}
          className="text-gray-600 hover:text-black transition-colors"
          title="Adicionar nova tarefa a esta coluna"
        >
          <i className="fa-solid fa-plus text-xl"></i>
        </button>
      </div>

      {/* Corpo da Coluna com os cards */}
      <div className="p-2 flex-1 flex flex-col gap-3 overflow-y-auto">
        <SortableContext items={tarefasIds} strategy={verticalListSortingStrategy}>
          {/* 4. Mapeamento das tarefas, passando as props corretas para CardTarefa */}
          {tarefas.map((tarefa) => (
            <CardTarefa
              key={tarefa.tar_id}
              tarefa={tarefa}
              onAbrirModalEdicao={onAbrirModalEdicao}
            />
          ))}
        </SortableContext>
      </div>
    </div>
  );
}