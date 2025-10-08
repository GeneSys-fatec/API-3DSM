import React, { useState, useEffect, useRef } from "react";
import { toast } from "react-toastify";
import { authFetch } from "@/utils/api";

interface ModalCriarEquipeProps {
  isOpen: boolean;
  onClose: () => void;
}

export default function ModalCriarEquipe({
  isOpen,
  onClose,
}: ModalCriarEquipeProps) {
  const modalRef = useRef<HTMLDivElement>(null);
  const [teamName, setTeamName] = useState("");
  const [description, setDescription] = useState("");
  const [newMemberEmail, setNewMemberEmail] = useState("");
  const [addedMembers, setAddedMembers] = useState<string[]>([]);

  useEffect(() => {
    const handleOutsideClick = (e: MouseEvent) => {
      if (modalRef.current && !modalRef.current.contains(e.target as Node)) {
        onClose();
      }
    };
    if (isOpen) {
      document.addEventListener("mousedown", handleOutsideClick);
    }
    return () => document.removeEventListener("mousedown", handleOutsideClick);
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  const handleAddMember = async (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key !== "Enter") return;
    e.preventDefault()

    const email = newMemberEmail.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(email)) {
      toast.error("Por favor, insira um email válido.")
      return;
    }

    if (addedMembers.includes(email)) {
      toast.warn("Este membro já foi adicionado.")
      return;
    }

    if (addedMembers.length >= 10) {
      toast.error("A equipe atingiu o limite máximo de 10 membros.")
      return;
    }

    try {
      const response = await authFetch(`http://localhost:8080/usuario/buscar/${email}`, {
        method: "GET",
      });

      if (!response.ok) {
        toast.error("Email inválido.")
        return;
      }

      setAddedMembers((prev) => [...prev, email]);
      setNewMemberEmail("");
    } catch (error) {
      console.error(error);
      toast.error("Erro ao verificar o email.");
    }
  };

  const handleRemoveMember = (email: string) => {
    setAddedMembers((prev) => prev.filter((m) => m !== email))
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!teamName.trim()) {
      toast.error("Por favor, insira o nome da equipe.")
      return;
    }

    const payload = {
      equNome: teamName,
      equDescricao: description,
      usuarioEmails: addedMembers,
    };

    try {
      const response = await authFetch("http://localhost:8080/equipe/cadastrar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      const data = await response.json();

      if (response.ok) {
        toast.success(`Equipe ${data.equNome} criada com sucesso!`);
        window.dispatchEvent(new CustomEvent("equipe:created"));
        setTeamName("");
        setDescription("");
        setAddedMembers([]);
        setNewMemberEmail("");
        onClose();
      } else {
        toast.error(data);
      }
    } catch (error) {
      console.error(error);
      toast.error("Erro ao criar equipe.");
    }
  };

  return (
    <div className="fixed inset-0 bg-gray-600/60 flex items-center justify-center z-50 p-4">
      <div ref={modalRef} className="bg-white rounded-lg shadow-2xl w-full max-w-4xl flex flex-col max-h-[90vh] overflow-y-auto">
        <div className="p-8 pb-4 flex justify-between">
          <h2 className="text-2xl font-bold text-gray-800">
            Criar Nova Equipe
          </h2>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600 text-3xl">
            &times;
          </button>
        </div>

        <form onSubmit={handleSubmit} className="flex flex-col flex-grow overflow-hidden">
          <div className="px-8 flex-grow overflow-y-auto">
            <div className="flex flex-col gap-y-6 text-left">
              <p className="flex justify-center text-sm text-gray-500 mb-8">
                Organize sua equipe convidando membros para colaborar em projetos.
              </p>

              <div>
                <label htmlFor="teamName" className="block text-sm font-medium text-gray-800 mb-1">
                  Nome da Equipe
                </label>
                <input
                  type="text"
                  id="teamName"
                  name="teamName"
                  value={teamName}
                  placeholder="Nome da Equipe"
                  onKeyDown={handleAddMember}
                  onChange={(e) => setTeamName(e.target.value)}
                  className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-3"
                />
              </div>

              <div>
                <label htmlFor="newMemberEmail" className="block text-sm font-medium text-gray-800 mb-1">
                  Adicionar Membros
                </label>
                <input
                  type="email"
                  id="newMemberEmail"
                  name="newMemberEmail"
                  value={newMemberEmail}
                  onChange={(e) => setNewMemberEmail(e.target.value)}
                  onKeyDown={handleAddMember}
                  placeholder="Digite o email e pressione Enter"
                  className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-3"
                />
              </div>

              {addedMembers.length > 0 && (
                <div className="max-h-40 overflow-y-auto pr-2">
                  <h3 className="block text-sm font-medium text-gray-600 mb-2">
                    Membros Adicionados:
                  </h3>
                  <div className="flex flex-col gap-2">
                    {addedMembers.map((email) => (
                      <div
                        key={email}
                        className="flex justify-between items-center p-3 rounded-xl text-sm"
                        style={{ background: "linear-gradient(90deg, #e0f2fe 0%, #c3dafe 100%)", border: "1px solid #bfdbfe", }}
                      >
                        <div className="flex items-center gap-2 text-gray-900">
                          <div className="w-4 h-4 rounded-full" style={{ background: "radial-gradient(circle, #3b82f6 0%, #1d4ed8 100%)", }}></div>
                          {email}
                        </div>
                        <button type="button" onClick={() => handleRemoveMember(email)} className="text-gray-500 hover:text-gray-800 text-2xl ml-2 leading-none">
                          &times;
                        </button>
                      </div>
                    ))}
                  </div>
                </div>
              )}

              <div>
                <label htmlFor="description" className="block text-sm font-medium text-gray-800 mb-1">
                  Descrição <span className="text-gray-500">(Opcional)</span>
                </label>
                <textarea
                  id="description"
                  name="description"
                  rows={3}
                  value={description}
                  placeholder="Adicione uma descrição para esta equipe..."
                  onChange={(e) => setDescription(e.target.value)}
                  className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-3"
                />
              </div>
            </div>
          </div>

          <div className="p-8 pt-4 flex justify-end gap-x-4">
            <button type="button" onClick={onClose} className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300">
              Cancelar
            </button>
            <button type="submit" className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700">
              Criar Equipe
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
