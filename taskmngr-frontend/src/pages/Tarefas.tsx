import React from "react";
import NavbarLogin from "../components/headers/NavbarLogin";
import ListaTarefas from "../components/ListaTarefas";
import NavbarProjetos from "../components/NavbarProjetos";
import BarraLateralProjetos from "../components/BarraLateralProjetos";
import BarraLateral from "../components/BarraLateral";
import type { Projeto } from "../components/BarraLateralProjetos";
import ModalProjetos from "../components/ModalProjetos";
import ModalEditarTarefas from "../components/ModalEditarTarefas";

const projetosMock: Projeto[] = [
    {
        id: 1,
        nome: "API-3sem"
    },
    {
        id: 2,
        nome: "Faculdade"
    },
    {
        id: 3,
        nome: "Projeto Pessoal 1"
    },
    {
        id: 4,
        nome: "Projeto Pessoal 2"
    },
]

type HomeState = {
    projetos: Projeto[];
    isModalOpen: boolean;
};

export default class Tarefas extends React.Component<object, HomeState> {
    state = {
        projetos: projetosMock,
        isModalOpen: false
    }

    handleOpenModal = () => {
        this.setState({ isModalOpen: true });
    };

    handleCloseModal = () => {
        this.setState({ isModalOpen: false });
    };

    handleAddProject = (novoProjeto: { nome: string }) => {
        this.setState((prevState) => ({
            projetos: [
                ...prevState.projetos,
                {
                    id: Date.now(),
                    nome: novoProjeto.nome,
                },
            ],

            isModalOpen: false,
        }));
    };


    render() {
        return (
            <div>
                <NavbarLogin></NavbarLogin>
                <div className="flex">
                    <BarraLateral></BarraLateral>
                    <BarraLateralProjetos projetos={this.state.projetos} onOpenModal={this.handleOpenModal}></BarraLateralProjetos>
                    <div className="p-2 w-full">
                        <NavbarProjetos></NavbarProjetos>
                        <ModalProjetos
                            isOpen={this.state.isModalOpen}
                            onClose={this.handleCloseModal}
                            onAddProject={this.handleAddProject}
                        />
                        <ListaTarefas></ListaTarefas>
                    </div>
                </div>
            </div>
        )
    }
}