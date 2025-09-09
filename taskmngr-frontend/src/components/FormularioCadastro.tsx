import React from "react";
import BotaoFormulario from "./BotaoFormulario";
import { Link } from "react-router-dom";

export default class FormularioCadastro extends React.Component {
    render() {
        return (
            <div className="flex justify-center items-center h-[calc(100vh-6rem)]">
                <div className="w-120 h-120 flex flex-col items-center justify-center bg-blue-950 shadow-md rounded-s-sm">
                    <h1 className="font-medium text-2xl text-white pt-8">Bem-vindo!</h1>
                    <p className="text-center text-white text-sm">Plataforma de Gerenciamento de Tarefas <br></br>(To Do/Task Manager)</p>
                    <img src="./Cadastro.png" className="w-110 img-fluid pt-3"></img>
                </div>
                <div className="w-120 h-120 flex flex-col items-center justify-center bg-gray-100 shadow-md rounded-s-sm">
                    <h1 className="font-medium text-2xl pb-8">Cadastro</h1>
                    <form>
                        <div className="relative w-80">
                            <i className="fa-solid fa-user absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                            <input type="text" placeholder="Nome" className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm" required></input>
                        </div><br></br>
                        <div className="relative w-80">
                            <i className="fa-solid fa-envelope absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                            <input type="email" placeholder="E-mail" className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm" required></input>
                        </div><br></br>
                        <div className="relative w-80">
                            <i className="fa-solid fa-lock absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                            <input type="password" placeholder="Senha" className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm" required></input>
                        </div><br></br>
                        <BotaoFormulario titulo="Cadastrar-se"></BotaoFormulario>
                    </form>
                    <Link to="/login" className="underline decoration-solid p-2 text-sm hover:text-blue-900">Já tem uma conta? Faça login</Link>
                </div>
            </div>
        )
    }
}