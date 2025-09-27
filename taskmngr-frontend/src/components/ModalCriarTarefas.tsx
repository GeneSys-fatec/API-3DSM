import React from 'react';
import type { Tarefa } from '../pages/Home';
import { ModalContext } from '../context/ModalContext';

const getFileIcon = (mimeType: string) => {
    if (mimeType.includes('image')) {
        return <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-purple-500" viewBox="0 0 20 20" fill="currentColor"><path fillRule="evenodd" d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 12H4l4.586-4.586a2 2 0 012.828 0L16 15zm-2-6a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd" /></svg>;
    }
    if (mimeType.includes('pdf')) {
        return <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-red-600" viewBox="0 0 20 20" fill="currentColor"><path d="M5.5 13a1.5 1.5 0 01-1.5-1.5v-2a1.5 1.5 0 013 0v2a1.5 1.5 0 01-1.5 1.5zm1.336-.5a2.5 2.5 0 10-3.356 3.356l1.242 1.242a.25.25 0 00.354 0l1.242-1.242A2.5 2.5 0 006.836 12.5zm.707-8.707a1 1 0 00-1.414 0L3.5 6.5l1.414 1.414L6 6.414V11a1 1 0 102 0V6.414l1.086 1.086 1.414-1.414-2.828-2.828z" /></svg>;
    }
    if (mimeType.includes('document') || mimeType.includes('word')) {
        return <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-blue-500" viewBox="0 0 20 20" fill="currentColor"><path fillRule="evenodd" d="M4 4a2 2 0 012-2h8a2 2 0 012 2v12a2 2 0 01-2 2H6a2 2 0 01-2-2V4zm2 2a1 1 0 00-1 1v1a1 1 0 001 1h8a1 1 0 001-1V7a1 1 0 00-1-1H6zm-1 5a1 1 0 011-1h8a1 1 0 110 2H6a1 1 0 01-1-1zm1 3a1 1 0 100 2h6a1 1 0 100-2H6z" clipRule="evenodd" /></svg>;
    }
    if (mimeType.includes('spreadsheet') || mimeType.includes('excel')) {
        return <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-green-600" viewBox="0 0 20 20" fill="currentColor"><path fillRule="evenodd" d="M4 4a2 2 0 012-2h8a2 2 0 012 2v12a2 2 0 01-2 2H6a2 2 0 01-2-2V4zm2 2a1 1 0 00-1 1v1a1 1 0 001 1h8a1 1 0 001-1V7a1 1 0 00-1-1H6zm-1 5a1 1 0 011-1h8a1 1 0 110 2H6a1 1 0 01-1-1zm1 3a1 1 0 100 2h6a1 1 0 100-2H6z" clipRule="evenodd" /></svg>;
    }
    return <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-gray-500" viewBox="0 0 20 20" fill="currentColor"><path fillRule="evenodd" d="M10 2a1 1 0 00-1 1v1h2V3a1 1 0 00-1-1zm-4 4a1 1 0 000 2h8a1 1 0 100-2H6zm-1 4a1 1 0 112 0 1 1 0 01-2 0zm5 0a1 1 0 112 0 1 1 0 01-2 0zm5 0a1 1 0 112 0 1 1 0 01-2 0zM4 14a1 1 0 00-1 1v1a1 1 0 001 1h12a1 1 0 001-1v-1a1 1 0 00-1-1H4z" clipRule="evenodd" /></svg>;
};

const formatFileSize = (bytes: number) => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

type ModalProps = {
    onSuccess: () => void;
    statusInicial: string;
    selectedProjectId: string | null;
};

type ModalState = {
    tar_titulo: string;
    tar_status: string;
    tar_descricao: string;
    usu_id: string;
    usu_nome: string;
    tar_prioridade: Tarefa[`tar_prioridade`];
    tar_prazo: string;
    tar_anexos: File[];
    usuarios: Usuario[];
};

type Usuario = {
    usu_id: string;
    usu_nome: string;
    usu_email?: string;
    usu_caminhoFoto?: string;
    usu_dataCriacao?: string;
    usu_dataAtualizacao?: string;
};

export default class ModalCriarTarefas extends React.Component<ModalProps, ModalState> {
    state: ModalState = {
        tar_titulo: '',
        tar_status: 'Pendente',
        tar_descricao: '',
        usu_id: '',
        usu_nome: 'Selecione um membro',
        tar_prioridade: 'Alta',
        tar_prazo: '',
        tar_anexos: [],
        usuarios: []
    };

    componentDidMount() {
        fetch("http://localhost:8080/usuario/listar")
            .then(res => res.json())
            .then(data => this.setState({ usuarios: data }))
            .catch(err => console.error("Erro ao buscar usuários:", err));
    }

    handleChange = (
        e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
    ) => {
        const { name, value } = e.target;
        this.setState({ [name]: value } as any);
    };

    handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const files = Array.from(e.target.files || []);

        this.setState(prevState => ({
            tar_anexos: [...prevState.tar_anexos, ...files]
        }));

        e.target.value = '';
    };

    handleRemoveAnexo = (fileToRemove: File) => {
        this.setState(prevState => ({
            tar_anexos: prevState.tar_anexos.filter(file => file !== fileToRemove)
        }));
    };

    handleSubmit = async (e: React.FormEvent, closeModal: () => void) => {
    e.preventDefault();
    const { tar_titulo, tar_status, tar_descricao, usu_nome, tar_prioridade, tar_prazo, tar_anexos } = this.state;

    if (!this.props.selectedProjectId) {
        alert("Ocorreu um erro: ID do projeto não encontrado.");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/tarefa/cadastrar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                tar_titulo: tar_titulo,
                tar_descricao: tar_descricao,
                tar_status: tar_status,
                tar_prioridade: tar_prioridade,
                tar_prazo: tar_prazo,
                usu_nome: usu_nome,
                proj_id: this.props.selectedProjectId 
            })
        });

        if (!response.ok) {
            throw new Error("Erro ao criar tarefa");
        }

        const tarefaCriada = await response.json();
        const tar_id = tarefaCriada.tar_id;

        for (const arquivo of tar_anexos) {
            const formData = new FormData();
            formData.append("file", arquivo);

            const uploadRes = await fetch(`http://localhost:8080/tarefa/${tar_id}/upload`, {
                method: "POST",
                body: formData
            });

            if (!uploadRes.ok) {
                console.error(`Falha no upload do arquivo ${arquivo.name}`);
            }
        }

        this.setState({
            tar_titulo: '',
            tar_status: 'Pendente',
            tar_descricao: '',
            usu_nome: 'Selecione um membro',
            tar_prioridade: 'Alta',
            tar_prazo: '',
            tar_anexos: []
        });

        this.props.onSuccess();
        closeModal();

    } catch (err) {
        console.error(err);
        alert("Ocorreu um erro ao criar a tarefa");
    }
};
    handleCancel = (closeModal: () => void) => {
        this.setState({
            tar_titulo: '',
            tar_status: 'Pendente',
            tar_descricao: '',
            usu_nome: 'Selecione um membro',
            tar_prioridade: 'Alta',
            tar_prazo: '',
            tar_anexos: []
        });
        closeModal();
    };


    render() {
        const { tar_anexos } = this.state;

        return (
            <ModalContext.Consumer>
                {context => {

                    if (!context) {
                        console.error("Erro: Formulário de Criação de tarefa deve ser usado dentro de um ModalProvider");
                        return null;
                    }

                    return (
                        <div className="fixed inset-0 bg-gray-600/60 flex items-center justify-center z-50 p-4">
                            <div className="bg-white rounded-lg shadow-2xl w-full max-w-4xl flex flex-col max-h-[90vh]">
                                <div className="p-8 pb-4 flex-shrink-0 flex justify-between items-center">
                                    <h2 className="text-2xl font-bold text-gray-800">Adicionar Nova Tarefa</h2>
                                    <button
                                        onClick={() => this.handleCancel(context.closeModal)}
                                        className="text-gray-400 hover:text-gray-600 text-3xl transition-colors duration-200"
                                    >
                                        &times;
                                    </button>
                                </div>

                                <form onSubmit={(e) => this.handleSubmit(e, context.closeModal)} className="flex flex-col flex-grow overflow-hidden">
                                    <div className="px-8 flex-grow overflow-y-auto">
                                        <div className="flex flex-col gap-y-6">
                                            <div>
                                                <label htmlFor="titulo" className="py-2 block text-sm font-medium text-gray-700">
                                                    Título da Tarefa
                                                </label>
                                                <input
                                                    type="text"
                                                    id="tar_titulo"
                                                    name="tar_titulo"
                                                    value={this.state.tar_titulo}
                                                    onChange={this.handleChange}
                                                    placeholder="Título da Tarefa"
                                                    className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
                                                    required
                                                />
                                            </div>

                                             <div className="flex items-center gap-4">
                                                {/*<div>
                                                    <select
                                                        id="tar_status"
                                                        name="tar_status"
                                                        value={this.state.tar_status}
                                                        onChange={this.handleChange}
                                                        className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
                                                    >
                                                        <option>Pendente</option>
                                                        <option>Em Desenvolvimento</option>
                                                        <option>Concluída</option>
                                                    </select>
                                                </div> */}

                                                <div>
                                                    <label
                                                        htmlFor="file-upload"
                                                        className="cursor-pointer bg-white p-3 border border-gray-300 rounded-md shadow-sm text-sm leading-4 font-medium text-gray-700 hover:bg-gray-50 flex items-center gap-2"
                                                    >
                                                        <svg
                                                            xmlns="http://www.w3.org/2000/svg"
                                                            className="h-4 w-4 text-gray-600"
                                                            fill="none"
                                                            viewBox="0 0 24 24"
                                                            stroke="currentColor"
                                                            strokeWidth={2}
                                                        >
                                                            <path
                                                                strokeLinecap="round"
                                                                strokeLinejoin="round"
                                                                d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13"
                                                            />
                                                        </svg>
                                                        <span>Anexar Arquivos ({tar_anexos.length})</span>
                                                        <input
                                                            id="file-upload"
                                                            name="anexo"
                                                            type="file"
                                                            className="sr-only"
                                                            onChange={this.handleFileChange}
                                                            accept=".pdf, .docx, .xlsx, image/png, image/jpeg, image/gif"
                                                        />
                                                    </label>
                                                </div>
                                            </div>

                                            {tar_anexos.length > 0 && (
                                                <div className="mt-2 p-4 border border-dashed border-gray-300 rounded-md bg-gray-50 max-h-40 overflow-y-auto">
                                                    <h4 className="text-sm font-semibold text-gray-700 mb-2">Anexos Selecionados ({tar_anexos.length}):</h4>
                                                    <ul className="space-y-2">
                                                        {tar_anexos.map((file, index) => (
                                                            <li key={index} className="flex items-center justify-between text-sm text-gray-600">
                                                                <div className="flex items-center gap-2 truncate pr-2">
                                                                    {getFileIcon(file.type)}
                                                                    <span className="truncate" title={file.name}>{file.name}</span>
                                                                    <span className="text-xs text-gray-400">({formatFileSize(file.size)})</span>
                                                                </div>

                                                                <button
                                                                    type="button"
                                                                    onClick={() => this.handleRemoveAnexo(file)}
                                                                    className="text-red-500 hover:text-red-700 ml-2 flex-shrink-0"
                                                                    title="Remover anexo"
                                                                >
                                                                    &times;
                                                                </button>
                                                            </li>
                                                        ))}
                                                    </ul>
                                                </div>
                                            )}

                                            <div>
                                                <label htmlFor="descricao" className="block text-sm font-medium text-gray-700">
                                                    Descrição
                                                </label>
                                                <textarea
                                                    id="tar_descricao"
                                                    name="tar_descricao"
                                                    value={this.state.tar_descricao}
                                                    onChange={this.handleChange}
                                                    rows={3}
                                                    className="mt-2 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 min-h-24 p-2"
                                                ></textarea>
                                            </div>

                                            <div className="bg-white border mt-2 flex flex-col gap-6 p-4 rounded-md">
                                                <h3 className="text-lg font-semibold text-gray-800">Detalhes</h3>
                                                <hr />
                                                <div className="grid grid-cols-1 gap-6">
                                                    <div className="flex flex-col gap-2">
                                                        <label className="block text-sm font-medium text-gray-700">Responsável</label>
                                                        <select
                                                            className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
                                                            name="usu_id"
                                                            value={this.state.usu_id}
                                                            onChange={
                                                            (e) => {
                                                                const usu_id = e.target.value;
                                                                const usuario = this.state.usuarios.find(u => u.usu_id === usu_id);
                                                                if (usuario) {
                                                                    this.setState ({
                                                                        usu_id: usuario.usu_id,
                                                                        usu_nome: usuario.usu_nome
                                                                    });
                                                                }
                                                            }}
                                                        >
                                                            <option value="">Selecione um membro</option>
                                                            {this.state.usuarios.map(usuario => (
                                                                <option key={usuario.usu_id} value={usuario.usu_id}>
                                                                    {usuario.usu_nome}
                                                                </option>
                                                            ))}
                                                        </select>
                                                    </div>

                                                    <div className="flex flex-col gap-2">
                                                        <label className="block text-sm font-medium text-gray-700">Prioridade</label>
                                                        <select
                                                            className="block w-full rounded-md border-gray-400 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
                                                            name="tar_prioridade"
                                                            value={this.state.tar_prioridade}
                                                            onChange={this.handleChange}
                                                        >
                                                            <option>Alta</option>
                                                            <option>Média</option>
                                                            <option>Baixa</option>
                                                        </select>
                                                    </div>

                                                    <div>
                                                        <label className="block text-sm font-medium text-gray-700 mb-1">
                                                            Data de entrega
                                                        </label>
                                                        <input
                                                            type="date"
                                                            aria-label="Planned Start"
                                                            className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2 text-gray-500"
                                                            name="tar_prazo"
                                                            value={this.state.tar_prazo}
                                                            onChange={this.handleChange}
                                                        />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="p-8 pt-4 flex justify-end gap-x-4 flex-shrink-0">
                                        <button
                                            type="button"
                                            onClick={() => this.handleCancel(context.closeModal)}
                                            className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 transition-colors duration-200"
                                        >
                                            Cancelar
                                        </button>

                                        <button
                                            type="submit"
                                            className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors duration-200"
                                        >
                                            Criar
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    );
                }}
            </ModalContext.Consumer>
        );
    }
}