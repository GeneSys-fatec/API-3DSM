import React from "react";
import { Link } from "react-router-dom";
import { toast } from "react-toastify";
import { withNavigation } from "@/utils/UseNavigate";

interface LoginProps {
  navigate: (path: string) => void;
}
interface LoginState {
  usuEmail: string;
  usuSenha: string;
  erros: { [campo: string]: string };
  mostrarSenha: boolean;
}

class FormularioLogin extends React.Component<LoginProps, LoginState> {
  constructor(props: LoginProps) {
    super(props);
    this.state = {
      usuEmail: "",
      usuSenha: "",
      erros: {},
      mostrarSenha: false,
    };
  }

  handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    this.setState((prev) => ({
      ...prev,
      [name]: value,
      erros: {
        ...prev.erros,
        [name]: "",
      },
    }));
  };

  toggleMostrarSenha = () => {
    this.setState((prev) => ({
      mostrarSenha: !prev.mostrarSenha
    }))
  }

  handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const { usuEmail, usuSenha } = this.state;
    const usuario = { usuEmail: usuEmail, usuSenha: usuSenha };

    try {
      const response = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(usuario),
      });

      const data = await response.json();
      const errosMap: { [campo: string]: string } = {};
      const campos = ["usuEmail", "usuSenha"];

      if (response.ok) {
        localStorage.setItem("token", data.token);
        localStorage.setItem("nome", data.nome);
        toast.success("Usuário logado com sucesso!");
        setTimeout(() => this.props.navigate("/home"), 1500);
        return;
      } 

      if (data.mensagem) {
        let erroEncontrado = false;
        campos.forEach((campo) => {
          if (data.mensagem.toLowerCase().includes(campo.toLowerCase())) {
            errosMap[campo] = data.mensagem;
            erroEncontrado = true;
          }
        });

        this.setState({ erros: errosMap });

        if (!erroEncontrado) {
          toast.error(data.mensagem);
        }

      }
    } catch (error) {
      console.error("Erro ao conectar com o servidor.", error);
      toast.error("Erro ao conectar com o servidor.");
    }
  };

  render() {
    const { usuEmail, usuSenha, erros, mostrarSenha } = this.state;
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
          <form className="w-full flex flex-col items-center gap-6 md:gap-4" onSubmit={this.handleSubmit}>
            <div className="relative w-full max-w-xs">
              <div className="relative">
                <i className="fa-solid fa-envelope absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                <input
                  type="email"
                  placeholder="E-mail"
                  name="usuEmail"
                  className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm"
                  value={usuEmail}
                  onChange={this.handleChange}
                />
              </div>
              {erros.usuEmail && (
                <p className="text-red-500 text-xs mt-1">{erros.usuEmail}</p>
              )}
            </div>

            <div className="relative w-full max-w-xs">
              <div className="relative">
                <i className="fa-solid fa-lock absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"></i>
                <input
                  type={mostrarSenha ? "text" : "password"}
                  placeholder="Senha"
                  name="usuSenha"
                  className="w-full bg-white pl-10 pr-3 py-2 border border-gray-300 outline-none rounded-sm"
                  value={usuSenha}
                  onChange={this.handleChange}
                />
                <i onClick={this.toggleMostrarSenha} className={`fa-solid ${mostrarSenha ? "fa-eye-slash" : "fa-eye"} absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 cursor-pointer`}></i>
              </div>
              {erros.usuSenha && (
                <p className="text-red-500 text-xs mt-1">{erros.usuSenha}</p>
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