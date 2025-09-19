import React from "react";
import ModalCriarTarefas from "./ModalCriarTarefas";
import ModalEditarTarefas from "./ModalEditarTarefas";

export type Responsavel = {
    nome: string;
    fotoURL?: string;
};

export type Tarefa = {
    id: string;
    titulo: string;
    status: string;
    responsavel: string;
    entrega: string;
    prioridade: string;
    descricao: string;
    anexo: File | null;
};

export type ListaTarefasState = {
    tarefas: Tarefa[];
    isModalCriarOpen: boolean;
    isModalEdicaoOpen: boolean;
    tarefaParaEditar: Tarefa | null;
    loading: boolean;
    error: string | null;
    tarefaSelecionada: Tarefa | null;
};

export default class ListaTarefas extends React.Component<object, ListaTarefasState> {
    state: ListaTarefasState = {
        tarefas: [],
        isModalCriarOpen: false,
        isModalEdicaoOpen: false,
        tarefaParaEditar: null,
        loading: false,
        error: null,
        tarefaSelecionada: null,
    };


    componentDidMount() {
        this.carregarTarefas();
    }

    openModalCriar = () => {
        this.setState({ isModalCriarOpen: true });
    };

    closeModalCriar = () => {
        this.setState({ isModalCriarOpen: false });
    };



    handleOpenModalEdicao = (tarefa: Tarefa) => this.setState({ isModalEdicaoOpen: true, tarefaSelecionada: tarefa });
    handleCloseModalEdicao = () => this.setState({ isModalEdicaoOpen: false, tarefaSelecionada: null });
    handleSaveTarefa = (tarefaAtualizada: Tarefa) => {
        this.editarTarefa(tarefaAtualizada);
    };

    carregarTarefas = async () => {
        this.setState({ loading: true, error: null });
        try {
            const response = await fetch("http://localhost:8080/tarefa/listar");
            if (!response.ok) {
                const errorBody = await response.text();
                throw new Error(`Erro na requisição: ${response.statusText}. Resposta do servidor: ${errorBody}`);
            }
            const data = await response.json();

            // transformar o JSON do back no formato do front
            const tarefasConvertidas: Tarefa[] = data.map((item: any) => ({
                id: item.tar_id,
                titulo: item.tar_titulo,
                status: item.tar_status,
                responsavel: item.usu_nome,
                entrega: item.tar_prazo ?? "-", // se for null, mostra -
                prioridade: item.tar_prioridade,
                descricao: item.tar_descricao,
                anexo: null, // por enquanto é null, pq n tem nd funcionando
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

    adicionarTarefa = async (novaTarefa: Omit<Tarefa, "id">) => {
        try {
            const response = await fetch("http://localhost:8080/tarefa/cadastrar", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    tar_titulo: novaTarefa.titulo,
                    tar_descricao: novaTarefa.descricao,
                    tar_status: novaTarefa.status,
                    tar_prioridade: novaTarefa.prioridade,
                    tar_prazo: novaTarefa.entrega && novaTarefa.entrega !== "-" ? novaTarefa.entrega : null,
                    tar_anexo: null,
                    usu_nome: novaTarefa.responsavel,
                    proj_nome: "API-3sem",
                }),
            });

            if (!response.ok) {
                const errorBody = await response.text();
                throw new Error(`Erro ao cadastrar tarefa: ${response.statusText}. Resposta do servidor: ${errorBody}`);
            }

            await this.carregarTarefas();

            // fechar modal
            this.setState({ isModalCriarOpen: false });

        } catch (error) {
            console.error("Falha ao adicionar tarefa:", error);
            this.setState({ error: "Não foi possível adicionar a tarefa." });
        }
    };

    editarTarefa = async (tarefaAtualizada: Tarefa) => {
        // A data do formulário vem como 'AAAA-MM-DD'.
        // Enviamos esse valor diretamente ou 'null' se não houver data.
        const prazoParaEnviar = (tarefaAtualizada.entrega && tarefaAtualizada.entrega !== '-')
            ? tarefaAtualizada.entrega
            : null;

        try {
            const response = await fetch(`http://localhost:8080/tarefa/atualizar/${tarefaAtualizada.id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    tar_titulo: tarefaAtualizada.titulo,
                    tar_descricao: tarefaAtualizada.descricao,
                    tar_status: tarefaAtualizada.status,
                    tar_prioridade: tarefaAtualizada.prioridade,
                    tar_prazo: prazoParaEnviar, // Enviando 'AAAA-MM-DD' ou null
                    tar_anexo: null,
                    usu_nome: tarefaAtualizada.responsavel,
                    proj_nome: "API-3sem",
                }),
            });

            if (!response.ok) {
                // Se a requisição falhar, lemos o corpo da resposta para ter mais detalhes.
                const errorBody = await response.text();
                throw new Error(`Erro ao atualizar tarefa: ${response.statusText}. Resposta do servidor: ${errorBody}`);
            }

            // atualizar lista após edição
            await this.carregarTarefas();

            // fechar modal
            this.setState({ isModalEdicaoOpen: false, tarefaSelecionada: null });

        } catch (error) {
            console.error("Falha ao editar tarefa:", error);
            this.setState({ error: "Não foi possível editar a tarefa." });
        }
    };


    excluirTarefa = async (idParaExcluir: string) => {
        try {
            const response = await fetch(`http://localhost:8080/tarefa/apagar/${idParaExcluir}`, {
                method: "DELETE",
            });

            if (!response.ok) {
                const errorBody = await response.text();
                throw new Error(`Erro ao excluir tarefa: ${response.statusText}. Resposta do servidor: ${errorBody}`);
            }
            this.setState(prevState => ({
                tarefas: prevState.tarefas.filter(tarefa => tarefa.id !== idParaExcluir),
            }));
        } catch (error) {
            console.error("Falha ao excluir tarefa:", error);
            this.setState({ error: "Não foi possível excluir a tarefa." });
        }
    };


    getStatusClass = (status: string) => {
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

    render() {
        const { tarefas, loading, error } = this.state;

        if (loading) {
            return <div className="p-4">Carregando tarefas...</div>;
        }

        if (error) {
            return <div className="p-4 text-red-600">{error}</div>;
        }

        return (
            <>
                <div className="bg-white p-4 rounded-lg shadow-md overflow-x-auto gap-4 ">

                    <div className="grid grid-cols-9 gap-4 py-3 px-2 text-xs font-semibold text-gray-500 border-b">
                        <div className="col-span-1">ID</div>
                        <div className="col-span-2"><i className="fa-solid fa-bars-staggered pr-4" />Título</div>
                        <div className="col-span-1"><i className="fa-solid fa-arrow-right pr-4" />Status</div>
                        <div className="col-span-1"><i className="fa-solid fa-user pr-4" />Responsável</div>
                        <div className="col-span-1"><i className="fa-solid fa-tag pr-4" />Entrega</div>
                        <div className="col-span-1"><i className="fa-solid fa-arrow-up pr-4" />Prioridade</div>
                        <div className="col-span-1 text-center"><i className="fa-solid fa-pencil pr-4" />Editar</div>
                        <div className="col-span-1 text-center"><i className="fa-solid fa-trash pr-4" />Excluir</div>
                    </div>

                    <div>
                        {tarefas.map((tarefa) => (
                            <div key={tarefa.id} className="grid grid-cols-9 gap-4 p-3 items-center hover:bg-gray-50 transition-colors duration-150">
                                <div className="col-span-1 text-sm font-medium text-gray-800">{tarefa.id}</div>
                                <div className="col-span-2 text-sm text-gray-800">{tarefa.titulo}</div>
                                <div className="col-span-1">
                                    <span className={`px-2 py-1 text-xs font-bold rounded-md uppercase whitespace-nowrap w-12 ${this.getStatusClass(tarefa.status)}`}>{tarefa.status}</span>
                                </div>
                                <div className="col-span-1 text-sm text-gray-800">{tarefa.responsavel}</div>
                                <div className="col-span-1 text-sm text-gray-600">{tarefa.entrega}</div>
                                <div className="col-span-1">
                                    <span className="px-2 py-1 text-xs font-bold rounded-md uppercase bg-red-100 text-red-700">{tarefa.prioridade}</span>
                                </div>
                                <div className="col-span-1 flex justify-center">
                                    <button
                                        onClick={() => this.handleOpenModalEdicao(tarefa)}
                                        className="text-xs font-semibold bg-gray-200 text-gray-700 px-3 py-1 rounded-md hover:bg-gray-300 transition-colors"                                    >
                                        Editar
                                    </button>
                                </div>
                                {this.state.tarefaSelecionada && (
                                    <ModalEditarTarefas
                                        isOpen={this.state.isModalEdicaoOpen}
                                        onClose={this.handleCloseModalEdicao}
                                        tarefa={this.state.tarefaSelecionada}
                                        onSave={this.handleSaveTarefa}
                                    />
                                )}
                                <div className="col-span-1 flex justify-center">
                                    <button
                                        className="text-xs font-semibold bg-gray-200 text-gray-700 px-3 py-1 rounded-md hover:bg-gray-300 transition-colors"
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
                        className="text-sm font-semibold text-blue-600 hover:text-blue-800 pt-4 cursor-pointer"
                    >
                        <i className="fa-solid fa-plus mr-2"></i>Adicionar Nova Tarefa
                    </button>
                </div>

                <ModalCriarTarefas isOpen={this.state.isModalCriarOpen} onClose={this.closeModalCriar} onAdicionarTarefa={this.adicionarTarefa} />
            </>
        );
    }
}
