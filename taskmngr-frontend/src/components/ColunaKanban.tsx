import React from "react";
import CardTarefa from "./CardTarefa";
import {
  SortableContext,
  verticalListSortingStrategy,
} from "@dnd-kit/sortable";
import { useDroppable } from "@dnd-kit/core";
import type { Tarefa } from "../pages/Home";

interface ColunaKanbanProps {
  id: string;
  titulo: string;
  tarefas: Tarefa[];
  corClasse: string;
  corFundo: string;
  onAbrirModalCriacao: () => void;
  onAbrirModalEdicao: (tarefa: Tarefa) => void;
}

export default function ColunaKanban(props: ColunaKanbanProps) {
  const {
    id,
    titulo,
    tarefas,
    corClasse,
    corFundo,
    onAbrirModalCriacao,
    onAbrirModalEdicao,
  } = props;

  const { setNodeRef } = useDroppable({ id });

  const tarefasIds = tarefas.map((t) => t.tar_id);

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
      <div
        className={`p-3 border-t-4 ${classeBordaHeader} rounded-t-lg ${corFundo} flex justify-between items-center`}
      >
        <h2 className="text-lg font-bold tracking-wider text-gray-700">
          {titulo}
        </h2>
        <button
          onClick={onAbrirModalCriacao}
          className="text-gray-600 hover:text-black transition-colors"
          title="Adicionar nova tarefa a esta coluna"
        >
          <i className="fa-solid fa-plus text-xl"></i>
        </button>
      </div>

      <div className="p-2 flex-1 flex flex-col gap-3 overflow-y-auto">
        <SortableContext
          items={tarefasIds}
          strategy={verticalListSortingStrategy}
        >
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
