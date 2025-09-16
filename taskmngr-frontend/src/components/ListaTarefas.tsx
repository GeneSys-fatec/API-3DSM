import React from "react";
import ModalCriarTarefas from "./ModalCriarTarefas";
import ModalEditarTarefa from "./ModalEditarTarefas";

export type Tarefa = {
    id: number;
    titulo: string;
    status: string;
    responsavel: string;
    entrega: string;
    prioridade: string;
    descricao: string;
    anexo: any;
};

type ListaTarefasState = {
    tarefas: Tarefa[];
    isModalCriarOpen: boolean;
    isModalEditarOpen: boolean;
    tarefaParaEditar: Tarefa | null;
};

export default class ListaTarefas extends React.Component<{}, ListaTarefasState> {
    state: ListaTarefasState = {
        tarefas: [
            {
                id: 1,
                titulo: "Fazer relatório",
                status: "Pendente",
                responsavel: "Gabriel Medeiros",
                entrega: "2025-09-20",
                prioridade: "Alta",
                descricao: "Descrição do relatório",
                anexo: null,
            },
            {
                id: 2,
                titulo: "Revisar código",
                status: "Pendente",
                responsavel: "Gabriel Medeiros",
                entrega: "2025-09-20",
                prioridade: "Alta",
                descricao: "Revisão do código da nova feature.",
                anexo: null,
            }
        ],
        isModalCriarOpen: false,
        isModalEditarOpen: false,
        tarefaParaEditar: null,
    };

    openModalCriar = () => {
        this.setState({ isModalCriarOpen: true });
    };

    closeModalCriar = () => {
        this.setState({ isModalCriarOpen: false });
    };

    closeModalEditar = () => {
        this.setState({
            isModalEditarOpen: false,
            tarefaParaEditar: null,
        });
    };

    adicionarTarefa = (novaTarefa: Omit<Tarefa, 'id'>) => {
    this.setState((prevState) => {
        const ultimoId = prevState.tarefas.reduce((maxId, tarefa) => {
            return tarefa.id > maxId ? tarefa.id : maxId;
        }, 0);

        const novoId = ultimoId + 1;

        const tarefaCompleta: Tarefa = {
            ...novaTarefa,
            id: novoId,
        };

        return {
            tarefas: [...prevState.tarefas, tarefaCompleta],
            isModalCriarOpen: false,
        };
    });
};

    excluirTarefa = (idParaExcluir: number) => {
        this.setState(prevState => ({
            // Usamos .filter() para criar um novo array sem o item com o id correspondente
            tarefas: prevState.tarefas.filter(tarefa => tarefa.id !== idParaExcluir),
        }));
    };
    


render() {
    return (
        <>
            <div className="bg-white p-4 rounded-lg shadow-md overflow-x-auto gap-4 ">
            
                <div className="grid grid-cols-9 gap-4 py-3 px-2 text-xs font-semibold text-gray-500 border-b">
                    <div className="col-span-1">ID</div>
                    <div className="col-span-2"><i className="fa-solid fa-bars-staggered mr-2" />Título</div>
                    <div className="col-span-1"><i className="fa-solid fa-arrow-right mr-2" />Status</div>
                    <div className="col-span-1"><i className="fa-solid fa-user mr-2" />Responsável</div>
                    <div className="col-span-1"><i className="fa-solid fa-tag mr-2" />Entrega</div>
                    <div className="col-span-1"><i className="fa-solid fa-arrow-up mr-2" />Prioridade</div>
                    <div className="col-span-1 text-center"><i className="fa-solid fa-pencil mr-2" />Editar</div>
                    <div className="col-span-1 text-center"><i className="fa-solid fa-trash mr-2" />Excluir</div>
                </div>

                <div>
                    {this.state.tarefas.map((tarefa) => (
                        <div key={tarefa.id} className="grid grid-cols-9 gap-4 p-3 items-center hover:bg-gray-50 transition-colors duration-150">
                            <div className="col-span-1 text-sm font-medium text-gray-800">{tarefa.id}</div>
                            <div className="col-span-2 text-sm text-gray-800">{tarefa.titulo}</div>
                            <div className="col-span-1">
                                <span className="px-2 py-1 text-xs font-bold rounded-md uppercase bg-red-100 text-red-700">{tarefa.status}</span>
                            </div>
                            <div className="col-span-1 text-sm text-gray-800">{tarefa.responsavel}</div>
                            <div className="col-span-1 text-sm text-gray-600">{tarefa.entrega}</div>
                            <div className="col-span-1">
                                <span className="px-2 py-1 text-xs font-bold rounded-md uppercase bg-red-100 text-red-700">{tarefa.prioridade}</span>
                            </div>
                            <div className="col-span-1 flex justify-center">
                                <button className="text-xs font-semibold bg-gray-200 text-gray-700 px-3 py-1 rounded-md hover:bg-gray-300 transition-colors">
                                    Editar
                                </button>
                            </div>
                            <div className="col-span-1 flex justify-center">
                                <button className="text-xs font-semibold bg-gray-200 text-gray-700 px-3 py-1 rounded-md hover:bg-gray-300 transition-colors"
                                 onClick={() => this.excluirTarefa(tarefa.id)}
                                >
                                    Excluir
                                </button>
                            </div>
                        </div>
                    ))}
                </div>

                <button
                    onClick={this.openModalCriar}
                    className="text-sm font-semibold text-blue-600 hover:text-blue-800 pt-4"
                >
                    <i className="fa-solid fa-plus mr-2"></i>Adicionar Nova Tarefa
                </button>
            </div>

            <ModalCriarTarefas isOpen={this.state.isModalCriarOpen} onClose={this.closeModalCriar} onAdicionarTarefa={this.adicionarTarefa} />

            {/* Lógica do Modal de Edição */}
            {/* this.state.isModalEditarOpen && (
                <ModalEditarTarefa isOpen={this.state.isModalEditarOpen} onClose={this.closeModalEditar}/>
            ) */}
        </>
    );
}
}