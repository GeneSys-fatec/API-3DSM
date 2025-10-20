import React from 'react';
import { getFileIcon } from '@/utils/fileUtils';
import type { Tarefa, Usuario, Anexo } from '@/types/types';
import { authFetch } from '@/utils/api';

interface FormularioTarefaProps {
    tarefa: Partial<Tarefa>;
    setTarefa: React.Dispatch<React.SetStateAction<Partial<Tarefa>>>;
    usuarios: Usuario[];
    anexos: File[];
    anexosExistentes?: Anexo[];
    handleFileChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    handleRemoveAnexo: (file: File) => void;
    handleRemoverAnexoExistente?: (nomeArquivo: string) => void;
    onVisualizaImagem?: (url: string) => void;
}

async function baixarAnexo(tarefaId: string, nomeArquivo: string) {
    try {
        const res = await authFetch(
            `http://localhost:8080/tarefa/${tarefaId}/anexos/${encodeURIComponent(nomeArquivo)}`
        );
        if (!res.ok) throw new Error("Erro ao baixar anexo");

        const blob = await res.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = nomeArquivo;
        a.click();
        window.URL.revokeObjectURL(url);
    } catch (err) {
        console.error(err);
        alert("Falha ao baixar o anexo.");
    }
}

export default function FormularioTarefa({
    tarefa,
    setTarefa,
    usuarios,
    anexos,
    anexosExistentes,
    handleFileChange,
    handleRemoveAnexo,
    handleRemoverAnexoExistente,
    onVisualizaImagem
}: FormularioTarefaProps) {

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setTarefa(prev => ({ ...prev, [name]: value }));
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const arquivos = Array.from(e.target.files || []);
        console.log("Arquivos selecionados:", arquivos);
        handleFileChange(e);
    };

    const handleUsuarioChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const usuId = e.target.value;
        const usuario = usuarios.find(u => u.usuId === usuId);
        if (usuario) {
            setTarefa(prev => ({
                ...prev,
                usuId: usuario.usuId,
                usuNome: usuario.usuNome
            }));
        } else {
            setTarefa(prev => ({ ...prev, usuId: '', usuNome: 'Selecione um membro' }));
        }
    };

    const totalAnexosCount = (anexosExistentes?.length || 0) + (anexos?.length || 0);

    return (
        <div className="flex-grow overflow-y-auto">
            <div className="flex flex-col gap-y-6">

                <div>
                    <label htmlFor="tarTitulo" className="py-2 block text-sm font-medium text-gray-700">Título da Tarefa</label>
                    <input
                        type="text" id="tarTitulo" name="tarTitulo"
                        value={tarefa.tarTitulo || ''} onChange={handleChange}
                        placeholder="Título da Tarefa"
                        className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
                    />
                </div>

                <div>
                    <label htmlFor="file-upload" className="cursor-pointer bg-white p-3 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 flex items-center gap-2">
                        <span>Anexar Arquivos ({totalAnexosCount})</span>
                        <input id="file-upload" name="anexo" type="file" className="sr-only" onChange={handleInputChange} multiple />
                    </label>
                </div>

                {((anexosExistentes && anexosExistentes.length > 0) || (anexos && anexos.length > 0)) && (
                    <div className="mt-2 p-4 border border-dashed rounded-md bg-gray-50 max-h-40 overflow-y-auto">
                        <h4 className="text-sm font-semibold text-gray-700 mb-2">Anexos ({totalAnexosCount}):</h4>
                        <ul className="space-y-3">
                            {anexosExistentes?.map((anexo) => {
                                const anexoUrl = `http://localhost:8080/anexos/${encodeURIComponent(anexo.arquivoNome)}`;

                                const isImage = /\.(jpe?g|png|gif|bmp|webp|svg)$/i.test(anexo.arquivoNome);
                                const isPdf = /\.pdf$/i.test(anexo.arquivoNome);

                                return (
                                    <li key={`existente-${anexo.arquivoNome}`} className="text-sm gap-1">
                                        <div className="flex items-start justify-between">
                                            <div className="flex flex-col gap-2 flex-grow min-w-0">

                                                <a
                                                    href={anexoUrl}
                                                    target="_blank"
                                                    rel="noopener noreferrer"
                                                    className="flex items-center gap-2 text-blue-600 hover:underline"
                                                    title={`Abrir ${anexo.arquivoNome} em nova aba`}
                                                >
                                                    {getFileIcon(anexo.arquivoTipo || "")}
                                                    <span className="truncate">{anexo.arquivoNome}</span>
                                                </a>

                                                {isImage && (
                                                    <div className="mt-1">
                                                        <img
                                                            src={anexoUrl}
                                                            alt={`Preview de ${anexo.arquivoNome}`}
                                                            className="max-w-full h-auto max-h-32 rounded-md border object-contain cursor-pointer hover:opacity-80 transition-opacity"
                                                            onClick={() => onVisualizaImagem && onVisualizaImagem(anexoUrl)}
                                                        />
                                                    </div>
                                                )}

                                            </div>

                                            {handleRemoverAnexoExistente && (
                                                <button
                                                    type="button"
                                                    onClick={() => handleRemoverAnexoExistente(anexo.arquivoNome)}
                                                    className="text-red-500 hover:text-red-700 ml-2 flex-shrink-0"
                                                >
                                                    &times;
                                                </button>
                                            )}
                                        </div>
                                    </li>
                                );
                            })}

                            {anexos.map((file, index) => (
                                <li key={`novo-${file.name}-${index}`} className="flex items-center justify-between text-sm">
                                    <div className="flex items-center gap-2 truncate pr-2">
                                        {getFileIcon(file.type || file.name.split('.').pop() || '')}
                                        <span className="truncate" title={file.name}>{file.name}</span>
                                    </div>
                                    <button type="button" onClick={() => handleRemoveAnexo(file)} className="text-red-500 hover:text-red-700 ml-2">&times;</button>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}

                <div>
                    <label htmlFor="tarDescricao" className="block text-sm font-medium text-gray-700">Descrição</label>
                    <textarea
                        id="tarDescricao" name="tarDescricao"
                        value={tarefa.tarDescricao || ''} onChange={handleChange}
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
                                name="usuId" value={tarefa.usuId || ''} onChange={handleUsuarioChange}
                            >
                                <option value="">Selecione um membro</option>
                                {usuarios.map(usuario => (
                                    <option key={usuario.usuId} value={usuario.usuId}>{usuario.usuNome}</option>
                                ))}
                            </select>
                        </div>

                        <div className="flex flex-col gap-2">
                            <label className="block text-sm font-medium text-gray-700">Prioridade</label>
                            <select
                                className="block w-full rounded-md border-gray-400 shadow-sm p-2"
                                name="tarPrioridade" value={tarefa.tarPrioridade || 'Média'} onChange={handleChange}
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
                                name="tarPrazo" value={tarefa.tarPrazo || ''} onChange={handleChange}
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}