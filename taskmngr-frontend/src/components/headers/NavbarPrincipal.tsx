import React from "react";
import "../../styles/Navbar.css";
import { ModalPerfil } from "../ModalPerfil"
import { ModalContext } from "../../context/ModalContext";
import type { ModalContextType } from "../../context/ModalContext";

interface NavbarPrincipalProps {
    onToggleSidebar: () => void;
    navigate: (path: string) => void;
}

interface NavbarPrincipalState {
    nome: string | null;
}

export default class NavbarPrincipal extends React.Component<NavbarPrincipalProps, NavbarPrincipalState> {
    static contextType = ModalContext;
    declare context: ModalContextType;

    constructor(props: NavbarPrincipalProps) {
        super(props);
        this.state = {
            nome: localStorage.getItem("nome"),
        };
    }

    handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("userId");
        localStorage.removeItem("nome");
        this.props.navigate("/login");
    }

    abrirModalPerfil = () => {
        const { isModalOpen, openModal, closeModal } = this.context;
        const { nome } = this.state;
        if (isModalOpen) {
            closeModal();
        } else {
            openModal(<ModalPerfil nome={nome} onLogout={this.handleLogout}/>);
        }
    };
    render() {
        return ( 
            <div className="flex justify-between items-center bg-indigo-950 shadow-md h-17 p-5">
                <div className="flex items-center gap-4">
                    <i 
                        className="fa-solid fa-bars text-2xl text-white cursor-pointer lg:hidden hamburguer"
                        onClick={this.props.onToggleSidebar}
                    ></i>
                    <p className="logo text-4xl text-white">GSW</p>
                </div>
                <div className="flex gap-10">
                    <div className="">
                        <i className="fa-solid fa-bell text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
                    </div>
                    <button className="" onClick={this.abrirModalPerfil}>
                        <i className="fa-solid fa-user text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
                    </button>
                </div>
            </div>
        )
    }
}