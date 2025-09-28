import React from "react";
import { Link } from "react-router-dom";
import { toast } from 'react-toastify';
import { withNavigation } from "../utils/UseNavigate";
 
interface CadastroProps {
    navigate: (path: string) => void;
}
 
interface CadastroState {
    nome: string;
    email: string;
    senha: string;
    erros: { [campo: string]: string };
}
 
class FormularioCadastro extends React.Component<CadastroProps, CadastroState> {
    constructor(props: CadastroProps) {
        super(props);
        this.state = {
            nome: "",
            email: "",
            senha: "",
            erros: {},
        };
    }
 
    handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const name = e.target.name as keyof CadastroState;
        const value = e.target.value;
 
        this.setState(prev => ({
            ...prev,
            [name]: value,
            erros: {
                ...prev.erros,
                [name]: "" 
            }
        }));
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
            const response = await fetch("http://localhost:8080/auth/cadastrar", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(novoUsuario),
            });
 
            const data = await response.json();
            const campos = ["nome", "email", "senha"];
            const errosMap: { [campo: string]: string } = {};
 
            if (response.ok) {
                toast.success("Usuário cadastrado com sucesso!");
                this.setState({ nome: "", email: "", senha: "", erros: {} });
                setTimeout(() => {
                    this.props.navigate("/login");
                }, 1500);
            } else {
                if (data.titulo === "Erro de validação" && data.mensagem) {
                    // Mapeia mensagens do backend por campo.
                    data.mensagem.split("; ").forEach((msg: string) => {
                        campos.forEach(campo => {
                            if (msg.toLowerCase().includes(campo)) {
                                errosMap[campo] = msg;
                            }
                        });
                    });
                } else if (data.mensagem) {
                    // Mapeia exceptions específicas do backend.
                    campos.forEach(campo => {
                        if (data.mensagem.toLowerCase().includes(campo)) {
                            errosMap[campo] = data.mensagem;
                        }
                    });
                }
                this.setState({ erros: errosMap });
                toast.error("Erro ao cadastrar");
            }
        } catch (error) {
            console.error("Erro na requisição:", error);
            toast.error("Erro ao conectar com o servidor.");
        }
    };
 
    render() {
        const { nome, email, senha, erros } = this.state;
 
        return (
            <div className="flex flex-col md:flex-row justify-center bg-gray-100 items-center min-h-screen p-4">
                <div className="absolute top-0 left-0 w-full md:hidden">
                    <img src="./BgTop.png" className="img-fluid" />
                </div>
                <div className="absolute bottom-0 left-0 w-full md:hidden">
                    <img src="./BgBottom.png" className="img-fluid" />
                </div>
                <div className="hidden md:flex w-full md:w-120 h-auto md:h-120 flex-col items-center justify-center bg-indigo-950 shadow-md rounded-s-sm p-6">
                    <h1 className="font-medium text-xl md:text-2xl text-white pt-8">Bem-vindo!</h1>
                    <p className="text-center text-white text-sm md:text-base"> Plataforma de Gerenciamento de Tarefas <br /> (To Do/Task Manager) </p>
                    <img src="./Cadastro.png" className="w-60 md:w-110 pt-3 img-fluid" />
                </div>
                <div className="w-full md:w-120 h-auto md:h-120 flex flex-col items-center justify-center md:bg-gray-200 md:shadow-md md:rounded-s-sm p-6 z-0">
                    <h1 className="font-medium text-xl md:text-2xl pb-8">Cadastro</h1>
                    <form className="w-full flex flex-col items-center gap-4" onSubmit={this.handleSubmit}>
                        <div className="relative w-full max-w-xs">
                            <div className="relative">
                                <i className="fa-solid fa-user absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                                <input type="text" name="nome" value={nome} onChange={this.handleChange} placeholder="Nome" className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm" required />
                            </div>
                            {erros.nome && <p className="text-red-500 text-xs mt-1">{erros.nome}</p>}
                        </div>
 
                        <div className="relative w-full max-w-xs">
                            <div className="relative">
                                <i className="fa-solid fa-envelope absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                                <input type="email" name="email" value={email} onChange={this.handleChange} placeholder="E-mail" className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm" required />
                            </div>
                            {erros.email && <p className="text-red-500 text-xs mt-1">{erros.email}</p>}
                        </div>
 
                        <div className="relative w-full max-w-xs">
                            <div className="relative">
                                <i className="fa-solid fa-lock absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                                <input type="password" name="senha" value={senha} onChange={this.handleChange} placeholder="Senha" className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm" required />
                            </div>
                            {erros.senha && <p className="text-red-500 text-xs mt-1">{erros.senha}</p>}
                        </div>
 
                        <button type="submit" className="w-full max-w-xs bg-indigo-950 text-white rounded-sm outline-none cursor-pointer p-2 m-2">
                            Cadastrar-se
                        </button>
                    </form>
                    <Link to="/login" className="underline decoration-solid p-2 text-sm hover:text-blue-900">
                        Já tem uma conta? Faça login
                    </Link>
                </div>
            </div>
        );
    }
}
 
export default withNavigation(FormularioCadastro);