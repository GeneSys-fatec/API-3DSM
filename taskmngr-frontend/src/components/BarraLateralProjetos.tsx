import React from "react";

export default class BarraLateralProjetos extends React.Component {
    render() {
        return (
            <div className="h-[calc(100vh-4.28rem)] bg-blue-950/20 text-white px-4 pt-4">
                <div className="flex bg-blue-950/40 w-60 rounded-md gap-2 p-2 text-xl">
                    <i className="fa-solid fa-folder-open"></i>
                    <h2 className="leading-none">Projetos</h2>
                </div>
                <div className="text-black flex justify-between py-4 font-semibold">
                    <h1 className="leading-none">Workspaces</h1>
                    <div className="flex text-blue-500 cursor-pointer">
                        <i className="fa-solid fa-plus"></i>
                        <p className="leading-none">Add</p>
                    </div>
                </div>
                <div className="flex flex-col gap-2">
                <div className="flex bg-blue-950/40 w-60 rounded-md gap-2 p-2 text-md justify-between">
                    <div className="flex">
                    <i className="fa-solid fa-caret-right"></i>
                    <p className="leading-none">Projeto 1</p>
                    </div>
                    <i className="fa-solid fa-ellipsis-vertical"></i>
                </div>
                <div className="flex w-60 rounded-md gap-2 p-2 text-md justify-between">
                    <div className="flex">
                    <i className="fa-solid fa-caret-right"></i>
                    <p className="leading-none">Projeto 1</p>
                    </div>
                    <i className="fa-solid fa-ellipsis-vertical"></i>
                </div>
                </div>
            </div>
        )
    }
}