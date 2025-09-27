import React from 'react';
import { ModalContext } from '../context/ModalContext';

type ModalProps = {
    tarefa: Tarefa;
    onSave: (tarefaAtualizada: Tarefa) => void;
};

type Tarefa = {
    tar_id: string;
    tar_titulo: string;
    tar_status: string;
    usu_id: string;
    usu_nome: string;
    tar_prazo: string;
    tar_prioridade: "Alta" | "Média" | "Baixa" ;
    tar_descricao: string;
    tar_anexo?: File | null;
};

type Usuario = {
    usu_id: string;
    usu_nome: string;
    usu_email?: string;
    usu_caminhoFoto?: string;
    usu_dataCriacao?: string;
    usu_dataAtualizacao?: string;
};

type ModalState = {
    tarefaEmEdicao: Tarefa;
    novoComentario: string;
    usuarios: Usuario[];
};


export default class ModalEditarTarefas extends React.Component<ModalProps, ModalState> {
    constructor(props: ModalProps) {
        super(props);

        this.state = {
            tarefaEmEdicao: { ...props.tarefa },
            novoComentario: '',
            usuarios: []
        };
    }

    componentDidUpdate(prevProps: ModalProps) {
        if (this.props.tarefa.tar_id !== prevProps.tarefa.tar_id) {
            this.setState({ tarefaEmEdicao: { ...this.props.tarefa } });
        }
    }

    componentDidMount() {
        fetch("http://localhost:8080/usuario/listar")
        .then(res => res.json())
        .then(data => this.setState({ usuarios: data }))
        .catch(err => console.error("Erro ao buscar usuários:", err));
    }

    handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        this.setState(prevState => ({
            tarefaEmEdicao: {
                ...prevState.tarefaEmEdicao,
                [name]: value,
            },
        }));
    };

    handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            const file = e.target.files[0];
            this.setState(prevState => ({
                tarefaEmEdicao: {
                    ...prevState.tarefaEmEdicao,
                    anexo: file,
                },
            }));
        }
    };

    handleSave = (e: React.FormEvent, closeModal: () => void) => {
        e.preventDefault();
        console.log("Tarefa enviada:", this.state.tarefaEmEdicao);
        this.props.onSave(this.state.tarefaEmEdicao);
        closeModal();
    };

    handleCommentSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        console.log("Novo comentário enviado:", this.state.novoComentario);
        this.setState({ novoComentario: '' });
    };

    render() {
        return (
            <ModalContext.Consumer>
                {context => {
                    if (!context) { return null; }
                    const { closeModal } = context;
                    const { tarefaEmEdicao, novoComentario } = this.state;

                    return (
                        <div className="fixed inset-0 bg-gray-600/60 flex items-center justify-center z-50 p-4">
                            <div className="bg-white rounded-lg shadow-2xl w-full max-w-4xl flex flex-col max-h-[90vh]">

                                {/* Cabeçalho do Modal */}
                                <div className="p-8 pb-4 flex-shrink-0">
                                    <div className="flex justify-between items-center">
                                        <h2 className="text-2xl font-bold text-gray-800">Editar Tarefa</h2>
                                        <button onClick={closeModal} className="text-gray-400 hover:text-gray-600 text-3xl transition-colors duration-200">
                                            &times;
                                        </button>
                                    </div>
                                </div>

                                {/* Formulário com scroll */}
                                <form onSubmit={(e) => this.handleSave(e, closeModal)} className='flex flex-col flex-grow overflow-hidden'>
                                    <div className="px-8 flex-grow overflow-y-auto">
                                        <div className='flex flex-col gap-y-6'>
                                            <div>
                                                <label htmlFor="titulo" className="py-2 block text-sm font-medium text-gray-700">Título da Tarefa</label>
                                                <input
                                                    type="text"
                                                    id="tar_titulo"
                                                    name="tar_titulo"
                                                    value={tarefaEmEdicao.tar_titulo}
                                                    onChange={this.handleChange}
                                                    placeholder="Título da Tarefa"
                                                    className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
                                                    required
                                                />
                                            </div>
                                            <div className="flex items-center gap-4">
                                                <div>
                                                    <select
                                                        id="tar_status"
                                                        name="tar_status"
                                                        value={tarefaEmEdicao.tar_status}
                                                        onChange={this.handleChange}
                                                        className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
                                                    >
                                                        <option>Pendente</option>
                                                        <option>Em Desenvolvimento</option>
                                                        <option>Concluída</option>
                                                    </select>
                                                </div>
                                                <div>
                                                    <label htmlFor="file-upload" className="cursor-pointer bg-white p-3 border border-gray-300 rounded-md shadow-sm text-sm leading-4 font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 flex items-center gap-2">
                                                        <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                                                            <path strokeLinecap="round" strokeLinejoin="round" d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13" />
                                                        </svg>
                                                        <span>Anexo</span>
                                                        <input id="file-upload" name="file-upload" type="file" className="sr-only" onChange={this.handleFileChange} />
                                                    </label>
                                                </div>
                                            </div>
                                            <div>
                                                <label htmlFor="descricao" className="block text-sm font-medium text-gray-700">Descrição</label>
                                                <textarea
                                                    id="tar_descricao"
                                                    name="tar_descricao"
                                                    value={tarefaEmEdicao.tar_descricao}
                                                    onChange={this.handleChange}
                                                    rows={3}
                                                    className="mt-2 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 min-h-24 p-2"
                                                ></textarea>
                                            </div>

                                            {/* Seção de Detalhes */}
                                            <div className="bg-white border mt-2 flex flex-col gap-6 p-4 rounded-md">
                                                <h3 className="text-lg font-semibold text-gray-800">Detalhes</h3>
                                                <hr />
                                                <div className="grid grid-cols-1 gap-6">
                                                    <div className="flex flex-col gap-2">
                                                        <label className="block text-sm font-medium text-gray-700">Responsável</label>
                                                        <select
                                                            className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
                                                            name="usu_id"
                                                            value={tarefaEmEdicao.usu_id}
                                                            onChange={
                                                            (e) => {
                                                                const usu_id = e.target.value;
                                                                const usuario = this.state.usuarios.find(u => u.usu_id === usu_id);
                                                                if (usuario) {
                                                                    this.setState(prevState => ({
                                                                        tarefaEmEdicao: {
                                                                            ...prevState.tarefaEmEdicao,
                                                                            usu_id: usuario.usu_id,
                                                                            usu_nome: usuario.usu_nome,
                                                                        },
                                                                    }));
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
                                                            value={tarefaEmEdicao.tar_prioridade}
                                                            onChange={this.handleChange}
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
                                                            className="block w-full ..."
                                                            name="tar_prazo"
                                                            value={tarefaEmEdicao.tar_prazo}
                                                            onChange={this.handleChange}
                                                        />
                                                    </div>
                                                </div>
                                            </div>

                                            {/* Seção de Comentários */}
                                            <div className="bg-gray-50 p-6 rounded-lg mt-4">
                                                <h3 className="text-lg font-semibold text-gray-800 mb-4">Deixe seu comentário</h3>
                                                <div className="mb-4">
                                                    <label htmlFor="comment" className="block text-gray-700 font-medium mb-2 sr-only">Comentário</label>
                                                    <textarea
                                                        id="comment"
                                                        name="comment"
                                                        value={novoComentario}
                                                        onChange={(e) => this.setState({ novoComentario: e.target.value })}
                                                        className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200"
                                                        placeholder="Digite seu comentário aqui..."
                                                    ></textarea>
                                                </div>
                                                <div className="text-right">
                                                    <button
                                                        type="button"
                                                        onClick={this.handleCommentSubmit}
                                                        className="bg-blue-600 text-white font-bold py-2 px-6 rounded-md hover:bg-blue-700 transition duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
                                                    >
                                                        Enviar
                                                    </button>
                                                </div>
                                            </div>

                                        </div>
                                    </div>

                                    {/* Rodapé com Botões */}
                                    <div className="p-8 pt-4 flex justify-end gap-x-4 flex-shrink-0">
                                        <button
                                            type="button"
                                            onClick={closeModal}
                                            className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 transition-colors duration-200"
                                        >
                                            Cancelar
                                        </button>
                                        <button
                                            type="submit"
                                            className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors duration-200"
                                        >
                                            Salvar Alterações
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    )
                }}

            </ModalContext.Consumer>
        );
    }
}