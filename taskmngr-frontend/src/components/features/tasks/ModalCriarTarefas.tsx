import React, { useState, useEffect, useContext } from "react";
import { ModalContext } from "@/context/ModalContext";
import { toast } from "react-toastify";
import FormularioTarefa from "./FormularioTarefa";
import type { Tarefa, Usuario } from "@/types/types";
import { authFetch } from "@/utils/api";

interface ModalCriarTarefasProps {
  onSuccess: () => void;
  statusInicial: string;
  selectedProjectId: string | null;
}

const estadoInicial: Partial<Tarefa> = {
  tarTitulo: "",
  tarDescricao: "",
  usuId: "",
  usuNome: "Selecione um membro",
  tarPrioridade: "Média",
  tarPrazo: "",
};

export default function ModalCriarTarefas({
  onSuccess,
  statusInicial,
  selectedProjectId,
}: ModalCriarTarefasProps) {
  const modalContext = useContext(ModalContext);
  const [tarefa, setTarefa] = useState<Partial<Tarefa>>({
    ...estadoInicial,
    tarStatus: statusInicial,
  });
  const [anexos, setAnexos] = useState<File[]>([]);
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);

  useEffect(() => {
    authFetch("http://localhost:8080/usuario/listar")
      .then((res) => res.json())
      .then((data) => setUsuarios(data))
      .catch((err) => console.error("Erro ao buscar usuários:", err));
  }, []);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  const arquivos = Array.from(e.target.files || []);
  console.log("Arquivos no Modal:", arquivos);
  setAnexos((prev) => [...prev, ...arquivos]);
  e.target.value = "";
  };
  const handleRemoveAnexo = (fileToRemove: File) => {
    setAnexos((prev) => prev.filter((file) => file !== fileToRemove));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedProjectId) {
      toast.error("ID do projeto não encontrado.");
      return;
    }

    try {
      const res = await authFetch("http://localhost:8080/tarefa/cadastrar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ...tarefa, projId: selectedProjectId }),
      });

      if (!res.ok) throw new Error(await res.text());

      const tarefaCriada = await res.json();

      for (const arquivo of anexos) {
        const formData = new FormData();
        formData.append("file", arquivo);
        await authFetch(
          `http://localhost:8080/tarefa/${tarefaCriada.tarId}/upload`,
          {
            method: "POST",
            body: formData,
          }
        );
      }

      toast.success("Tarefa criada com sucesso!");
      onSuccess();
      modalContext?.closeModal();
    } catch (error) {
      console.error(error);
      toast.error("Erro ao criar a tarefa.");
    }
  };

  return (
    <div className="fixed inset-0 bg-gray-600/60 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-2xl w-full max-w-4xl flex flex-col max-h-[90vh]">
        <div className="p-8 pb-4 flex justify-between items-center">
          <h2 className="text-2xl font-bold text-gray-800">
            Adicionar Nova Tarefa
          </h2>
          <button
            onClick={() => modalContext?.closeModal()}
            className="text-gray-400 hover:text-gray-600 text-3xl"
          >
            &times;
          </button>
        </div>

        <form
          onSubmit={handleSubmit}
          className="flex flex-col flex-grow overflow-hidden"
        >
          <FormularioTarefa
            tarefa={tarefa}
            setTarefa={setTarefa}
            usuarios={usuarios}
            anexos={anexos}
            handleFileChange={handleFileChange}
            handleRemoveAnexo={handleRemoveAnexo}
          />
          <div className="p-8 pt-4 flex justify-end gap-x-4">
            <button
              type="button"
              onClick={() => modalContext?.closeModal()}
              className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700"
            >
              Criar Tarefa
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
