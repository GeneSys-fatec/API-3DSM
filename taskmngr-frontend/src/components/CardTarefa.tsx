import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import useMediaQuery from "./hooks/MediaQuerie";

interface CardTarefaProps {
  id: number;
  titulo: string;
  descricao?: string;
  prioridade: string;
  prazo: string;
  responsavel: {
    nome: string;
    fotoURL?: string;
  };
  corClasse: string;
  isOverlay?: boolean;
}

export default function CardTarefa(props: CardTarefaProps) {
  const { id, titulo, prioridade, prazo, responsavel, corClasse, isOverlay } = props;

  
  const isDesktop = useMediaQuery('(min-width: 1024px)');

  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging,
  } = useSortable({ 
    id: id,
    data: { type: 'task' },
    disabled: !isDesktop, 
  });

  const conditionalListeners = isDesktop ? listeners : undefined;

  const style = {
    transition,
    transform: isOverlay ? undefined : CSS.Transform.toString(transform),
    opacity: isDragging && !isOverlay ? 0 : 1,
  };

  const prioridadeEstilo = {
    Alta: "bg-red-100 text-red-700",
    Média: "bg-yellow-100 text-yellow-700",
    Baixa: "bg-green-100 text-green-700",
  }[prioridade];

  const mapaDeCoresBorda = {
    "orange-400": "border-orange-400",
    "blue-400": "border-blue-400",
    "green-500": "border-green-500",
    "gray-400": "border-gray-400",
  };
  const classeBorda = mapaDeCoresBorda[corClasse as keyof typeof mapaDeCoresBorda] || "border-gray-400";
  
  const overlayClasses = isOverlay ? `shadow-2xl scale-105 border-l-4 ${classeBorda} rotate-1` : "hover:scale-105";

  
  const cursorClasses = isDesktop ? "hover:cursor-grab active:cursor-grabbing" : "";

  return (
    <div
      ref={setNodeRef}
      style={style}
      {...attributes}
      {...listeners}
      {...conditionalListeners}
      className={`bg-white p-3 rounded-lg shadow-md group transition-transform duration-200 ${isDesktop ? 'touch-none' : ''} ${overlayClasses} ${cursorClasses}`}
    >
      <div className="flex justify-between h-full">
        <div className="flex flex-col justify-between">
          <h2 className="font-semibold text-gray-800 pr-2">{titulo}</h2>
          <div className="flex items-center gap-3 mt-2">
            <span className={`text-xs font-semibold px-2.5 py-0.5 rounded-full ${prioridadeEstilo}`}>
              {prioridade}
            </span>
            <span className="text-sm text-gray-500">{prazo}</span>
          </div>
        </div>
        <div className="flex flex-col justify-between items-center gap-0.5">
          <button className="text-gray-400 hover:text-gray-800 opacity-0 group-hover:opacity-100 transition-opacity -mt-1">
            <i className="fa-solid fa-ellipsis-vertical"></i>
          </button>
          <div
            title={responsavel.nome}
            className="w-6 h-6 rounded-full bg-indigo-500 text-white flex items-center justify-center text-sm font-bold"
          >
            {responsavel.nome.charAt(0).toUpperCase()}
          </div>
        </div>
      </div>
    </div>
  );
}
