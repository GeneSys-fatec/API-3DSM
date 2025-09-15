import React from "react";
import ModalCriarTarefas from "./ModalCriarTarefas"; 
import ModalEditarTarefa from "./ModalEditarTarefas";

export default class ListaTarefas extends React.Component {
    state = {
        tarefas: [
            {
                id: 1,
                titulo: "Fazer relatório",
                status: "Em andamento",
                responsavel: "Gabriel Medeiros",
                entrega: "2025-09-20",
                prioridade: "Alta",
                descricao: "Descrição do relatório",
                anexo: null,
            },
            {
                id: 2,
                titulo: "Revisar código",
                status: "Em andamento",
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


    render() {
        return (
            <div>
                <div className="flex justify-between items-center mb-6 px-4">
                </div>
                <div className="relative overflow-x-auto sm:rounded-lg">
                    <table className="w-full text-sm text-left rtl:text-right">
                        <thead className="text-xs uppercase bg-white text-gray-400">
                            <tr>
                                <th scope="col" className="py-3">Id</th>
                                <th scope="col" className="py-3">Título</th>
                                <th scope="col" className="pl-20 py-3">Status</th>
                                <th scope="col" className="py-3">Responsável</th>
                                <th scope="col" className="py-3">Data de entrega</th>
                                <th scope="col" className="py-3">Prioridade</th>
                                <th scope="col" className="py-3">Editar</th>
                                <th scope="col" className="py-3">Deletar</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.tarefas.map((tarefa) => (
                                <tr key={tarefa.id} className="bg-white hover:bg-gray-50 transition-colors duration-200">
                                    <td className="py-3">{tarefa.id}</td>
                                    <td className="py-3">{tarefa.titulo}</td>
                                    <td className="pl-20 py-3">{tarefa.status}</td>
                                    <td className="py-3">{tarefa.responsavel}</td>
                                    <td className="py-3">{tarefa.entrega}</td>
                                    <td className="py-3">{tarefa.prioridade}</td>
                                    <td className="py-3">
                                        <button
                                            // onClick={() => this.openModalEditar(tarefa)}  // tem que criar a lógica pra fazer funcionar a cada task
                                            className="font-medium text-white bg-gray-400 p-1 rounded hover:bg-indigo-950 transition-colors"
                                        >
                                            Editar
                                        </button>
                                    </td>
                                    <td className="py-3">
                                        <a href="#" className="font-medium text-white bg-gray-400 p-1 rounded hover:bg-indigo-950 transition-colors">Excluir</a>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <button
                        onClick={this.openModalCriar}
                        className="text-sm"
                    >
                        <i className="fa-solid fa-plus"></i>Adicionar Nova Tarefa
                    </button>
                </div>


                <ModalCriarTarefas isOpen={this.state.isModalCriarOpen} onClose={this.closeModalCriar} />

                {/* Tem que criar a lógica para fazer abrir o modal de editar pra cada task */}
                {/* this.state.isModalEditarOpen && (
                    <ModalEditarTarefa isOpen={this.state.isModalEditarOpen} onClose={this.closeModalEditar}/>
                 ) */}
            </div>
        );
    }
}