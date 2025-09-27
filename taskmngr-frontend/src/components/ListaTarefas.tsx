import React from "react";
import ModalCriarTarefas from "./ModalCriarTarefas";
import ModalEditarTarefas from "./ModalEditarTarefas";
import { ModalContext } from "../context/ModalContext";
import type { Tarefa } from "../pages/Home";

export type Responsavel = {
    nome: string;
    fotoURL?: string;
};


export type ListaTarefasState = {
    tarefas: Tarefa[];
    loading: boolean;
    error: string | null;
    tarefaParaExcluir: string | null;
};

export default class ListaTarefas extends React.Component<object, ListaTarefasState> {
    static contextType = ModalContext;
    declare context: React.ContextType<typeof ModalContext>;

    state: ListaTarefasState = {
        tarefas: [],
        loading: false,
        error: null,
        tarefaParaExcluir: null,
    };


    componentDidMount() {
        this.carregarTarefas();
    }

    carregarTarefas = async () => {
        this.setState({ loading: true, error: null });
        try {
            const response = await fetch("http://localhost:8080/tarefa/listar");
            if (!response.ok) {
                const errorBody = await response.text();
                throw new Error(`Erro na requisição: ${response.statusText}. Resposta do servidor: ${errorBody}`);
            }
            const data = await response.json();

            // eslint-disable-next-line @typescript-eslint/no-explicit-any
            const tarefasConvertidas: Tarefa[] = data.map((item: any) => ({
                tar_id: item.tar_id,
                tar_titulo: item.tar_titulo,
                tar_status: item.tar_status,
                usu_nome: item.usu_nome,
                tar_prazo: item.tar_prazo ?? "-",
                tar_prioridade: item.tar_prioridade,
                tar_descricao: item.tar_descricao,
                tar_anexos: [], // por enquanto é null, pq n tem nd funcionando
            }));

            this.setState({ tarefas: tarefasConvertidas, loading: false });
        } catch (error) {
            console.error("Falha ao carregar tarefas:", error);
            this.setState({
                error: "Não foi possível carregar as tarefas.",
                loading: false,
            });
        }
    };

    adicionarTarefa = async (novaTarefa: Omit<Tarefa, "tar_id">) => {
        try {
            const response = await fetch("http://localhost:8080/tarefa/cadastrar", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    tar_titulo: novaTarefa.tar_titulo,
                    tar_descricao: novaTarefa.tar_descricao,
                    tar_status: novaTarefa.tar_status,
                    tar_prioridade: novaTarefa.tar_prioridade,
                    tar_prazo: novaTarefa.tar_prazo && novaTarefa.tar_prazo !== "-" ? novaTarefa.tar_prazo : null,
                    tar_anexos: [],
                    usu_nome: novaTarefa.usu_nome,
                    proj_nome: "API-3sem",
                }),
            });

            if (!response.ok) {
                const errorBody = await response.text();
                throw new Error(`Erro ao cadastrar tarefa: ${response.statusText}. Resposta do servidor: ${errorBody}`);
            }

            await this.carregarTarefas();


        } catch (error) {
            console.error("Falha ao adicionar tarefa:", error);
            this.setState({ error: "Não foi possível adicionar a tarefa." });
        }
    };

    editarTarefa = async (tarefaAtualizada: Tarefa) => {
        try {
            const response = await fetch(`http://localhost:8080/tarefa/atualizar/${tarefaAtualizada.tar_id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    tar_titulo: tarefaAtualizada.tar_titulo,
                    tar_descricao: tarefaAtualizada.tar_descricao,
                    tar_status: tarefaAtualizada.tar_status,
                    tar_prioridade: tarefaAtualizada.tar_prioridade,
                    tar_prazo: tarefaAtualizada.tar_prazo && tarefaAtualizada.tar_prazo !== "-" ? tarefaAtualizada.tar_prazo : null,
                    tar_anexos: [],
                    usu_nome: tarefaAtualizada.usu_nome,
                    proj_nome: "API-3sem",
                }),
            });

            if (!response.ok) {
                // Se a requisição falhar, lemos o corpo da resposta para ter mais detalhes.
                const errorBody = await response.text();
                throw new Error(`Erro ao atualizar tarefa: ${response.statusText}. Resposta do servidor: ${errorBody}`);
            }

            await this.carregarTarefas();

        } catch (error) {
            console.error("Falha ao editar tarefa:", error);
            this.setState({ error: "Não foi possível editar a tarefa." });
        }
    };

    // pro modal
    confirmarExclusao = (idParaExcluir: string) => {
        this.setState({ tarefaParaExcluir: idParaExcluir });
    };

    cancelarExclusao = () => {
        this.setState({ tarefaParaExcluir: null });
    };

    excluirTarefa = async () => {
        const { tarefaParaExcluir } = this.state;
        if (!tarefaParaExcluir) return;

        try {
            const response = await fetch(`http://localhost:8080/tarefa/apagar/${tarefaParaExcluir}`, {
                method: "DELETE",
            });

            if (!response.ok) {
                const errorBody = await response.text();
                throw new Error(`Erro ao excluir tarefa: ${response.statusText}. Resposta do servidor: ${errorBody}`);
            }
            this.setState(prevState => ({
                tarefas: prevState.tarefas.filter(tarefa => tarefa.tar_id !== tarefaParaExcluir),
                tarefaParaExcluir: null,
            }));
        } catch (error) {
            console.error("Falha ao excluir tarefa:", error);
            this.setState({ error: "Não foi possível excluir a tarefa.", tarefaParaExcluir: null });
        }
    };

    abrirModalCriacao = () => {
        if (this.context) {
            this.context.openModal(
                <ModalCriarTarefas onAdicionarTarefa={this.adicionarTarefa}
                    statusInicial="Pendente"
                />
            );
        } else {
            console.error("ModalContext não está disponível. Verifique se o componente está dentro de um ModalProvider.");
        }
    };

    abrirModalEdicao = (tarefa: Tarefa) => {
        if (this.context) {
            this.context.openModal(
                <ModalEditarTarefas tarefa={tarefa} onSave={this.editarTarefa} />
            );
        } else {
            console.error("ModalContext não está disponível. Verifique se o componente está dentro de um ModalProvider.");
        }
    };

    getStatusClass = (status: string | null | undefined) => {
        if (!status) {
            return 'bg-gray-100 text-gray-800';
        }

        switch (status.toLowerCase()) {
            case 'pendente':
                return 'bg-orange-100 text-orange-400';
            case 'em desenvolvimento':
                return 'bg-blue-100 text-blue-400';
            case 'concluída':
                return 'bg-green-100 text-green-500';
            default:
                return 'bg-gray-100 text-gray-800';
        }
    };

    getPrioridadeClass = (prioridade: string | null | undefined) => {
        if (!prioridade) {
            return 'bg-gray-100 text-gray-800';
        }

        switch (prioridade.toLowerCase()) {
            case 'alta':
                return 'bg-red-100 text-red-700';
            case 'média':
                return 'bg-yellow-100 text-yellow-700';
            case 'baixa':
                return 'bg-blue-100 text-blue-700';
            default:
                return 'bg-gray-100 text-gray-800';
        }
    };

    render() {
        const { tarefas, loading, error, tarefaParaExcluir } = this.state;

        if (loading) {
            return <div className="p-4">Carregando tarefas...</div>;
        }

        if (error) {
            return <div className="p-4 text-red-600">{error}</div>;
        }

        return (
            <>
                <div className="bg-white md:p-4 md:rounded-lg md:shadow-md overflow-hidden relative min-w-full">

                    <div className="hidden md:block">
                        <div className="max-h-[500px] overflow-y-auto">

                            <div className="grid grid-cols-12 gap-3 py-3 px-2 text-xs font-semibold text-gray-500 border-b sticky top-0 bg-white">
                                <div className="col-span-1 text-center">ID</div>
                                <div className="col-span-3"><i className="fa-solid fa-bars-staggered pr-2" />Título</div>
                                <div className="col-span-2 text-center"><i className="fa-solid fa-user pr-1" />Responsável</div>
                                <div className="col-span-2 text-center"><i className="fa-solid fa-tag pr-1" />Entrega</div>
                                <div className="col-span-1 text-center"><i className="fa-solid fa-arrow-up pr-1" />Prioridade</div>
                                <div className="col-span-1 text-center"><i className="fa-solid fa-arrow-right pr-1" />Status</div>
                                <div className="col-span-1 text-center"><i className="fa-solid fa-pencil pr-1" />Editar</div>
                                <div className="col-span-1"><i className="fa-solid fa-trash pr-1" />Excluir</div>
                            </div>

                            <div>
                                {tarefas.map((tarefa, index) => (
                                    <div key={tarefa.tar_id} className="grid grid-cols-12 gap-3 p-3 items-center hover:bg-gray-100 transition-all duration-100 ease-in-out">
                                        <div className="col-span-1 text-sm font-medium text-gray-800 text-center" title={tarefa.tar_id}>{index + 1}</div>
                                        <div className="col-span-3 text-sm text-gray-800 truncate" title={tarefa.tar_titulo}>
                                            <span className="truncate">{tarefa.tar_titulo}</span>
                                        </div>
                                        <div className="col-span-2 text-sm text-gray-800 text-center">{tarefa.usu_nome}</div>
                                        <div className="col-span-2 flex justify-center">
                                            <span className="text-xs font-semibold bg-gray-200 text-gray-700 px-2 py-1 rounded-md hover:bg-gray-300 transition-colors">{tarefa.tar_prazo}</span>
                                        </div>
                                        <div className="col-span-1 flex justify-center">
                                            <span className={`px-2 py-1 text-xs font-bold rounded-md uppercase ${this.getPrioridadeClass(tarefa.tar_prioridade)}`}>{tarefa.tar_prioridade}</span>
                                        </div>
                                        <div className="col-span-1 overflow-hidden flex justify-center">
                                            <span
                                                className={`px-2 py-1 text-xs font-bold rounded-md uppercase truncate ${this.getStatusClass(tarefa.tar_status)}`}
                                                title={tarefa.tar_status}
                                            >
                                                {tarefa.tar_status}
                                            </span>
                                        </div>
                                        <div className="col-span-1 flex justify-center">
                                            <button
                                                onClick={() => this.abrirModalEdicao(tarefa)}
                                                className="text-xs font-semibold bg-gray-200 text-gray-700 px-2 py-1 rounded-md hover:bg-gray-300 transition-colors"
                                            >
                                                Editar
                                            </button>
                                        </div>
                                        <div className="col-span-1 flex justify-center">
                                            <button
                                                className="text-xs font-semibold bg-gray-200 text-gray-700 px-2 py-1 rounded-md hover:bg-gray-300 transition-colors"
                                                onClick={() => this.confirmarExclusao(tarefa.tar_id)}
                                            >
                                                Excluir
                                            </button>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>

                        <button
                            onClick={this.abrirModalCriacao}
                            className="text-sm font-semibold text-blue-600 hover:text-blue-800 pt-4 cursor-pointer"
                        >
                            <i className="fa-solid fa-plus mr-2"></i>Adicionar Nova Tarefa
                        </button>
                    </div>

                    <div className="block md:hidden relative w-screen -ml-1">
                        <div className="max-h-[calc(100vh-280px)] overflow-y-auto pb-20 px-1 -mt-2">
                            <div className="space-y-4">
                                {tarefas.map((tarefa, index) => (
                                    <React.Fragment key={tarefa.tar_id}>
                                        <div className="bg-white rounded-lg p-5 hover:bg-gray-200 active:bg-gray-200 transition-all duration-300 ease-in-out">

                                            <div className="flex justify-between items-start mb-2">
                                                <div className="flex-1 min-w-0 mr-3">
                                                    <h3 className="font-semibold text-gray-800 text-lg break-words mb-2">
                                                        <span className="text-gray-500">#{index + 1}</span> - {tarefa.tar_titulo}
                                                    </h3>
                                                    <div className="flex gap-2">
                                                        <span className={`px-3 py-1 text-sm font-bold rounded-full uppercase ${this.getPrioridadeClass(tarefa.tar_prioridade)}`}>
                                                            {tarefa.tar_prioridade}
                                                        </span>
                                                        <span className={`px-3 py-1 text-sm font-bold rounded-full uppercase ${this.getStatusClass(tarefa.tar_status)}`}>
                                                            {tarefa.tar_status}
                                                        </span>
                                                    </div>
                                                </div>
                                                <div className="flex gap-1 flex-shrink-0">
                                                    <button
                                                        onClick={() => this.abrirModalEdicao(tarefa)}
                                                        className="text-blue-600 hover:text-blue-800 p-2"
                                                        title="Editar"
                                                    >
                                                        <i className="fa-solid fa-pencil text-lg"></i>
                                                    </button>
                                                    <button
                                                        className="text-red-600 hover:text-red-800 p-2"
                                                        onClick={() => this.confirmarExclusao(tarefa.tar_id)}
                                                        title="Excluir"
                                                    >
                                                        <i className="fa-solid fa-trash text-lg"></i>
                                                    </button>
                                                </div>
                                            </div>

                                            <div className="space-y-2 text-base">
                                                <div className="flex items-center justify-between gap-3">
                                                    <div className="flex items-center min-w-0 flex-1">
                                                        <span className="text-gray-600 font-medium flex-shrink-0">Responsável:</span>
                                                        <span className="text-gray-800 font-medium pl-2 break-words">{tarefa.usu_nome}</span>
                                                    </div>
                                                </div>
                                                <div className="flex items-center justify-between gap-3">
                                                    <div className="flex items-center min-w-0 flex-1">
                                                        <span className="text-gray-600 font-medium flex-shrink-0">Entrega:</span>
                                                        <span className="text-gray-800 font-medium pl-2">{tarefa.tar_prazo}</span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        {index < tarefas.length - 1 && (
                                            <hr className="border-gray-400 mx-4" />
                                        )}
                                    </React.Fragment>
                                ))}
                            </div>
                        </div>

                        <div className="fixed bottom-20 left-4 right-4 z-50">
                            <div className="bg-white border-t border-gray-200 p-3 rounded-lg shadow-lg">
                                <button
                                    onClick={this.abrirModalCriacao}
                                    className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-4 px-4 rounded-lg transition-colors flex items-center justify-center text-lg"
                                    title="Adicionar Nova Tarefa"
                                >
                                    <i className="fa-solid fa-plus mr-2 text-lg"></i>Adicionar Nova Tarefa
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                {/*modal pra confirmar se quer excluir*/}
                {tarefaParaExcluir && (
                    <div className="fixed inset-0 bg-black/60 flex items-center justify-center z-50 p-4">
                        <div className="bg-white rounded-lg p-6 max-w-md w-full shadow-xl">
                            <div className="text-center">
                                <i className="fa-solid fa-triangle-exclamation text-red-500 text-4xl mb-4"></i>
                                <h3 className="text-lg font-semibold text-gray-900 mb-4">Tem certeza?</h3>
                                <p className="text-gray-600">
                                    Esta ação não pode ser desfeita. A tarefa será excluída permanentemente.
                                </p>
                                <div className="flex gap-4 justify-center" style={{ paddingTop: '20px' }}>
                                    <button
                                        onClick={this.cancelarExclusao}
                                        className="px-6 py-3 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition-colors font-medium"
                                    >
                                        Cancelar
                                    </button>
                                    <button
                                        onClick={this.excluirTarefa}
                                        className="px-6 py-3 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors font-medium"
                                    >
                                        Excluir
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                )}
            </>
        );
    }
}