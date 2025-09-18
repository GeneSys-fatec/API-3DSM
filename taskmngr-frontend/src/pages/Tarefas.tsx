import React from "react";
import ListaTarefas from "../components/ListaTarefas";
import type { Projeto } from "../components/BarraLateralProjetos";

type HomeState = {
    projetos: Projeto[];
    isModalOpen: boolean;
};

export default class Tarefas extends React.Component<object, HomeState> {
    render() {
        return (
            <>
                <div className="flex justify-center pt-5">
                            <ListaTarefas></ListaTarefas>
                </div>
            </>
        )
    }
}