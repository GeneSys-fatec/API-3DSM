import React from "react";
import { Link } from "react-router-dom";

export default class BarraLateral extends React.Component {
    render() {
        return (
            <div className="flex justify-between h-[calc(100vh-4.28rem)] w-14 bg-indigo-950 flex flex-col items-center py-8">
                <div className="flex flex-col gap-14">
                    <Link to="/home">
                        <i className="fa-solid fa-house text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
                    </Link>
                    <Link to="/equipes">
                        <i className="fa-solid fa-people-group text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
                    </Link>
                    <Link to="/calendario">
                        <i className="fa-solid fa-calendar text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
                    </Link>
                </div>
                <i className="fa-solid fa-info text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
            </div>
        )
    }
}