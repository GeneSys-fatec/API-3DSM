import React from "react";

export default class NavbarProjetos extends React.Component {
    render() {
        return (
            <div className="flex flex-row h-14 w-full rounded-md shadow-md border border-gray-300 px-4 justify-between items-center">
                <div className="flex gap-8">
                    <p>Visão Geral</p>
                    <p>Tarefas</p>
                    <p>Estatísticas</p>
                </div>
                <div className="flex justify-between items-center gap-2">
                    <div className="relative">
                        <i className="fa-solid fa-magnifying-glass absolute top-1/2 left-3 -translate-y-1/2 text-gray-400"></i>
                        <input type="text" placeholder="Busque uma tarefa..." className="bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-3xl w-90 h-10"></input>
                    </div>
                    <i className="fa-solid fa-arrow-down-wide-short cursor-pointer"></i>
                </div>
            </div>
        )
    }
}