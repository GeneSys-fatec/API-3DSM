import { useState, useEffect } from "react";
import CardTarefa from "@/components/features/tasks/CardTarefa";
import {
  SortableContext,
  verticalListSortingStrategy,
} from "@dnd-kit/sortable";
import { useDroppable } from "@dnd-kit/core";
import type { Tarefa } from "@/types/types";

interface ColunaKanbanProps {
  id: string;
  titulo: string;
  tarefas: Tarefa[];
  corClasse: string;
  corFundo: string;
  onAbrirModalCriacao: () => void;
  onAbrirModalEdicao: (tarefa: Tarefa) => void;
  onApagarColuna: () => void;
  onExcluirTarefa: (tarefa: Tarefa) => void;
  isEditing: boolean;
  onStartEditing: (id: string | null) => void;
  onFinishEditing: (id: string, newTitle: string) => void;
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
    onApagarColuna,
    onExcluirTarefa,
    isEditing,
    onStartEditing,
    onFinishEditing,
  } = props;

  const { setNodeRef } = useDroppable({ id });

  const tarefasIds = tarefas.map((t) => t.tar_id);

  const [tempTitle, setTempTitle] = useState(titulo);

  const mapaDeCoresBorda: { [key: string]: string } = {
    "orange-400": "border-orange-400",
    "blue-400": "border-blue-400",
    "green-500": "border-green-500",
    "purple-400": "border-purple-400",
    "red-400": "border-red-400",
  };
  const classeBordaHeader = mapaDeCoresBorda[corClasse] || "border-gray-400";

  useEffect(() => {
    setTempTitle(titulo);
  }, [titulo]);

  const handleFinishEditing = () => {
    if (tempTitle.trim() && tempTitle.trim() !== titulo) {
      onFinishEditing(id, tempTitle.trim());
    } else {
      setTempTitle(titulo);
      onStartEditing(null);
    }
  };

  return (
    <div
      ref={setNodeRef}
      className="flex flex-col w-full lg:w-96 lg:flex-shrink-0 bg-gray-100 rounded-lg max-h-[80vh] lg:h-full shadow-md"
    >
      <div
        className={`group p-3 border-t-4 ${classeBordaHeader} rounded-t-lg ${corFundo} flex items-center justify-between`}
      >
        {isEditing ? (
          <input
            type="text"
            value={tempTitle}
            onChange={(e) => setTempTitle(e.target.value)}
            onBlur={handleFinishEditing}
            onKeyDown={(e) => {
              if (e.key === "Enter") handleFinishEditing();
            }}
            className="text-lg font-bold tracking-wider text-gray-800 bg-transparent border-2 border-indigo-500 rounded-md w-full mr-2 outline-none"
            autoFocus
          />
        ) : (
          <h2
            className="text-lg font-bold tracking-wider text-gray-800 antialiased"
            onDoubleClick={() => onStartEditing(id)}
          >
            {titulo}
          </h2>
        )}

        <button
          className="
            flex items-center justify-center
            w-7 h-7
            text-gray-600/70 hover:text-gray-900
            hover:bg-black/10
            rounded-full
            opacity-100 lg:opacity-0 lg:group-hover:opacity-100 
            transition-all duration-200"
          aria-label="Apagar coluna"
          onClick={onApagarColuna}
        >
          <i className="fa-solid fa-xmark fa-lg"></i>
        </button>
      </div>

      <div className="p-4 flex flex-col gap-4 overflow-y-auto">
        <SortableContext
          items={tarefasIds}
          strategy={verticalListSortingStrategy}
        >
          {tarefas.map((tarefa) => (
            <CardTarefa
              key={tarefa.tar_id}
              tarefa={tarefa}
              onAbrirModalEdicao={onAbrirModalEdicao}
              corClasse={corClasse}
              onExcluir={onExcluirTarefa}
            />
          ))}
        </SortableContext>
        <button
          className="mt-2 text-left p-2 text-gray-500 hover:bg-gray-200 rounded-md transition-colors"
          onClick={onAbrirModalCriacao}
          title="Adicionar nova tarefa a esta coluna"
        >
          + Adicionar tarefa
        </button>
      </div>
    </div>
  );
}
