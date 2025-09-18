import React from "react";
import { Link } from "react-router-dom";
import { toast } from 'react-toastify';

interface Props {
    nome: string;
    email: string;
    senha: string;
}

export default class FormularioCadastro extends React.Component<{}, Props> {
    constructor(props: {}) {
        super(props);
        this.state = {
            nome: "",
            email: "",
            senha: "",
        };
    }

    handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const name = e.target.name as keyof Props;
        this.setState({ [name]: e.target.value } as Pick<Props, keyof Props>);
    };

    handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const { nome, email, senha } = this.state;
        const novoUsuario = {
            usu_nome: nome,
            usu_email: email,
            usu_senha: senha
        };

        try {
            const response = await fetch("http://localhost:8080/usuario/cadastrar", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(novoUsuario),
            });

            if (response.ok) {
                toast.success("Usuário cadastrado com sucesso!");
                this.setState({ nome: "", email: "", senha: "" });
            } else {
                toast.error("Erro ao cadastrar usuário");
            }
        } catch (error) {
            console.error("Erro na requisição:", error);
        }
    };

    render() {
        return (
            <div className="flex flex-col md:flex-row justify-center bg-gray-100 items-center min-h-screen p-4">
                <div className="absolute top-0 left-0 w-full md:hidden">
                    <img src="./BgTop.png" className="img-fluid"></img>
                </div>
                <div className="absolute bottom-0 left-0 w-full md:hidden">
                    <img src="./BgBottom.png" className="img-fluid"></img>
                </div>
                <div className="hidden md:flex w-full md:w-120 h-auto md:h-120 flex-col items-center justify-center bg-indigo-950 shadow-md rounded-s-sm p-6">
                    <h1 className="font-medium text-xl md:text-2xl text-white pt-8">Bem-vindo!</h1>
                    <p className="text-center text-white text-sm md:text-base">Plataforma de Gerenciamento de Tarefas <br></br>(To Do/Task Manager)</p>
                    <img src="./Cadastro.png" className="w-60 md:w-110 pt-3 img-fluid"></img>
                </div>
                <div className="w-full md:w-120 h-auto md:h-120 flex flex-col items-center justify-center md:bg-gray-200 md:shadow-md md:rounded-s-sm p-6 z-0">
                    <h1 className="font-medium text-xl md:text-2xl pb-8">Cadastro</h1>
                    <form className="w-full flex flex-col items-center gap-4" onSubmit={this.handleSubmit}>
                        <div className="relative w-full max-w-xs">
                            <i className="fa-solid fa-user absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                            <input type="text" name="nome" value={this.state.nome} onChange={this.handleChange} placeholder="Nome" className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm" required></input>
                        </div>
                        <div className="relative w-full max-w-xs">
                            <i className="fa-solid fa-envelope absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                            <input type="email" name="email" value={this.state.email} onChange={this.handleChange} placeholder="E-mail" className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm" required></input>
                        </div>
                        <div className="relative w-full max-w-xs">
                            <i className="fa-solid fa-lock absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                            <input type="password" name="senha" value={this.state.senha} onChange={this.handleChange} placeholder="Senha" className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm" required></input>
                        </div>
                        <button type="submit" className="w-full max-w-xs bg-indigo-950 text-white rounded-sm outline-none cursor-pointer p-2 m-2">Cadastrar-se</button>
                    </form>
                    <Link to="/login" className="underline decoration-solid p-2 text-sm hover:text-blue-900">Já tem uma conta? Faça login</Link>
                </div>
            </div>
        )
    }
}