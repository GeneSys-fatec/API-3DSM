import React from "react";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";
import { withNavigation } from "@/utils/UseNavigate";

interface LoginProps {
  navigate: (path: string) => void;
}
interface LoginState {
  email: string;
  senha: string;
  erros: { [campo: string]: string };
}

class FormularioLogin extends React.Component<LoginProps, LoginState> {
  constructor(props: LoginProps) {
    super(props);
    this.state = {
      email: "",
      senha: "",
      erros: {},
    };
  }

  handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const name = e.target.name as keyof LoginState;
    const value = e.target.value;

    this.setState((prev) => ({
      ...prev,
      [name]: value,
      erros: {
        ...prev.erros,
        [name]: "",
      },
    }));
  };

  handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const { email, senha } = this.state;
    const usuario = { usu_email: email, usu_senha: senha };

    try {
      const res = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(usuario),
      });

      const data = await res.json();
      const campos = ["email", "senha"];
      const errosMap: { [campo: string]: string } = {};

      if (res.ok) {
        localStorage.setItem("token", data.token);
        localStorage.setItem("nome", data.nome);
        console.log("Token:", data.token);
        toast.success("Usuário logado com sucesso!");
        setTimeout(() => {
          this.props.navigate("/home");
        }, 1500);
      } else {
        if (data.mensagem) {
          campos.forEach((campo) => {
            if (data.mensagem.toLowerCase().includes(campo)) {
              errosMap[campo] = data.mensagem;
            }
          });
        }
        this.setState({ erros: errosMap });
        toast.error("Erro ao logar");
      }
    } catch (error) {
      console.error("Erro ao conectar com o servidor.", error);
      toast.error("Erro ao conectar com o servidor.");
    }
  };

  render() {
    const { email, senha, erros } = this.state;

    return (
      <div className="flex flex-col md:flex-row justify-center bg-gray-100 items-center min-h-screen p-4">
        <div className="absolute top-0 left-0 w-full md:hidden">
          <img src="./BgTop.png" className="img-fluid" />
        </div>
        <div className="absolute bottom-0 left-0 w-full md:hidden">
          <img src="./BgBottom.png" className="img-fluid" />
        </div>
        <div className="w-full md:w-120 h-auto md:h-120 flex flex-col items-center justify-center md:bg-gray-200 md:shadow-md md:rounded-s-sm p-6 z-0">
          <h1 className="font-medium text-xl md:text-2xl pb-8">Login</h1>
          <form
            className="w-full flex flex-col items-center gap-6 md:gap-4"
            onSubmit={this.handleSubmit}
          >
            <div className="relative w-full max-w-xs">
              <div className="relative">
                <i className="fa-solid fa-envelope absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                <input
                  type="email"
                  placeholder="E-mail"
                  name="email"
                  className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm"
                  value={email}
                  onChange={this.handleChange}
                  required
                />
              </div>
              {erros.email && (
                <p className="text-red-500 text-xs mt-1">{erros.email}</p>
              )}
            </div>

            <div className="relative w-full max-w-xs">
              <div className="relative">
                <i className="fa-solid fa-lock absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                <input
                  type="password"
                  placeholder="Senha"
                  name="senha"
                  className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm"
                  value={senha}
                  onChange={this.handleChange}
                  required
                />
              </div>
              {erros.senha && (
                <p className="text-red-500 text-xs mt-1">{erros.senha}</p>
              )}
            </div>

            <button
              type="submit"
              className="w-full max-w-xs bg-indigo-950 text-white rounded-sm outline-none cursor-pointer p-2 m-2"
            >
              Login
            </button>
          </form>
          <Link
            to="/cadastro"
            className="underline decoration-solid p-2 text-sm hover:text-blue-900"
          >
            Não tem uma conta? Cadastre-se
          </Link>
        </div>
        <div className="hidden md:flex w-full md:w-120 h-auto md:h-120 flex-col items-center justify-center bg-indigo-950 shadow-md rounded-e-sm p-6">
          <h1 className="font-medium text-xl md:text-2xl text-white pt-8">
            Bem-vindo de volta!
          </h1>
          <p className="text-center text-white text-sm md:text-base">
            Para se manter conectado com a gente,
            <br />
            por favor digite suas informações pessoais.
          </p>
          <img src="./Login.png" className="w-60 md:w-110 pt-3 img-fluid" />
        </div>
      </div>
    );
  }
}

const FormularioLoginComNavegacao = withNavigation(FormularioLogin);
export default FormularioLoginComNavegacao;
