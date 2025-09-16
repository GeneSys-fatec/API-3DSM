import React from "react";
import { Link } from "react-router-dom";
export default class FormularioLogin extends React.Component {
    render() {
        return (
            <div className="flex flex-col md:flex-row justify-center bg-gray-300 items-center min-h-screen p-4 gap-6">        
                <div className="w-full md:w-120 h-auto md:h-120 flex flex-col items-center justify-center bg-gray-100 shadow-md rounded-s-sm p-6 z-0">
                    <h1 className="font-medium text-xl md:text-2xl pb-8">Login</h1>
                    <form className="w-full flex flex-col items-center gap-6 md:gap-4">
                        <div className="relative w-full max-w-xs">
                            <i className="fa-solid fa-envelope absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                            <input type="email" placeholder="E-mail" className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm" required></input>
                        </div>
                        <div className="relative w-full max-w-xs">
                            <i className="fa-solid fa-lock absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                            <input type="password" placeholder="Senha" className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm" required></input>
                        </div>
                        <button type="submit" className="w-full max-w-xs bg-indigo-950 text-white rounded-sm outline-none cursor-pointer p-2 m-2">Login</button>
                    </form>
                    <Link to="/" className="underline decoration-solid p-2 text-sm hover:text-blue-900">Não tem uma conta? Cadastre-se</Link>
                </div>
                <div className="hidden md:flex w-full md:w-120 h-auto md:h-120 flex-col items-center justify-center bg-indigo-950 shadow-md rounded-e-sm p-6">
                    <h1 className="font-medium text-xl md:text-2xl text-white pt-8">Bem-vindo de volta!</h1>
                    <p className="text-center text-white text-sm md:text-base">Para se manter conectado com a gente,<br></br>por favor digite suas informações pessoais.</p>
                    <img src="./Login.png" className="w-60 md:w-110 pt-3 img-fluid"></img>
                </div>
            </div>
        )
    }
}