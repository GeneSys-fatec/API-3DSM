import React from "react";
import CardTarefa from "./CardTarefa";
import { SortableContext, verticalListSortingStrategy } from "@dnd-kit/sortable";
import { useDroppable } from "@dnd-kit/core";
import type { Tarefa } from "../pages/Home";

// A interface de props agora declara que espera receber um 'id'
interface ColunaKanbanProps {
  id: string;
  titulo: string;
  tarefas: Tarefa[];
  corClasse: string;
  corFundo: string;
}

export default function ColunaKanban(props: ColunaKanbanProps) {
  const { id, titulo, tarefas, corClasse, corFundo } = props;
  
  // O 'id' da prop é usado aqui para registrar a área "droppable"
  const { setNodeRef } = useDroppable({ id });

  const mapaDeCoresBorda = {
    "orange-400": "border-orange-400",
    "blue-400": "border-blue-400",
    "green-500": "border-green-500",
    "gray-400": "border-gray-400",
  };
  const classeBordaHeader =
    mapaDeCoresBorda[corClasse as keyof typeof mapaDeCoresBorda] || "border-gray-400";
    
  const tarefasIds = tarefas.map((t) => t.id);

  return (
    <div
      ref={setNodeRef}
      className={`flex flex-col w-full lg:w-93 lg:flex-shrink-0 bg-gray-100 rounded-lg max-h-[80vh] lg:h-full`}
    >
      <div className={`p-3 border-t-4 ${classeBordaHeader} rounded-t-lg ${corFundo}`}>
        <h2 className="text-xl font-bold tracking-wider text-gray-700">{titulo}</h2>
      </div>
      <div className="p-4 flex flex-col gap-4 overflow-y-auto overflow-x-hidden">
        <SortableContext items={tarefasIds} strategy={verticalListSortingStrategy}>
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
        </SortableContext>
        <button className="mt-2 text-left p-2 text-gray-500 hover:bg-gray-200 rounded-md transition-colors">
          + Adicionar tarefa
        </button>
      </div>
    </div>
  );
}

