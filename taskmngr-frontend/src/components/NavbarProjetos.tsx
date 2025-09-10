import React from "react";

export default class NavbarProjetos extends React.Component {
    render () {
        return (
            <div className="flex flex-col h-12 w-full rounded-md shadow-md border border-gray-300 gap-2 pl-4 justify-center ">
                <div className="flex gap-4">
                    <p>Visão Geral</p>
                    <p>Tarefas</p>
                    <p>Estatísticas</p>
                </div>
            </div>
        )
    }
}