import React, { useState } from "react";
import ColunaKanban from "../components/ColunaKanban";
import CardTarefa from "../components/CardTarefa";
import { 
  DndContext, 
  type DragEndEvent,
  type DragStartEvent,
  DragOverlay,
  PointerSensor,
  KeyboardSensor,
  useSensor,
  useSensors,
  rectIntersection,
  defaultDropAnimationSideEffects,
  type DropAnimation,
} from "@dnd-kit/core";
import { arrayMove } from "@dnd-kit/sortable";

export interface Tarefa {
  id: number;
  titulo: string;
  descricao?: string;
  prioridade: "Alta" | "Média" | "Baixa";
  entrega: string;
  responsavel: string;
  status: string;
}

type Coluna = {
  id: string;
  titulo: string;
  corClasse: string;
  corFundo: string;
};

const tarefasIniciais: Tarefa[] = [
  { id: 1, titulo: "Implementar login", descricao: "Adicionar autenticação com JWT", prioridade: "Alta", entrega: "22/08/2025", responsavel: "Matheus", status: "Pendente" },
  { id: 2, titulo: "Criar dashboard", prioridade: "Média", entrega: "30/09/2025", responsavel: "Ana Júlia", status: "Em desenvolvimento" },
  { id: 3, titulo: "Documentar API", prioridade: "Baixa", entrega: "15/10/2025", responsavel: "Lavínia", status: "Feito" },
  { id: 4, titulo: "Configurar ambiente", descricao: "", prioridade: "Média", entrega: "22/08/2025", responsavel: "Gabriel", status: "Pendente" },
  { id: 5, titulo: "Configurar rotas", prioridade: "Baixa", entrega: "30/09/2025", responsavel: "Ana Beatriz", status: "Pendente" },
  { id: 6, titulo: "Autenticação de usuário", prioridade: "Média", entrega: "15/10/2025", responsavel: "Giovanni", status: "Feito" },
];

const colunasIniciais: Coluna[] = [
  { id: "Pendente", titulo: "Pendente", corClasse: "orange-400", corFundo: "bg-orange-400/35" },
  { id: "Em desenvolvimento", titulo: "Em desenvolvimento", corClasse: "blue-400", corFundo: "bg-blue-400/40" },
  { id: "Feito", titulo: "Feito", corClasse: "green-500", corFundo: "bg-green-500/40" },
];

const agruparTarefasPorColuna = (tarefas: Tarefa[], colunas: Coluna[]) => {
  const tarefasAgrupadas: { [key: string]: Tarefa[] } = {};
  colunas.forEach((coluna) => {
    tarefasAgrupadas[coluna.id] = tarefas.filter(
      (tarefa) => tarefa.status === coluna.id
    );
  });
  return tarefasAgrupadas;
};

const dropAnimation: DropAnimation = {
  sideEffects: defaultDropAnimationSideEffects({
    styles: {
      active: {
        opacity: '0.5',
      },
    },
  }),
};

export default function Home() {
  const [colunas, setColunas] = useState<Coluna[]>(colunasIniciais);
  const [tarefas, setTarefas] = useState<{ [key: string]: Tarefa[] }>(
    agruparTarefasPorColuna(tarefasIniciais, colunasIniciais)
  );
  const [activeTask, setActiveTask] = useState<Tarefa | null>(null);

  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: {
        distance: 10,
      },
    }),
    useSensor(KeyboardSensor, {})
  );

  const encontrarColunaDaTarefa = (tarefaId: number, tarefasAtuais: { [key: string]: Tarefa[] }) => {
    if (!tarefaId) return null;
    return Object.keys(tarefasAtuais).find((colunaId) =>
      tarefasAtuais[colunaId].some((tarefa) => tarefa.id === tarefaId)
    );
  };
  
  const handleDragStart = (event: DragStartEvent) => {
    const { active } = event;
    const activeId = active.id as number;
    const colunaId = encontrarColunaDaTarefa(activeId, tarefas);
    if (colunaId) {
      const tarefa = tarefas[colunaId].find(t => t.id === activeId);
      if (tarefa) setActiveTask(tarefa);
    }
  };

  const handleDragEnd = (event: DragEndEvent) => {
    setActiveTask(null);
    const { active, over } = event;
    if (!over) return;

    const activeId = active.id as number;
    const overId = over.id as number | string;

    if (activeId === overId) return;

    setTarefas((prev) => {
      const activeContainer = encontrarColunaDaTarefa(activeId, prev);
      const overContainer = encontrarColunaDaTarefa(overId as number, prev) || (typeof overId === 'string' ? overId : null);
      
      if (!activeContainer || !overContainer) return prev;

      const newTarefas = JSON.parse(JSON.stringify(prev));

      if (activeContainer === overContainer) {
        const items = newTarefas[activeContainer];
        const oldIndex = items.findIndex((item: Tarefa) => item.id === activeId);
        const newIndex = items.findIndex((item: Tarefa) => item.id === overId);
        if (oldIndex !== -1 && newIndex !== -1) {
          newTarefas[activeContainer] = arrayMove(items, oldIndex, newIndex);
        }
      } else {
        const activeItems = newTarefas[activeContainer];
        const overItems = newTarefas[overContainer];
        const activeIndex = activeItems.findIndex((item: Tarefa) => item.id === activeId);
        const [movedItem] = activeItems.splice(activeIndex, 1);
        movedItem.status = overContainer;
        
        let overIndex = overItems.findIndex((item: Tarefa) => item.id === overId);
        if (overIndex === -1) overIndex = overItems.length;
        
        overItems.splice(overIndex, 0, movedItem);

        newTarefas[activeContainer] = activeItems;
        newTarefas[overContainer] = overItems;
      }
      return newTarefas;
    });
  };

  const handleAddColumn = () => {
    const nomeNovaColuna = prompt("Digite o nome da nova coluna:");
    if (nomeNovaColuna && nomeNovaColuna.trim() !== "") {
      const novaColuna: Coluna = {
        id: nomeNovaColuna,
        titulo: nomeNovaColuna,
        corClasse: "gray-400", 
        corFundo: "bg-gray-400/35",
      };
      
      setColunas((prevColunas) => [...prevColunas, novaColuna]);
      
      setTarefas((prevTarefas) => ({
        ...prevTarefas,
        [novaColuna.id]: [],
      }));
    }
  };

  return (
    <DndContext 
      sensors={sensors} 
      collisionDetection={rectIntersection}
      onDragStart={handleDragStart}
      onDragEnd={handleDragEnd}
    >
      <div className="flex flex-col h-full lg:flex-row items-center lg:items-start gap-6 pt-5 pb-4 lg:pr-4 flex-1 overflow-y-auto lg:overflow-x-auto lg:overflow-y-hidden">
        {colunas.map((coluna) => (
            <ColunaKanban
              key={coluna.id}
              id={coluna.id}
              titulo={coluna.titulo}
              corClasse={coluna.corClasse}
              corFundo={coluna.corFundo}
              tarefas={tarefas[coluna.id] || []}
            />
        ))}

        <DragOverlay dropAnimation={dropAnimation}>
          {activeTask ? (
             <CardTarefa
                id={activeTask.id}
                titulo={activeTask.titulo}
                prioridade={activeTask.prioridade}
                prazo={activeTask.entrega}
                responsavel={{ nome: activeTask.responsavel }}
                corClasse={colunas.find(c => c.id === activeTask.status)?.corClasse || 'gray-400'}
                isOverlay
              />
          ) : null}
        </DragOverlay>

        {/* CORREÇÃO DEFINITIVA: Removida a classe 'mt-auto' */}
        <div className="w-full lg:w-80 flex-shrink-0 lg:mt-0">
          <button
            onClick={handleAddColumn}
            className="w-full rounded-lg bg-gray-200/70 p-3 text-center transition-colors hover:bg-gray-300 lg:h-full flex items-center justify-center"
          >
            <h2 className="text-xl font-bold tracking-wider text-gray-600">
              + Adicionar Card
            </h2>
          </button>
        </div>
      </div>
    </DndContext>
  );
}

