import React from "react";
import { useModal } from "../context/ModalContext";

interface PerfilProps {
    nome: string | null;
    onLogout: () => void;
}

export const ModalPerfil: React.FC<PerfilProps> = ({ nome, onLogout }) => {
    const { closeModal } = useModal();
    if (!nome) return null;
    const inicialUsuario = nome?.charAt(0)?.toUpperCase() || "?";  
    return (
        <div className="fixed right-12 top-12 w-48 flex flex-col items-center justify-center gap-2 py-4 bg-white rounded shadow-md">
            <div className="w-12 h-12 bg-gray-300 rounded-full flex items-center justify-center">
                {inicialUsuario}
            </div>
            <div className="text-lg font-medium text-gray-800">{nome || "Nome do Usuário"}</div>
            <button onClick={() => {
                    closeModal();
                    onLogout();
                }}
                className="w-full text-indigo-600 hover:text-indigo-800 font-semibold rounded cursor-pointer">
                Deslogar
            </button>
        </div>
    )
}