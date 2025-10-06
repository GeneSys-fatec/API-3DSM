import { useState, useEffect, useCallback } from "react";
import ModalEditarProjetos from "../features/projects/ModalEditarProjetos";
import { authFetch } from "@/utils/api";
import type { Projeto } from "@/types/types";
import { useOptionsMenu } from "@/hooks/useOptionsMenu";

type ProjetoDaAPI = {
  proj_id?: string | number;
  id?: string | number;
  proj_nome?: string;
  nome?: string;
  proj_descricao?: string;
  descricao?: string;
  proj_dataCriacao?: string;
  dataCriacao?: string;
};

interface BarraLateralProjetosProps {
  isOpen: boolean;
  onClose: () => void;
  onOpenModal: () => void;
  onProjectSelect: (projectId: string | null) => void;
}

export default function BarraLateralProjetos({
  isOpen,
  onClose,
  onOpenModal,
  onProjectSelect,
}: BarraLateralProjetosProps) {
  const [activeProjectId, setActiveProjectId] = useState<string | null>(null);
  const [projetos, setProjetos] = useState<Projeto[]>([]);
  const [isDeleting, setIsDeleting] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [projetoParaEditar, setProjetoParaEditar] = useState<Projeto | null>(
    null
  );

  const optionsMenu = useOptionsMenu();

  const fetchProjetos = useCallback(async () => {
    try {
      const response = await authFetch(
        "http://localhost:8080/projeto/meus-projetos"
      );
      if (!response.ok) throw new Error("Erro ao buscar projetos");
      const data = await response.json();

      const normalized: Projeto[] = (Array.isArray(data) ? data : []).map(
        (d: ProjetoDaAPI) => ({
          id: String(d.proj_id ?? d.id ?? ""),
          nome: String(d.proj_nome ?? d.nome ?? ""),
          descricao: String(d.proj_descricao ?? d.descricao ?? ""),
          dataCriacao: String(d.proj_dataCriacao ?? d.dataCriacao ?? ""),
        })
      );

      setProjetos(normalized);

      if (normalized.length > 0 && !activeProjectId) {
        const firstProjectId = normalized[0].id;
        setActiveProjectId(firstProjectId);
        onProjectSelect(firstProjectId);
      } else if (normalized.length === 0) {
        onProjectSelect(null);
      }
    } catch (error) {
      console.error(error);
    }
  }, [activeProjectId, onProjectSelect]);

  useEffect(() => {
    fetchProjetos();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    const handleProjetoCreated = () => fetchProjetos();
    window.addEventListener("projeto:created", handleProjetoCreated);
    return () => {
      window.removeEventListener("projeto:created", handleProjetoCreated);
    };
  }, [fetchProjetos]);

  const handleEdit = () => {
    if (!optionsMenu.selectedId) return;
    const projeto =
      projetos.find((p) => p.id === optionsMenu.selectedId) ?? null;
    setProjetoParaEditar(projeto);
    setIsEditModalOpen(true);
    optionsMenu.close();
  };

  const handleDelete = async () => {
    if (!optionsMenu.selectedId) return;
    try {
      setIsDeleting(true);
      await authFetch(
        `http://localhost:8080/projeto/apagar/${encodeURIComponent(
          optionsMenu.selectedId
        )}`,
        { method: "DELETE" }
      );
      if (activeProjectId === optionsMenu.selectedId) {
        onProjectSelect(null);
        setActiveProjectId(null);
      }
      await fetchProjetos();
      optionsMenu.close();
    } catch (err) {
      console.error(err);
    } finally {
      setIsDeleting(false);
    }
  };

  return (
    <div
      className={`group fixed lg:static h-full bg-white border-r border-slate-200 flex flex-col
                  z-40 transition-transform lg:transition-all duration-300 ease-in-out
                  ${isOpen ? "translate-x-0" : "-translate-x-full"}
                  lg:translate-x-0 lg:w-16 lg:hover:w-64`}
    >
      <div
        className="
          hidden lg:flex items-center justify-center w-6 h-6 rounded-full absolute -right-1 top-1/2 -translate-y-1/2
          cursor-pointer group-hover:bg-indigo-100 transition-all duration-300
        "
      >
       <i className="fa-solid fa-angles-right text-slate-500 text-lg 
      group-hover:text-indigo-600 
      transition-all duration-300
      group-hover:rotate-180"></i>
      </div>

      
      <div className="p-4 border-b border-slate-200 flex items-center gap-3 overflow-hidden">
        <i className="fa-solid fa-folder-open text-indigo-600 text-2xl"></i>
        <h2
          className={`text-xl font-bold text-slate-800 whitespace-nowrap transition-opacity duration-200
                       ${
                         isOpen ? "opacity-100" : "opacity-0"
                       } lg:opacity-0 lg:group-hover:opacity-100`}
        >
          Projetos
        </h2>
      </div>
      <div className="p-4 overflow-hidden">
        <div className="flex justify-between items-center mb-4 pb-3">
          <h1
            className={`font-bold text-slate-500 text-sm tracking-wider uppercase whitespace-nowrap transition-opacity
                         ${
                           isOpen ? "opacity-100" : "opacity-0"
                         } lg:opacity-0 lg:group-hover:opacity-100`}
          >
            Workspaces
          </h1>
          <button
            onClick={onOpenModal}
            className="flex items-center gap-1 text-indigo-600 hover:text-indigo-800 font-semibold text-sm transition-colors"
          >
            <i className="fa-solid fa-plus"></i>
            <span
              className={`whitespace-nowrap ${
                isOpen ? "opacity-100" : "opacity-0"
              } lg:opacity-0 lg:group-hover:opacity-100`}
            >
              Add
            </span>
          </button>
        </div>
        <div className="flex flex-col gap-1">
          {projetos.map((p) => {
            const isActive = activeProjectId === p.id;
            return (
              <div
                key={p.id}
                onClick={() => {
                  setActiveProjectId(p.id);
                  onProjectSelect(p.id);
                  if (isOpen) onClose();
                }}
                className={`flex items-center justify-between w-full rounded-md p-2 text-md cursor-pointer transition-colors duration-150 ${
                  isActive
                    ? `font-bold ${
                        isOpen
                          ? "bg-indigo-100 text-indigo-700"
                          : "text-slate-800"
                      } lg:group-hover:bg-indigo-100 lg:group-hover:text-indigo-700`
                    : "text-slate-600 hover:bg-slate-100"
                }`}
              >
                <div className="flex items-center gap-2 truncate">
                  <span
                    className={`w-1.5 h-1.5 rounded-full flex-shrink-0 ${
                      isActive ? "bg-indigo-500" : "bg-slate-300"
                    }`}
                  />
                  <p
                    className={`leading-none truncate transition-opacity duration-200 ${
                      isOpen ? "opacity-100" : "opacity-0"
                    } lg:opacity-0 lg:group-hover:opacity-100`}
                  >
                    {p.nome}
                  </p>
                </div>
                <i
                  role="button"
                  aria-label="Mais opções"
                  title="Mais opções"
                  tabIndex={0}
                  onClick={(e) => optionsMenu.open(e, p.id)}
                  className={`fa-solid fa-ellipsis-vertical cursor-pointer p-2 rounded-full flex-shrink-0 transition-opacity duration-200 
                              ${isOpen ? "opacity-100" : "opacity-0"} 
                              lg:opacity-0 lg:group-hover:opacity-100 
                              ${
                                isActive
                                  ? "text-indigo-600"
                                  : "text-slate-400 hover:bg-slate-200"
                              }
                              focus:outline-none focus:ring-2 focus:ring-indigo-400`}
                />
              </div>
            );
          })}
        </div>
      </div>

      {optionsMenu.isOpen && optionsMenu.position && (
        <>
          <div className="fixed inset-0 z-40" onClick={optionsMenu.close}></div>
          <div
            className="fixed z-50 bg-white border border-slate-200 rounded-md shadow-lg w-44 p-2"
            style={{
              top: optionsMenu.position.top,
              left: optionsMenu.position.left,
              transform: "translate(calc(-100% - 6px), -50%)",
            }}
            onClick={(e) => e.stopPropagation()}
          >
            <button
              onClick={handleEdit}
              className="w-full text-left px-3 py-2 rounded hover:bg-slate-100 flex items-center gap-2"
            >
              <i className="fa-solid fa-pen text-slate-600"></i>
              <span>Editar</span>
            </button>
            <button
              onClick={handleDelete}
              disabled={isDeleting}
              className="w-full text-left px-3 py-2 rounded hover:bg-red-50 text-red-700 flex items-center gap-2 disabled:opacity-60"
            >
              <i className="fa-solid fa-trash"></i>
              <span>{isDeleting ? "Excluindo..." : "Excluir"}</span>
            </button>
          </div>
        </>
      )}

      {isEditModalOpen && projetoParaEditar && (
        <ModalEditarProjetos
          isOpen={isEditModalOpen}
          onClose={() => setIsEditModalOpen(false)}
          projeto={projetoParaEditar}
          onSaved={() => {
            fetchProjetos();
            setIsEditModalOpen(false);
          }}
        />
      )}
    </div>
  );
}
