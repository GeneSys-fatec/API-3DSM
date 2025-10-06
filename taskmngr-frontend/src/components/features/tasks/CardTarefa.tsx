import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import useMediaQuery from "@/hooks/MediaQuerie";
import type { Tarefa } from "@/types/types";
import { Trash } from "lucide-react";

interface CardTarefaProps {
  tarefa: Tarefa;
  onAbrirModalEdicao?: (tarefa: Tarefa) => void;
  isOverlay?: boolean;
  corClasse?: string;
  onExcluir?: (tarefa: Tarefa) => void;
}

export default function CardTarefa(props: CardTarefaProps) {
  const { tarefa, onAbrirModalEdicao, isOverlay, corClasse, onExcluir } = props;

  const isDesktop = useMediaQuery("(min-width: 1024px)");

  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging,
  } = useSortable({
    id: tarefa.tar_id,
    data: { type: "task", tarefa },
    disabled: !isDesktop,
  });

  const conditionalListeners = isDesktop ? listeners : undefined;

  const style = {
    transition,
    transform: isOverlay ? undefined : CSS.Transform.toString(transform),
    opacity: isDragging && !isOverlay ? 0 : 1,
  };

  const prioridadeEstilo: { [key: string]: string } = {
    Alta: "bg-red-100 text-red-700",
    Média: "bg-yellow-100 text-yellow-700",
    Baixa: "bg-green-100 text-green-700",
  };

  const classePrioridade = tarefa.tar_prioridade
    ? prioridadeEstilo[tarefa.tar_prioridade]
    : "bg-gray-100 text-gray-800";

  const classesDinamicas = isOverlay
    ? `shadow-2xl scale-105 rotate-1`
    : `hover:scale-105 ${
        corClasse
          ? `border-l-4 border-${corClasse} hover:border-${corClasse}`
          : ""
      }`;

  const cursorClasses = isDesktop
    ? "hover:cursor-grab active-cursor-grabbing"
    : "cursor-pointer";

  const inicialResponsavel = tarefa.usu_nome?.charAt(0)?.toUpperCase() || "?";

  return (
    <div
      ref={setNodeRef}
      style={style}
      {...attributes}
      {...listeners}
      {...conditionalListeners}
      onClick={() => onAbrirModalEdicao && onAbrirModalEdicao(tarefa)}
      className={`bg-white p-3 rounded-lg shadow-md group 
        transition-all duration-200 ease-in-out 
        ${isDesktop ? "touch-none" : ""} 
        ${classesDinamicas} 
        ${cursorClasses}`}
    >
      <div className="flex justify-between h-full">
        <div className="flex flex-col justify-between truncate">
          <h2 className="font-semibold text-gray-800 pr-2 truncate">
            {tarefa.tar_titulo}
          </h2>
          <div className="flex items-center gap-3 mt-2">
            <span
              className={`text-xs font-semibold px-2.5 py-0.5 rounded-full ${classePrioridade}`}
            >
              {tarefa.tar_prioridade}
            </span>
            <span className="text-sm text-gray-500 ">{tarefa.tar_prazo}</span>
          </div>
        </div>
        <div className="flex flex-col justify-between items-center gap-2">
          <button
            className="text-gray-400 hover:text-red-600 opacity-0 group-hover:opacity-100 transition-opacity"
            title="Excluir tarefa"
            onClick={(e) => {
              e.stopPropagation();

              if (onExcluir) {
                onExcluir(tarefa);
              }
            }}
          >
            <Trash className="h-4" />
          </button>
          <div
            title={tarefa.usu_nome}
            className="w-6 h-6 rounded-full bg-indigo-500 text-white flex items-center justify-center text-sm font-bold"
          >
            {inicialResponsavel}
          </div>
        </div>
      </div>
    </div>
  );
}
