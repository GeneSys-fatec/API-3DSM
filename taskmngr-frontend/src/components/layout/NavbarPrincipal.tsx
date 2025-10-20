import React from "react";
import "@/styles/Navbar.css";
import { ModalPerfil } from "../features/profile/ModalPerfil";
import { ModalContext } from "@/context/ModalContext";
import type { ModalContextType } from "@/context/ModalContext";
import ModalNotificacoes from "../features/notifications/ModalNotificacoes";
import { buscarNotificacoesNaoLidas, marcarTodasComoLidas } from "../features/notifications/notificacaoService";

interface NavbarPrincipalProps {
  onToggleSidebar: () => void;
  navigate: (path: string) => void;
}

interface NavbarPrincipalState {
  nome: string | null;
  temNaoLidas: boolean;
}

export default class NavbarPrincipal extends React.Component<
  NavbarPrincipalProps,
  NavbarPrincipalState
> {
  static contextType = ModalContext;
  declare context: ModalContextType;

  intervaloNotificacoes?: NodeJS.Timeout;

  constructor(props: NavbarPrincipalProps) {
    super(props);
    this.state = {
      nome: localStorage.getItem("nome"),
      temNaoLidas: false,
    };
  }

  async componentDidMount() {
    await this.verificarNotificacoesNaoLidas();

    this.intervaloNotificacoes = setInterval(() => {
      this.verificarNotificacoesNaoLidas();
    }, 5000);
  }

  componentWillUnmount() {
    if (this.intervaloNotificacoes) {
      clearInterval(this.intervaloNotificacoes);
    }
  }

  verificarNotificacoesNaoLidas = async () => {
    try {
      const temNaoLidas = await buscarNotificacoesNaoLidas();
      this.setState({ temNaoLidas });
    } catch (err) {
      console.error("Erro ao verificar notificações não lidas", err);
    }
  };

  handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("nome");
    this.props.navigate("/login");
  };

  abrirModalPerfil = () => {
    const { isModalOpen, openModal, closeModal } = this.context;
    const { nome } = this.state;
    if (isModalOpen) {
      closeModal();
    } else {
      openModal(<ModalPerfil nome={nome} onLogout={this.handleLogout} />);
    }
  };

  abrirModalNotificacoes = async () => {
    const { isModalOpen, openModal, closeModal } = this.context;
    if (isModalOpen) {
      closeModal();
    } else {
      await marcarTodasComoLidas();
      this.setState({ temNaoLidas: false });

      openModal(
        <ModalNotificacoes
          isOpen={true}
          onClose={() => {
            closeModal();
            this.verificarNotificacoesNaoLidas();
          }}
        />
      );
    }
  };

  render() {

    const { temNaoLidas } = this.state;

    return (
      <div className="flex justify-between items-center bg-indigo-950 shadow-md h-17 p-5">
        <div className="flex items-center gap-4">
          <i
            className="fa-solid fa-bars text-2xl text-white cursor-pointer lg:hidden hamburguer"
            onClick={this.props.onToggleSidebar}
          ></i>
          <p className="logo text-4xl text-white">GSW</p>
        </div>
        <div className="flex gap-10">
          <button className="" onClick={this.abrirModalNotificacoes}>
            <div className="relative">
              <i className="fa-solid fa-bell text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
              {temNaoLidas && (
                <span className="absolute top-0 right-0 inline-block w-2 h-2 bg-red-600 rounded-full"></span>
              )}
            </div>
          </button>
          <button className="" onClick={this.abrirModalPerfil}>
            <i className="fa-solid fa-user text-2xl text-white cursor-pointer hover:text-indigo-200"></i>
          </button>
        </div>
      </div>
    );
  }
}
