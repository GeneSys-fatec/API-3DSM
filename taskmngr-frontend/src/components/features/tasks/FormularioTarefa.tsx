import React from 'react';
import { getFileIcon, formatFileSize } from '@/utils/fileUtils';
import type { Tarefa, Usuario } from '@/types/types'; 

interface FormularioTarefaProps {
    tarefa: Partial<Tarefa>; 
    setTarefa: React.Dispatch<React.SetStateAction<Partial<Tarefa>>>;
    usuarios: Usuario[];
    anexos: File[];
    handleFileChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    handleRemoveAnexo: (file: File) => void;
}

export default function FormularioTarefa({
    tarefa,
    setTarefa,
    usuarios,
    anexos,
    handleFileChange,
    handleRemoveAnexo
}: FormularioTarefaProps) {
    
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setTarefa(prev => ({ ...prev, [name]: value }));
    };

    const handleUsuarioChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const usu_id = e.target.value;
        const usuario = usuarios.find(u => u.usu_id === usu_id);
        if (usuario) {
            setTarefa(prev => ({
                ...prev,
                usu_id: usuario.usu_id,
                usu_nome: usuario.usu_nome
            }));
        } else {
            setTarefa(prev => ({...prev, usu_id: '', usu_nome: 'Selecione um membro'}));
        }
    };
    
    return (
        <div className="px-8 flex-grow overflow-y-auto">
            <div className="flex flex-col gap-y-6">
                
                <div>
                    <label htmlFor="tar_titulo" className="py-2 block text-sm font-medium text-gray-700">Título da Tarefa</label>
                    <input
                        type="text" id="tar_titulo" name="tar_titulo"
                        value={tarefa.tar_titulo || ''} onChange={handleChange}
                        placeholder="Título da Tarefa"
                        className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
                    />
                </div>

                
                <div>
                    <label htmlFor="file-upload" className="cursor-pointer bg-white p-3 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 flex items-center gap-2">
                        
                        <span>Anexar Arquivos ({anexos.length})</span>
                        <input id="file-upload" name="anexo" type="file" className="sr-only" onChange={handleFileChange} multiple />
                    </label>
                </div>
                {anexos.length > 0 && (
                    <div className="mt-2 p-4 border border-dashed rounded-md bg-gray-50 max-h-40 overflow-y-auto">
                        <h4 className="text-sm font-semibold text-gray-700 mb-2">Anexos ({anexos.length}):</h4>
                        <ul className="space-y-2">
                            {anexos.map((file, index) => (
                                <li key={index} className="flex items-center justify-between text-sm">
                                    <div className="flex items-center gap-2 truncate pr-2">
                                        {getFileIcon(file.type)}
                                        <span className="truncate" title={file.name}>{file.name}</span>
                                        <span className="text-xs text-gray-400">({formatFileSize(file.size)})</span>
                                    </div>
                                    <button type="button" onClick={() => handleRemoveAnexo(file)} className="text-red-500 hover:text-red-700 ml-2">&times;</button>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
                
                
                <div>
                    <label htmlFor="tar_descricao" className="block text-sm font-medium text-gray-700">Descrição</label>
                    <textarea
                        id="tar_descricao" name="tar_descricao"
                        value={tarefa.tar_descricao || ''} onChange={handleChange}
                        rows={3} className="mt-2 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 min-h-24 p-2"
                    ></textarea>
                </div>

                
                <div className="bg-white border mt-2 flex flex-col gap-6 p-4 rounded-md">
                    <h3 className="text-lg font-semibold text-gray-800">Detalhes</h3>
                    <hr />
                    <div className="grid grid-cols-1 gap-6">
                        
                        <div className="flex flex-col gap-2">
                            <label className="block text-sm font-medium text-gray-700">Responsável</label>
                            <select
                                className="block w-full rounded-md border-gray-300 shadow-sm p-2"
                                name="usu_id" value={tarefa.usu_id || ''} onChange={handleUsuarioChange}
                            >
                                <option value="">Selecione um membro</option>
                                {usuarios.map(usuario => (
                                    <option key={usuario.usu_id} value={usuario.usu_id}>{usuario.usu_nome}</option>
                                ))}
                            </select>
                        </div>
                        
                        <div className="flex flex-col gap-2">
                            <label className="block text-sm font-medium text-gray-700">Prioridade</label>
                            <select
                                className="block w-full rounded-md border-gray-400 shadow-sm p-2"
                                name="tar_prioridade" value={tarefa.tar_prioridade || 'Média'} onChange={handleChange}
                            >
                                <option>Alta</option>
                                <option>Média</option>
                                <option>Baixa</option>
                            </select>
                        </div>
                        
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">Data de entrega</label>
                            <input
                                type="date"
                                className="block w-full rounded-md border-gray-300 shadow-sm p-2 text-gray-500"
                                name="tar_prazo" value={tarefa.tar_prazo || ''} onChange={handleChange}
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}