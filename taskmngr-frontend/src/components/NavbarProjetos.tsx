import React from "react";
import { NavLink } from "react-router-dom";

export default class NavbarProjetos extends React.Component {
    render() {
        return (
            <div className="flex flex-row h-14 w-full rounded-md shadow-md border border-gray-300 px-4 justify-between items-center">
                <div className="flex gap-8">
                    <NavLink to="/home" className={({ isActive }) => `relative pb-1 after:content-[''] after:absolute after:left-0 after:bottom-0 after:h-[1px] after:bg-black after:w-full after:scale-x-0 after:transition-transform after:duration-300 after:ease-in-out ${isActive ? "after:scale-x-100" : "hover:after:scale-x-100"}`}>Visão Geral</NavLink>
                    <NavLink to="/tarefas" className={({ isActive }) => `relative pb-1 after:content-[''] after:absolute after:left-0 after:bottom-0 after:h-[1px] after:bg-black after:w-full after:scale-x-0 after:transition-transform after:duration-300 after:ease-in-out ${isActive ? "after:scale-x-100" : "hover:after:scale-x-100"}`}>Tarefas</NavLink>
                    <NavLink to="/dashboard" className={({ isActive }) => `relative pb-1 after:content-[''] after:absolute after:left-0 after:bottom-0 after:h-[1px] after:bg-black after:w-full after:scale-x-0 after:transition-transform after:duration-300 after:ease-in-out ${isActive ? "after:scale-x-100" : "hover:after:scale-x-100"}`}>Estatísticas</NavLink>
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