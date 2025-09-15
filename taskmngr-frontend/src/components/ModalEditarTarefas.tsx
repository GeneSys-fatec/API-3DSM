import React, { useState, useEffect } from 'react';

type Tarefa = {
    id: string;
    titulo: string;
    status: 'Pendente' | 'Em Desenvolvimento' | 'Concluída';
    descricao: string;
    responsavel: string;
    prioridade: 'Alta' | 'Média' | 'Baixa';
    dataEntrega: string;
    anexo: string | null;
};

type ModalProps = {
    isOpen: boolean;
    onClose: () => void;
    tarefa: Tarefa;
    onSave: (tarefaAtualizada: Tarefa) => void;
};

export default function ModalEditarTarefas({ isOpen, onClose, tarefa, onSave }: ModalProps) {
    const [tarefaEmEdicao, setTarefaEmEdicao] = useState<Tarefa>(tarefa);
    const [campoEditando, setCampoEditando] = useState<string | null>(null);

    useEffect(() => {
        setTarefaEmEdicao(tarefa);
    }, [tarefa]);

    if (!isOpen) {
        return null;
    }

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setTarefaEmEdicao(prev => ({ ...prev, [name]: value }));
    };

    const handleSave = () => {
        onSave(tarefaEmEdicao);
        setCampoEditando(null);
    };

    const renderInput = (fieldName: keyof Tarefa, type: string = 'text') => {
        if (campoEditando === fieldName) {
            return (
                <input
                    type={type}
                    name={fieldName}
                    value={tarefaEmEdicao[fieldName] || ''}
                    onChange={handleChange}
                    onBlur={() => setCampoEditando(null)}
                    autoFocus
                    className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
                />
            );
        }
        return (
            <span
                onClick={() => setCampoEditando(fieldName)}
                className="p-2 border border-transparent rounded-md hover:bg-gray-100 cursor-pointer transition-colors duration-200"
            >
                {tarefaEmEdicao[fieldName] || 'Clique para adicionar...'}
            </span>
        );
    };

    const renderTextarea = (fieldName: keyof Tarefa) => {
        if (campoEditando === fieldName) {
            return (
                <textarea
                    name={fieldName}
                    value={tarefaEmEdicao[fieldName] || ''}
                    onChange={handleChange}
                    onBlur={() => setCampoEditando(null)}
                    autoFocus
                    rows={3}
                    className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 min-h-24 p-2"
                />
            );
        }
        return (
            <span
                onClick={() => setCampoEditando(fieldName)}
                className="p-2 block border border-transparent rounded-md hover:bg-gray-100 cursor-pointer transition-colors duration-200 whitespace-pre-wrap"
            >
                {tarefaEmEdicao[fieldName] || 'Clique para adicionar uma descrição...'}
            </span>
        );
    };

    const renderSelect = (fieldName: keyof Tarefa, options: string[]) => {
        if (campoEditando === fieldName) {
            return (
                <select
                    name={fieldName}
                    value={tarefaEmEdicao[fieldName] ?? ''}
                    onChange={handleChange}
                    onBlur={() => setCampoEditando(null)}
                    autoFocus
                    className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
                >
                    {options.map(option => (
                        <option key={option} value={option}>{option}</option>
                    ))}
                </select>
            );
        }
        return (
            <span
                onClick={() => setCampoEditando(fieldName)}
                className="p-2 border border-transparent rounded-md hover:bg-gray-100 cursor-pointer transition-colors duration-200"
            >
                {tarefaEmEdicao[fieldName]}
            </span>
        );
    };

    return (
        <div className="fixed inset-0 bg-gray-600/60 flex items-center justify-center z-50 p-4">
            <div className="bg-white rounded-lg shadow-2xl w-full max-w-xl p-8 flex flex-col gap-4 **max-h-[90vh] overflow-y-auto**">
                <div className="flex justify-between items-center mb-4">
                    <h2 className="text-2xl font-bold text-gray-800">Editar Tarefa</h2>
                    <button onClick={onClose} className="text-gray-400 hover:text-gray-600 text-3xl transition-colors duration-200">
                        &times;
                    </button>
                </div>

                <div className='flex flex-col gap-y-6'>
                    <div>
                        <label className="py-2 block text-sm font-medium text-gray-700">Título da Tarefa</label>
                        {renderInput('titulo')}
                    </div>

                    <div className="flex items-center gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-700">Status</label>
                            {renderSelect('status', ['Pendente', 'Em Desenvolvimento', 'Concluída'])}
                        </div>
                        {/* ... outros campos como 'Anexo' */}
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Descrição</label>
                        {renderTextarea('descricao')}
                    </div>

                    <div className="bg-white border mt-2 flex flex-col gap-6 p-4">
                        <h3 className="text-lg font-semibold text-gray-800">Detalhes</h3>
                        <hr />
                        <div className="grid grid-cols-1 gap-6">
                            <div className="flex flex-col gap-2">
                                <label className="block text-sm font-medium text-gray-700">Responsável</label>
                                {renderSelect('responsavel', ['Selecione um membro', 'Matheus', 'Ana Júlia'])}
                            </div>
                            <div className="flex flex-col gap-2">
                                <label className="block text-sm font-medium text-gray-700">Prioridade</label>
                                {renderSelect('prioridade', ['Alta', 'Média', 'Baixa'])}
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Data de entrega</label>
                                {renderInput('dataEntrega', 'date')}
                            </div>
                        </div>
                    </div>

                    <div className="flex justify-end mt-8 gap-x-9">
                        <button
                            type="button"
                            onClick={onClose}
                            className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 transition-colors duration-200"
                        >
                            Cancelar
                        </button>
                        <button
                            type="button"
                            onClick={handleSave}
                            className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors duration-200"
                        >
                            Salvar
                        </button>
                    </div>
                    <div className="bg-white p-6 rounded-lg shadow-md max-w-lg mx-auto mt-10">
                        <h3 className="text-xl font-semibold text-gray-800 mb-4">Deixe seu comentário</h3>
                        <form action="#" method="POST">
                            <div className="mb-4">
                                <label className="block text-gray-700 font-medium mb-2">Comentário</label>
                                <textarea id="comment" name="comment" className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200" placeholder="Digite seu comentário aqui..."></textarea>
                            </div>
                            <div className="text-right">
                                <button type="submit" className="bg-blue-600 text-white font-bold py-2 px-6 rounded-md hover:bg-blue-700 transition duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50">
                                    Enviar
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}