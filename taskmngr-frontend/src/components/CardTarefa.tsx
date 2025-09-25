import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import useMediaQuery from "./hooks/MediaQuerie";
import type { Tarefa } from "../pages/Home"; // Importa a interface Tarefa do Home

// 1. A interface de props foi totalmente atualizada
interface CardTarefaProps {
  tarefa: Tarefa;
  onAbrirModalEdicao?: (tarefa: Tarefa) => void; // Função para abrir o modal (opcional)
  isOverlay?: boolean; // Para estilização especial durante o arraste
}

export default function CardTarefa(props: CardTarefaProps) {
  // 2. Desestruturamos as props principais
  const { tarefa, onAbrirModalEdicao, isOverlay } = props;

  const isDesktop = useMediaQuery('(min-width: 1024px)');

  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging,
  } = useSortable({
    // 3. O ID para o dnd-kit agora vem do objeto tarefa
    id: tarefa.tar_id,
    data: { type: 'task', tarefa }, // Passa a tarefa inteira nos dados do dnd-kit
    disabled: !isDesktop,
  });

  const conditionalListeners = isDesktop ? listeners : undefined;

  const style = {
    transition,
    transform: isOverlay ? undefined : CSS.Transform.toString(transform),
    opacity: isDragging && !isOverlay ? 0 : 1,
  };

  // 4. Acessamos a prioridade a partir do objeto tarefa
  const prioridadeEstilo: { [key: string]: string } = {
    Alta: "bg-red-100 text-red-700",
    Média: "bg-yellow-100 text-yellow-700",
    Baixa: "bg-green-100 text-green-700",
  };
  const classePrioridade = tarefa.tar_prioridade ? prioridadeEstilo[tarefa.tar_prioridade] : "bg-gray-100 text-gray-800";

  const overlayClasses = isOverlay ? `shadow-2xl scale-105 rotate-1` : "hover:scale-105";
  const cursorClasses = isDesktop ? "hover:cursor-grab active:cursor-grabbing" : "cursor-pointer";

  // 5. A inicial do avatar agora é pega de 'usu_nome' de forma segura
  const inicialResponsavel = tarefa.usu_nome?.charAt(0)?.toUpperCase() || '?';

  return (
    <div
      ref={setNodeRef}
      style={style}
      {...attributes}
      {...listeners}
      {...conditionalListeners}
      // 6. Adicionado o evento onClick para abrir o modal de edição
      onClick={() => onAbrirModalEdicao && onAbrirModalEdicao(tarefa)}
      className={`bg-white p-3 rounded-lg shadow-md group transition-transform duration-200 ${isDesktop ? 'touch-none' : ''} ${overlayClasses} ${cursorClasses}`}
    >
      <div className="flex justify-between h-full">
        <div className="flex flex-col justify-between">
          {/* 7. Todas as informações agora vêm do objeto 'tarefa' */}
          <h2 className="font-semibold text-gray-800 pr-2">{tarefa.tar_titulo}</h2>
          <div className="flex items-center gap-3 mt-2">
            <span className={`text-xs font-semibold px-2.5 py-0.5 rounded-full ${classePrioridade}`}>
              {tarefa.tar_prioridade}
            </span>
            <span className="text-sm text-gray-500">{tarefa.tar_prazo}</span>
          </div>
        </div>
        <div className="flex flex-col justify-between items-center gap-0.5">
          <button className="text-gray-400 hover:text-gray-800 opacity-0 group-hover:opacity-100 transition-opacity -mt-1">
            <i className="fa-solid fa-ellipsis-vertical"></i>
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