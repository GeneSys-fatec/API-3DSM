import React from "react";
import "../../styles/Navbar.css";

export default class NavbarPrincipal extends React.Component {
    render() {
        return ( 
            <div className="flex justify-between items-center bg-blue-950 h-17 p-5">
                <p className="logo text-4xl text-white">GSW</p>
                <div className="flex gap-10">
                    <i className="fa-solid fa-bell text-2xl text-white cursor-pointer"></i>
                    <i className="fa-solid fa-user text-2xl text-white cursor-pointer"></i>
                </div>
            </div>
        )
    }
}