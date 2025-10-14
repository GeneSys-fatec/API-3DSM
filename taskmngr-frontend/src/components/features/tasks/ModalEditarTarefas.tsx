import React, { useState, useEffect, useContext } from "react";
import { ModalContext } from "@/context/ModalContext.tsx";
import { toast } from "react-toastify";
import FormularioTarefa from "./FormularioTarefa";
import type { Tarefa, Usuario, Anexo } from "@/types/types";
import { authFetch } from "@/utils/api";
import { getFileIcon } from "@/utils/fileUtils";
import { showErrorToastFromResponse, showValidationToast } from "@/utils/errorUtils";
import { uploadTaskAttachments } from "@/utils/taskUtils";
import ListaComentarios from "./ListaComentarios";

interface ModalEditarTarefasProps {
  tarefa: Tarefa;
  onSave: () => void;
}

export default function ModalEditarTarefas({
  tarefa: tarefaInicial,
  onSave,
}: ModalEditarTarefasProps) {
  const modalContext = useContext(ModalContext);
  const [tarefa, setTarefa] = useState<Partial<Tarefa>>(tarefaInicial);
  const [novosAnexos, setNovosAnexos] = useState<File[]>([]);
  const [anexosExistentes, setAnexosExistentes] = useState<Anexo[]>([]);
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);

  useEffect(() => {
    authFetch("http://localhost:8080/usuario/listar")
      .then((res) => res.json())
      .then(setUsuarios)
      .catch((err) => console.error("Erro ao buscar usuários:", err));

    authFetch(`http://localhost:8080/tarefa/${tarefaInicial.tarId}/anexos`)
      .then((res) => res.json())
      .then(setAnexosExistentes)
      .catch((err) => console.error("Erro ao buscar anexos:", err));
  }, [tarefaInicial.tarId]);

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const novos = Array.from(e.target.files || []);
    setNovosAnexos((prev) => [...prev, ...novos]);
    e.target.value = "";
  };

  const handleRemoveNovoAnexo = (fileToRemove: File) => {
    setNovosAnexos((prev) => prev.filter((file) => file !== fileToRemove));
  };

  const handleRemoverAnexoExistente = async (nomeArquivo: string) => {
    if (!window.confirm(`Remover anexo "${nomeArquivo}"?`)) return;
    try {
      await authFetch(
        `http://localhost:8080/tarefa/${tarefa.tarId}/anexos/${encodeURIComponent(nomeArquivo)}`,
        { method: "DELETE" }
      );
      setAnexosExistentes((prev) =>
        prev.filter((a) => a.arquivoNome !== nomeArquivo)
      );
      toast.success("Anexo removido.");
    } catch (err) {
      console.error("Falha ao remover anexo:", err);
      toast.error("Erro ao remover anexo.");
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // Coleta de erros de validação (um único toast no final)
    const validationErrors: string[] = [];

    if (!tarefa.tarTitulo?.trim()) {
      validationErrors.push("O título da tarefa é obrigatório.");
    }
    if (!tarefa.usuId) {
      validationErrors.push("Selecione um responsável pela tarefa.");
    }
    if (!tarefa.tarPrazo) {
      validationErrors.push("Informe um prazo para a tarefa.");
    }

    // Removido: validação de anexos no front (limites ficam no backend)
    // if (novosAnexos.length > 0) { ... validateAttachments ... }

    if (validationErrors.length > 0) {
      showValidationToast(validationErrors, "Erros de validação");
      return;
    }

    try {
      const res = await authFetch(
        `http://localhost:8080/tarefa/atualizar/${tarefa.tarId}`,
        {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(tarefa),
        }
      );

      if (!res.ok) {
        await showErrorToastFromResponse(res, "Erro ao atualizar a tarefa");
        return;
      }

      // Upload de novos anexos (backend trata compressão e limites)
      if (novosAnexos.length > 0) {
        const ok = await uploadTaskAttachments(String(tarefa.tarId), novosAnexos);
        if (!ok) {
          // toast.error("Falha ao anexar arquivos. Após a compressão, alguns anexos permanecem acima do limite esperado.");
          return;
        }
      }

      toast.success("Tarefa atualizada com sucesso!");
      onSave();
      modalContext?.closeModal();
    } catch (error) {
      console.error("Falha ao atualizar tarefa:", error);
      toast.error("Erro ao atualizar a tarefa.");
    }
  };

  return (
    <div className="fixed inset-0 bg-gray-600/60 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-2xl w-full max-w-4xl flex flex-col max-h-[90vh]">
        <div className="p-8 pb-4 flex justify-between items-center">
          <h2 className="text-2xl font-bold text-gray-800">Editar Tarefa</h2>
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
          <div className="px-8 flex-grow overflow-y-auto">
            <FormularioTarefa
              tarefa={tarefa}
              setTarefa={setTarefa}
              usuarios={usuarios}
              anexos={novosAnexos}
              anexosExistentes={anexosExistentes} // <-- passa os existentes pra dentro do form
              handleFileChange={handleFileChange}
              handleRemoveAnexo={handleRemoveNovoAnexo}
          />
          <div className="flex-grow overflow-y-auto pt-4">
            <div className="flex flex-col gap-2">
              <h3 className="text-lg font-semibold text-gray-800">Comentários</h3>
              {tarefa.tarId && (
                  <ListaComentarios tarId={tarefa.tarId} />
              )}
            </div>
          </div>
            {anexosExistentes.length > 0 && (
              <div className="mt-4 p-3 border rounded-md bg-gray-50">
                <h4 className="font-semibold text-sm mb-2">
                  Anexos existentes
                </h4>
                <ul className="space-y-2">
                  {anexosExistentes.map((anexo) => (
                    <li
                      key={anexo.arquivoNome}
                      className="flex items-center justify-between"
                    >
                      <div className="flex items-center gap-2 truncate">
                        {getFileIcon(anexo.arquivoTipo || "")}
                        <a
                          href={`http://localhost:8080/tarefa/${tarefa.tarId
                            }/anexos/${encodeURIComponent(anexo.arquivoNome)}`}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="truncate text-blue-600 hover:underline"
                        >
                          {anexo.arquivoNome}
                        </a>
                      </div>
                      <button
                        type="button"
                        onClick={() =>
                          handleRemoverAnexoExistente(anexo.arquivoNome)
                        }
                        className="text-red-500"
                      >
                        &times;
                      </button>
                    </li>
                  ))}
                </ul>
              </div>
            )}
          </div>

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
              Salvar Alterações
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
