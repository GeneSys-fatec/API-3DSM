import React from "react";

export interface Projeto {
    id: number;
    nome: string
}

interface BarraLateralProjetosProps {
    projetos: Projeto[]
}

export default class BarraLateralProjetos extends React.Component<BarraLateralProjetosProps> {
    render() {

        const { projetos } = this.props

        return (
            <div className="h-[calc(100vh-4.28rem)] bg-indigo-300/30 text-white px-4 pt-4">
                <div className="flex bg-indigo-950 w-60 rounded-md shadow-md gap-2 p-2 text-xl">
                    <i className="fa-solid fa-folder-open"></i>
                    <h2 className="leading-none">Projetos</h2>
                </div>
                <div className="text-black flex justify-between py-4 font-semibold">
                    <h1 className="leading-none">Workspaces</h1>
                    <div className="flex text-blue-400 cursor-pointer">
                        <i className="fa-solid fa-plus"></i>
                        <p className="leading-none">Add</p>
                    </div>
                </div>
                <div className="flex flex-col gap-2">
                    {projetos.map((p) => (
                        <div className="flex text-indigo-400 w-60 rounded-md gap-2 p-2 text-md justify-between hover:bg-indigo-400 hover:text-white">
                            <div className="flex">
                                <i className="fa-solid fa-caret-right"></i>
                                <p className="leading-none">{p.nome}</p>
                            </div>
                            <i className="fa-solid fa-ellipsis-vertical cursor-pointer"></i>
                        </div>
                    ))}
                </div>
            </div>
        )
    }
}