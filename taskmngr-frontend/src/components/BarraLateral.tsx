import React from "react";
import { Link } from "react-router-dom";

export default class BarraLateral extends React.Component {
    render() {
        return (
            <>
                <div className="hidden lg:flex justify-between h-full w-16 border-t-3 border-white bg-indigo-950 flex-col items-center py-8 flex-shrink-0">
                    <div className="flex flex-col gap-14">
                        <Link to="/home" title="Início">
                            <i className="fa-solid fa-house text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
                        </Link>
                        <Link to="/equipes" title="Equipes">
                            <i className="fa-solid fa-people-group text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
                        </Link>
                        <Link to="/calendario" title="Calendário">
                            <i className="fa-solid fa-calendar text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
                        </Link>
                    </div>
                    <Link to="/info" title="Informações">
                        <i className="fa-solid fa-info-circle text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
                    </Link>
                </div>

                <div className="lg:hidden fixed bottom-0 left-0 w-full bg-indigo-950 shadow-lg z-50">
                    <div className="flex justify-around items-center h-16">
                        <Link to="/home" className="flex-1 flex justify-center p-2">
                            <i className="fa-solid fa-house text-2xl text-white"></i>
                        </Link>
                        <Link to="/equipes" className="flex-1 flex justify-center p-2">
                            <i className="fa-solid fa-people-group text-2xl text-white"></i>
                        </Link>
                        <Link to="/calendario" className="flex-1 flex justify-center p-2">
                            <i className="fa-solid fa-calendar text-2xl text-white"></i>
                        </Link>
                        <Link to="/info" className="flex-1 flex justify-center p-2">
                            <i className="fa-solid fa-info-circle text-2xl text-white"></i>
                        </Link>
                    </div>
                </div>
            </>
        )
    }
}

