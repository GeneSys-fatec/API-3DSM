import React from 'react';
import CardNotificacao from './CardNotificacao';
import { Notificacao } from '@/types/types';

interface ModalNotificacaoProps {
    isOpen: boolean;
    onClose: () => void;
}

const mockNotificacoes: Notificacao[] = [
    {
        id: '1',
        tipo: 'expirado',
        tarNome: 'Tarefa Criar Dashboard',
        data: 'agora',
    },
    {
        id: '2',
        tipo: 'comentario',
        tarNome: 'Criar CRUD de equipes.',
        data: 'há 2 dias',
        usuNome: 'Usuário',
    },
    {
        id: '3',
        tipo: 'atribuido',
        tarNome: 'Tarefa Realizar deploy',
        data: 'há 28 dias',
    },
];

const ModalNotificacoes: React.FC<ModalNotificacaoProps> = ({ isOpen, onClose }) => {
    if (!isOpen) {
        return null;
    }

    return (
        <div className="fixed z-50 top-14 right-4 w-[90%] sm:w-80 md:w-96 lg:w-[24rem] max-h-[calc(100vh-2rem)] // Mantém o modal limitado à tela">
            
            <div className="bg-white rounded-lg shadow-xl ring-1 ring-black ring-opacity-5 overflow-hidden h-full flex flex-col">

                <div className="flex justify-between items-center p-4 border-b border-gray-200 flex-shrink-0">
                    <h2 className="text-lg font-semibold text-gray-900">Notificações</h2>
                    <button
                        onClick={onClose}
                        className="text-gray-400 hover:text-gray-600 transition-colors"
                        aria-label="Fechar"
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                            <path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd" />
                        </svg>
                    </button>
                </div>
                <div className="max-h-96 overflow-y-auto">
                    {mockNotificacoes.map((notificacao) => (
                        <CardNotificacao key={notificacao.id} notificacao={notificacao} />
                    ))}
                </div>
            </div>
        </div>
    );
};

export default ModalNotificacoes;