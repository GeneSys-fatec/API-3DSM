import { useEffect, useState } from "react";
import TeamCard from "./CardEquipe";
import ModalCriarEquipe from "./ModalCriarEquipe";
import { getMinhasEquipes } from "./teamService";
import { Equipe } from "@/types/types";
import { toast } from "react-toastify";
import { authFetch } from "@/utils/api";
import ModalConfirmacao from "@/components/ui/ModalConfirmacao";

export default function ListaEquipes() {
  const [equipes, setEquipes] = useState<Equipe[]>([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [equipeParaExcluir, setEquipeParaExcluir] = useState<Equipe | null>(
    null
  );

  const cores = [
    "blue-400",
    "green-500",
    "purple-400",
    "orange-400",
    "red-400",
  ];

  useEffect(() => {
    async function fetchEquipes() {
      try {
        const data = await getMinhasEquipes();
        setEquipes(data);
      } catch (error) {
        console.error(error);
        if (error instanceof Error) {
          toast.error(error.message);
        } else {
          toast.error("Um erro inesperado ocorreu ao buscar equipes.");
        }
      }
    }
    fetchEquipes();
    window.addEventListener("equipe:created", fetchEquipes as EventListener);
    return () => {
      window.removeEventListener(
        "equipe:created",
        fetchEquipes as EventListener
      );
    };
  }, []);

  const handleOpenModal = () => setIsModalOpen(true);
  const handleCloseModal = () => setIsModalOpen(false);

  const confirmarExclusao = (equipe: Equipe) => {
    setEquipeParaExcluir(equipe);
  };

  const executarExclusao = async () => {
    if (!equipeParaExcluir) return;
    try {
      const response = await authFetch(
        `http://localhost:8080/equipe/apagar/${equipeParaExcluir.equId}`,
        {
          method: "DELETE",
        }
      );
      if (response.ok) {
        setEquipes((prev) =>
          prev.filter((e) => e.equId !== equipeParaExcluir.equId)
        );
        toast.success("Equipe excluída com sucesso!");
      } else {
        toast.error("Erro ao excluir equipe.");
      }
    } catch (error) {
      console.error(error);
      toast.error("Erro de conexão ao excluir equipe.");
    } finally {
      setEquipeParaExcluir(null);
    }
  };

  const handleCancelExclusao = () => {
    setEquipeParaExcluir(null);
  };

  if (equipes.length === 0) {
    return (
      <div className="flex flex-col justify-center items-center h-full text-center p-4">
        <i className="fa-solid fa-users text-5xl text-slate-300 mb-4"></i>
        <h2 className="text-xl font-bold text-slate-600">
          Nenhuma equipe cadastrada
        </h2>
        <p className="text-slate-500 mt-1 pb-2">
          Crie uma equipe para começar a organizar seus membros.
        </p>
        <button
          onClick={handleOpenModal}
          className="mt-4 px-6 py-2 bg-indigo-600 hover:bg-indigo-800 text-white rounded-lg transition-colors"
        >
          Criar Nova Equipe
        </button>
        <ModalCriarEquipe isOpen={isModalOpen} onClose={handleCloseModal} />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 p-8 overflow-y-auto">
      <div className="flex flex-col md:flex-row md:justify-between md:items-center pb-2 px-4">
        <div className="mb-4 md:mb-0 flex flex-col gap-2">
          <h1 className="text-3xl font-extrabold text-gray-900">Equipes</h1>
          <hr className="text-gray-400" />
          <p className="text-gray-500 mt-1">
            Acesse e gerencie todas as suas equipes em um só lugar.
          </p>
        </div>

        <button
          onClick={handleOpenModal}
          className="px-6 py-3 bg-indigo-600 text-white font-medium rounded-xl hover:bg-indigo-700 transition shadow-md hover:shadow-lg flex items-center justify-center w-full md:w-auto"
        >
          Criar Equipe
        </button>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 p-4">
        {equipes.map((equipe, i) => (
          <TeamCard
            key={equipe.equId}
            id={equipe.equId}
            equNome={equipe.equNome}
            equDescricao={equipe.equDescricao}
            usuarios={equipe.equMembros ?? []}
            corClasse={cores[i % cores.length]}
            onDelete={() => confirmarExclusao(equipe)}
            onEdit={() =>
              toast.warn("Funcionalidade de edição ainda não implementada.")
            }
          />
        ))}
      </div>

      <ModalCriarEquipe isOpen={isModalOpen} onClose={handleCloseModal} />

      {equipeParaExcluir && (
        <ModalConfirmacao
          titulo="Confirmar Exclusão da Equipe"
          mensagem={
            <p>
              Deseja realmente excluir a equipe{" "}
              <strong>{equipeParaExcluir.equNome}</strong>?
              <br />
              Esta ação é irreversível.
            </p>
          }
          onConfirm={executarExclusao}
          onCancel={handleCancelExclusao}
          confirmText="Sim, Excluir"
          cancelText="Cancelar"
        />
      )}
    </div>
  );
}
