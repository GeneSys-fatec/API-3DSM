import React from "react";
import "../../styles/Navbar.css";

export default class NavbarPrincipal extends React.Component {
    render() {
        return ( 
            <div className="flex justify-between items-center bg-indigo-950 border-b-3 border-gray-300 shadow-md h-17 p-5">
                <p className="logo text-4xl text-white">GSW</p>
                <div className="flex gap-10">
                    <i className="fa-solid fa-bell text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
                    <i className="fa-solid fa-user text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
                </div>
            </div>
        )
    }
}