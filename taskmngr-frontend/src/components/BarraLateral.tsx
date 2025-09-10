import React from "react";

export default class BarraLateral extends React.Component {
    render() {
        return (
            <div className="h-[calc(100vh-4.28rem)] w-14 bg-black flex flex-col items-center py-8 gap-14">
                <i className="fa-solid fa-house text-2xl text-white cursor-pointer hover:text-gray-300"></i>
                <i className="fa-solid fa-people-group text-2xl text-white cursor-pointer hover:text-gray-300"></i>
                <i className="fa-solid fa-calendar text-2xl text-white cursor-pointer hover:text-gray-300"></i>
                <i className="fa-solid fa-chart-line text-2xl text-white cursor-pointer hover:text-gray-300"></i>
            </div>
        )
    }
}