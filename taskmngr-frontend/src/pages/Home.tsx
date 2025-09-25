import React, { useState, useEffect, useContext, useCallback } from "react";
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
  tar_prioridade: "Alta" | "Média" | "Baixa";
  tar_descricao: string;
  tar_anexo?: File | null;
}

export type NovaTarefa = Omit<Tarefa, "tar_id">;

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

const adicionarCoresAsColunas = (
  colunasDaAPI: Omit<Coluna, "corClasse" | "corFundo">[]
): Coluna[] => {
  const paletaDeCores = [
    { corClasse: "orange-400", corFundo: "bg-orange-400/35" },
    { corClasse: "blue-400", corFundo: "bg-blue-400/40" },
    { corClasse: "green-500", corFundo: "bg-green-500/40" },
    { corClasse: "purple-400", corFundo: "bg-purple-400/35" },
    { corClasse: "red-400", corFundo: "bg-red-400/35" },
  ];

  return colunasDaAPI.map((coluna, index) => ({
    ...coluna,
    ...paletaDeCores[index % paletaDeCores.length],
  }));
};

const dropAnimation: DropAnimation = {
  sideEffects: defaultDropAnimationSideEffects({
    styles: {
      active: { opacity: "0.5" },
    },
  }),
};

export default function Home() {
  const [colunas, setColunas] = useState<Coluna[]>([]);
  const [tarefas, setTarefas] = useState<{ [key: string]: Tarefa[] }>({});
  const [activeTask, setActiveTask] = useState<Tarefa | null>(null);
  const [loading, setLoading] = useState(true);
  const modalContext = useContext(ModalContext);
  const [editingColumnId, setEditingColumnId] = useState<string | null>(null);
  const [tarefaParaExcluir, setTarefaParaExcluir] = useState<Tarefa | null>(
    null
  );
  const [colunaParaExcluir, setColunaParaExcluir] = useState<Coluna | null>(
    null
  );

  const handleUpdateColumnTitle = useCallback(
    async (id: string, newTitle: string) => {
      const originalColumns = [...colunas];
      const columnToUpdate = originalColumns.find((c) => c.id === id);

      if (!columnToUpdate || columnToUpdate.titulo === newTitle) {
        setEditingColumnId(null);
        return;
      }

      setColunas((prev) =>
        prev.map((c) => (c.id === id ? { ...c, titulo: newTitle } : c))
      );
      setEditingColumnId(null);

      try {
        const response = await fetch(`http://localhost:8080/colunas/atualizar/${id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ titulo: newTitle }),
        });

        if (!response.ok)
          throw new Error("Falha ao atualizar o título no servidor.");
      } catch (error) {
        console.error("Erro ao atualizar título da coluna:", error);

        setColunas(originalColumns);
        console.log("Não foi possível atualizar o título da coluna.");
      }
    },
    [colunas]
  );

  const abrirModalExclusao = useCallback((tarefa: Tarefa) => {
    setTarefaParaExcluir(tarefa);
  }, []);

  const cancelarExclusao = useCallback(() => {
    setTarefaParaExcluir(null);
  }, []);

  const executarExclusao = useCallback(async () => {
    if (!tarefaParaExcluir) return;

    try {
      const response = await fetch(
        `http://localhost:8080/tarefa/apagar/${tarefaParaExcluir.tar_id}`,
        {
          method: "DELETE",
        }
      );

      if (!response.ok) {
        throw new Error("Erro ao excluir a tarefa.");
      }

      setTarefas((prevTarefas) => {
        const novasTarefas = { ...prevTarefas };
        const colunaDaTarefa = novasTarefas[tarefaParaExcluir.tar_status];
        if (colunaDaTarefa) {
          novasTarefas[tarefaParaExcluir.tar_status] = colunaDaTarefa.filter(
            (t) => t.tar_id !== tarefaParaExcluir.tar_id
          );
        }
        return novasTarefas;
      });
    } catch (error) {
      console.error("Falha ao excluir tarefa:", error);
      console.log("Não foi possível excluir a tarefa.");
    } finally {
      setTarefaParaExcluir(null);
    }
  }, [tarefaParaExcluir]);

  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: { distance: 10 },
    }),
    useSensor(KeyboardSensor, {})
  );

  const fetchData = useCallback(async () => {
    try {
      const [colunasResponse, tarefasResponse] = await Promise.all([
        fetch("http://localhost:8080/colunas"),
        fetch("http://localhost:8080/tarefa/listar"),
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

  const encontrarColunaDaTarefa = (
    tarefaId: string,
    tarefasAtuais: { [key: string]: Tarefa[] }
  ) => {
    if (!tarefaId) return null;
    return Object.keys(tarefasAtuais).find((colunaTitulo) =>
      tarefasAtuais[colunaTitulo].some((tarefa) => tarefa.tar_id === tarefaId)
    );
  };

  const handleDragStart = (event: DragStartEvent) => {
    const activeId = event.active.id as string;
    const colunaTitulo = encontrarColunaDaTarefa(activeId, tarefas);
    if (colunaTitulo) {
      const tarefa = tarefas[colunaTitulo].find((t) => t.tar_id === activeId);
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
    const overColuna = colunas.find((c) => c.id === overId);
    const overContainer = overColuna
      ? overColuna.titulo
      : encontrarColunaDaTarefa(overId, tarefas);

    if (!activeContainer || !overContainer || activeContainer === overContainer)
      return;

    const tarefaMovidaOriginal = tarefas[activeContainer]?.find(
      (t) => t.tar_id === activeId
    );
    if (!tarefaMovidaOriginal) return;

    setTarefas((prev) => {
      const newTarefas = JSON.parse(JSON.stringify(prev));
      const activeItems = newTarefas[activeContainer];
      const activeIndex = activeItems.findIndex(
        (item: Tarefa) => item.tar_id === activeId
      );
      if (activeIndex === -1) return prev;

      const [movedItem] = activeItems.splice(activeIndex, 1);
      movedItem.tar_status = overContainer;

      const overItems = newTarefas[overContainer];
      let overIndex = overItems.findIndex(
        (item: Tarefa) => item.tar_id === overId
      );
      if (overIndex === -1) overIndex = overItems.length;
      overItems.splice(overIndex, 0, movedItem);

      return newTarefas;
    });

    try {
      const corpoDaRequisicao = {
        ...tarefaMovidaOriginal,
        tar_status: overContainer,
      };

      if (
        !corpoDaRequisicao.tar_descricao ||
        corpoDaRequisicao.tar_descricao.trim() === ""
      ) {
        corpoDaRequisicao.tar_descricao = " ";
      }

      const response = await fetch(
        `http://localhost:8080/tarefa/atualizar/${activeId}`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(corpoDaRequisicao),
        }
      );

      if (!response.ok)
        throw new Error("Falha ao atualizar a tarefa no servidor.");
    } catch (error) {
      console.error(
        "Erro ao salvar a mudança da tarefa, revertendo a UI:",
        error
      );
      fetchData();
    }
  };

  const handleAddColumn = useCallback(async () => {
    try {
      const response = await fetch("http://localhost:8080/colunas/cadastrar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },

        body: JSON.stringify({ titulo: "Nova Coluna" }),
      });

      if (!response.ok) {
        throw new Error("Falha ao criar a coluna no servidor.");
      }

      const novaColunaSalva = await response.json();

      setColunas((prevColunas) => {
        const colunasAtualizadas = [...prevColunas, novaColunaSalva];

        return adicionarCoresAsColunas(colunasAtualizadas);
      });

      setEditingColumnId(novaColunaSalva.id);
    } catch (error) {
      console.error("Erro ao adicionar nova coluna:", error);
    }
  }, [colunas]);

  const adicionarTarefa = useCallback(
    async (novaTarefa: NovaTarefa) => {
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
    },
    [fetchData]
  );

  const editarTarefa = useCallback(
    async (tarefaAtualizada: Tarefa) => {
      try {
        const response = await fetch(
          `http://localhost:8080/tarefa/atualizar/${tarefaAtualizada.tar_id}`,
          {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              ...tarefaAtualizada,
              proj_nome: "API-3sem",
            }),
          }
        );
        if (!response.ok) throw new Error("Erro ao atualizar tarefa");
        await fetchData();
      } catch (error) {
        console.error("Falha ao editar tarefa:", error);
      }
    },
    [fetchData]
  );

  const abrirModalCriacao = useCallback(
    (statusDaColuna: string) => {
      if (modalContext) {
        modalContext.openModal(
          <ModalCriarTarefas
            onAdicionarTarefa={adicionarTarefa}
            statusInicial={statusDaColuna}
          />
        );
      }
    },
    [modalContext, adicionarTarefa]
  );

  const abrirModalEdicao = useCallback(
    (tarefa: Tarefa) => {
      if (modalContext) {
        modalContext.openModal(
          <ModalEditarTarefas tarefa={tarefa} onSave={editarTarefa} />
        );
      }
    },
    [modalContext, editarTarefa]
  );

  const iniciarExclusaoColuna = useCallback(
    (coluna: Coluna) => {
      const tarefasNestaColuna = tarefas[coluna.titulo] || [];
      if (tarefasNestaColuna.length > 0) {
        console.warn(
          `AÇÃO BLOQUEADA: Não é possível apagar a coluna "${coluna.titulo}" porque ela contém tarefas.`
        );
        return;
      }

      setColunaParaExcluir(coluna);
    },
    [tarefas]
  );

  const cancelarExclusaoColuna = useCallback(() => {
    setColunaParaExcluir(null);
  }, []);

  const executarExclusaoColuna = useCallback(async () => {
    if (!colunaParaExcluir) return;

    try {
      const response = await fetch(
        `http://localhost:8080/colunas/deletar/${colunaParaExcluir.id}`,
        {
          method: "DELETE",
        }
      );

      if (!response.ok) {
        throw new Error("Falha ao apagar a coluna no servidor.");
      }

      setColunas((prevColunas) =>
        prevColunas.filter((c) => c.id !== colunaParaExcluir.id)
      );
    } catch (error) {
      console.error("Erro ao apagar coluna:", error);
    } finally {
      setColunaParaExcluir(null);
    }
  }, [colunaParaExcluir]);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-screen text-xl font-semibold">
        Carregando quadro...
      </div>
    );
  }

  return (
    <>
      <DndContext
        sensors={sensors}
        collisionDetection={rectIntersection}
        onDragStart={handleDragStart}
        onDragEnd={handleDragEnd}
      >
        <div className="flex flex-col h-full lg:flex-row items-center lg:items-start gap-5 pt-5 pb-4 lg:pr-4 flex-1 overflow-y-auto lg:overflow-x-auto lg:overflow-y-hidden">
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
              onApagarColuna={() => iniciarExclusaoColuna(coluna)}
              onExcluirTarefa={abrirModalExclusao}
              isEditing={editingColumnId === coluna.id}
              onStartEditing={setEditingColumnId}
              onFinishEditing={handleUpdateColumnTitle}
            />
          ))}
          <DragOverlay dropAnimation={dropAnimation}>
            {activeTask ? <CardTarefa tarefa={activeTask} isOverlay /> : null}
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
      {tarefaParaExcluir && (
        <div className="fixed inset-0 bg-black/60 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg p-6 max-w-md w-full shadow-xl">
            <div className="text-center">
              <i className="fa-solid fa-triangle-exclamation text-red-500 text-4xl mb-4"></i>
              <h3 className="text-lg font-semibold text-gray-900 mb-4">
                Tem certeza?
              </h3>
              <p className="text-gray-600">
                A tarefa "
                <span className="font-bold">
                  {tarefaParaExcluir.tar_titulo}
                </span>
                " será excluída permanentemente.
              </p>
              <div
                className="flex gap-4 justify-center"
                style={{ paddingTop: "20px" }}
              >
                <button
                  onClick={cancelarExclusao}
                  className="px-6 py-3 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition-colors font-medium"
                >
                  Cancelar
                </button>
                <button
                  onClick={executarExclusao}
                  className="px-6 py-3 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors font-medium"
                >
                  Excluir
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
      {colunaParaExcluir && (
        <div className="fixed inset-0 bg-black/60 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-lg p-6 max-w-md w-full shadow-xl">
            <div className="text-center">
              <i className="fa-solid fa-triangle-exclamation text-red-500 text-4xl mb-4"></i>
              <h3 className="text-lg font-semibold text-gray-900 mb-4">
                Excluir Coluna?
              </h3>
              <p className="text-gray-600">
                A coluna "
                <span className="font-bold">{colunaParaExcluir.titulo}</span>"
                será excluída permanentemente. Esta ação não pode ser desfeita.
              </p>
              <div
                className="flex gap-4 justify-center"
                style={{ paddingTop: "20px" }}
              >
                <button
                  onClick={cancelarExclusaoColuna}
                  className="px-6 py-3 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition-colors font-medium"
                >
                  Cancelar
                </button>
                <button
                  onClick={executarExclusaoColuna}
                  className="px-6 py-3 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors font-medium"
                >
                  Excluir
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
