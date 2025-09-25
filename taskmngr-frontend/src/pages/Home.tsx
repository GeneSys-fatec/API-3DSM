import React, { useState, useEffect, useContext, useCallback } from "react"; // NOVO: useContext
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
import { ModalContext } from "../context/ModalContext";
import ModalCriarTarefas from "../components/ModalCriarTarefas";
import ModalEditarTarefas from "../components/ModalEditarTarefas";

export interface Tarefa {
  tar_id: string;
  tar_titulo: string;
  tar_status: string;
  usu_nome: string;
  tar_prazo: string;
  tar_prioridade: "Alta" | "Média" | "Baixa" ;
  tar_descricao: string;
  tar_anexo?: File | null;
}

export type NovaTarefa = Omit<Tarefa, 'tar_id'>;

type Coluna = {
  id: string;
  titulo: string;
  ordem: number;
  corClasse: string;
  corFundo: string;
};

const agruparTarefasPorColuna = (tarefas: Tarefa[], colunas: Coluna[]) => {
  const tarefasAgrupadas: { [key: string]: Tarefa[] } = {};
  colunas.forEach((coluna) => {
    tarefasAgrupadas[coluna.titulo] = tarefas.filter(
      (tarefa) => tarefa.tar_status === coluna.titulo
    );
  });
  return tarefasAgrupadas;
};

const adicionarCoresAsColunas = (colunasDaAPI: Omit<Coluna, 'corClasse' | 'corFundo'>[]): Coluna[] => {
  const paletaDeCores = [
    { corClasse: "orange-400", corFundo: "bg-orange-400/35" },
    { corClasse: "blue-400", corFundo: "bg-blue-400/40" },
    { corClasse: "green-500", corFundo: "bg-green-500/40" },
    { corClasse: "purple-400", corFundo: "bg-purple-400/35" },
    { corClasse: "red-400", corFundo: "bg-red-400/35" },
  ];

  return colunasDaAPI.map((coluna, index) => ({
    ...coluna,
    ...paletaDeCores[index % paletaDeCores.length]
  }));
}


const dropAnimation: DropAnimation = {
  sideEffects: defaultDropAnimationSideEffects({
    styles: {
      active: { opacity: '0.5' },
    },
  }),
};


export default function Home() {
  const [colunas, setColunas] = useState<Coluna[]>([]);
  const [tarefas, setTarefas] = useState<{ [key: string]: Tarefa[] }>({});
  const [activeTask, setActiveTask] = useState<Tarefa | null>(null);
  const [loading, setLoading] = useState(true);
  const modalContext = useContext(ModalContext);

  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: { distance: 10 },
    }),
    useSensor(KeyboardSensor, {})
  );

  const fetchData = useCallback(async () => {
    try {
      const [colunasResponse, tarefasResponse] = await Promise.all([
        fetch('http://localhost:8080/colunas'),
        fetch('http://localhost:8080/tarefa/listar')
      ]);
      if (!colunasResponse.ok || !tarefasResponse.ok) {
        throw new Error("Falha ao buscar dados da API");
      }
      let colunasData: Coluna[] = await colunasResponse.json();
      const tarefasData: Tarefa[] = await tarefasResponse.json();
      colunasData.sort((a, b) => a.ordem - b.ordem);
      colunasData = adicionarCoresAsColunas(colunasData);
      setColunas(colunasData);
      setTarefas(agruparTarefasPorColuna(tarefasData, colunasData));
    } catch (error) {
      console.error("Erro ao carregar o quadro:", error);
    } finally {
      if (loading) setLoading(false);
    }
  }, []);

  useEffect(() => {
    setLoading(true);
    fetchData();
  }, []);

  const encontrarColunaDaTarefa = (tarefaId: string, tarefasAtuais: { [key: string]: Tarefa[] }) => {
    if (!tarefaId) return null;
    return Object.keys(tarefasAtuais).find((colunaTitulo) =>
      tarefasAtuais[colunaTitulo].some((tarefa) => tarefa.tar_id === tarefaId)
    );
  };


  const handleDragStart = (event: DragStartEvent) => {
    const activeId = event.active.id as string;
    const colunaTitulo = encontrarColunaDaTarefa(activeId, tarefas);
    if (colunaTitulo) {
      const tarefa = tarefas[colunaTitulo].find(t => t.tar_id === activeId);
      if (tarefa) setActiveTask(tarefa);
    }
  };

  const handleDragEnd = async (event: DragEndEvent) => {
    setActiveTask(null);
    const { active, over } = event;
    if (!over || active.id === over.id) return;

    const activeId = active.id as string;
    const overId = over.id as string;

    const activeContainer = encontrarColunaDaTarefa(activeId, tarefas);
    const overColuna = colunas.find(c => c.id === overId);
    const overContainer = overColuna ? overColuna.titulo : encontrarColunaDaTarefa(overId, tarefas);

    if (!activeContainer || !overContainer || activeContainer === overContainer) return;
    
    const tarefaMovidaOriginal = tarefas[activeContainer]?.find(t => t.tar_id === activeId);
    if (!tarefaMovidaOriginal) return;

    setTarefas(prev => {
        const newTarefas = JSON.parse(JSON.stringify(prev));
        const activeItems = newTarefas[activeContainer];
        const activeIndex = activeItems.findIndex((item: Tarefa) => item.tar_id === activeId);
        if (activeIndex === -1) return prev;
        
        const [movedItem] = activeItems.splice(activeIndex, 1);
        movedItem.tar_status = overContainer;
        
        const overItems = newTarefas[overContainer];
        let overIndex = overItems.findIndex((item: Tarefa) => item.tar_id === overId);
        if (overIndex === -1) overIndex = overItems.length;
        overItems.splice(overIndex, 0, movedItem);

        return newTarefas;
    });

    try {
        const corpoDaRequisicao = { ...tarefaMovidaOriginal, tar_status: overContainer };

        if (!corpoDaRequisicao.tar_descricao || corpoDaRequisicao.tar_descricao.trim() === '') {
            corpoDaRequisicao.tar_descricao = " ";
        }

        const response = await fetch(`http://localhost:8080/tarefa/atualizar/${activeId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(corpoDaRequisicao)
        });

        if (!response.ok) throw new Error('Falha ao atualizar a tarefa no servidor.');

    } catch (error) {
        console.error("Erro ao salvar a mudança da tarefa, revertendo a UI:", error);
        fetchData();
    }
  };

  const handleAddColumn = useCallback(async () => {
    const nomeNovaColuna = prompt("Digite o nome da nova coluna:");

    if (!nomeNovaColuna || nomeNovaColuna.trim() === "") {
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/colunas', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ titulo: nomeNovaColuna.trim() })
      });

      if (!response.ok) {
        throw new Error("Falha ao criar a coluna no servidor.");
      }

      await fetchData();

    } catch (error) {
      console.error("Erro ao adicionar nova coluna:", error);
      alert("Não foi possível adicionar a coluna. Tente novamente.");
    }
  }, [fetchData]);

  const adicionarTarefa = useCallback(async (novaTarefa: NovaTarefa) => {
    try {
      const response = await fetch("http://localhost:8080/tarefa/cadastrar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...novaTarefa, proj_nome: "API-3sem" }),
      });
      if (!response.ok) throw new Error("Erro ao cadastrar tarefa");
      await fetchData();
    } catch (error) {
      console.error("Falha ao adicionar tarefa:", error);
    }
  }, [fetchData]);

  const editarTarefa = useCallback(async (tarefaAtualizada: Tarefa) => {
    try {
      const response = await fetch(`http://localhost:8080/tarefa/atualizar/${tarefaAtualizada.tar_id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...tarefaAtualizada, proj_nome: "API-3sem" }),
      });
      if (!response.ok) throw new Error("Erro ao atualizar tarefa");
      await fetchData(); // Recarrega os dados do quadro
    } catch (error) {
      console.error("Falha ao editar tarefa:", error);
    }
  }, [fetchData]);

  const abrirModalCriacao = useCallback((statusDaColuna: string) => {
    if (modalContext) {
      modalContext.openModal(
        <ModalCriarTarefas onAdicionarTarefa={adicionarTarefa} statusInicial={statusDaColuna} />
      );
    }
  }, [modalContext, adicionarTarefa]);

  const abrirModalEdicao = useCallback((tarefa: Tarefa) => {
    if (modalContext) {
      modalContext.openModal(
        <ModalEditarTarefas tarefa={tarefa} onSave={editarTarefa} />
      );
    }
  }, [modalContext, editarTarefa]);

  if (loading) {
    return <div className="flex justify-center items-center h-screen text-xl font-semibold">Carregando quadro...</div>;
  }

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
            tarefas={tarefas[coluna.titulo] || []}
            onAbrirModalCriacao={() => abrirModalCriacao(coluna.titulo)}
            onAbrirModalEdicao={abrirModalEdicao}
          />
        ))}
        <DragOverlay dropAnimation={dropAnimation}>
          {activeTask ? (
            <CardTarefa
              tarefa={activeTask}
              isOverlay
            />
          ) : null}
        </DragOverlay>
        <div className="w-full lg:w-80 flex-shrink-0 lg:mt-0">
          <button
            onClick={handleAddColumn}
            className="w-full rounded-lg bg-gray-200/70 p-3 text-center transition-colors hover:bg-gray-300 lg:h-full flex items-center justify-center"
          >
            <h2 className="text-xl font-bold tracking-wider text-gray-600">
              + Adicionar Coluna
            </h2>
          </button>
        </div>
      </div>
    </DndContext>
  );
}
