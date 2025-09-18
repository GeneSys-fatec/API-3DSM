import React from "react";
import "../../styles/Navbar.css";

interface NavbarPrincipalProps {
    onToggleSidebar: () => void;
}

export default class NavbarPrincipal extends React.Component<NavbarPrincipalProps> {
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
                    <i className="fa-solid fa-bell text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
                    <i className="fa-solid fa-user text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
                </div>
            </div>
        )
    }
}