import { Component } from "react";
import { Outlet } from "react-router-dom";

import NavbarPrincipal from "../headers/NavbarPrincipal";
import BarraLateral from "../BarraLateral";
import BarraLateralProjetos, { type Projeto } from "../BarraLateralProjetos";
import NavbarProjetos from "../NavbarProjetos";
import ModalProjetos from "../ModalProjetos";

const projetosMock: Projeto[] = [
  { id: 1, nome: "API-3sem" },
  { id: 2, nome: "Faculdade" },
  { id: 3, nome: "Projeto Pessoal 1" },
  { id: 4, nome: "Projeto Pessoal 2" },
];

interface LayoutState {
  projetos: Projeto[];
  isModalOpen: boolean;
  isSidebarOpen: boolean;
}

export default class LayoutPrincipal extends Component<object, LayoutState> {
  state = {
    projetos: projetosMock,
    isModalOpen: false,
    isSidebarOpen: false,
  };

  handleOpenModal = () => this.setState({ isModalOpen: true });
  handleCloseModal = () => this.setState({ isModalOpen: false });

  handleAddProject = (novoProjeto: { nome: string }) => {
    this.setState((prevState) => ({
      projetos: [
        ...prevState.projetos,
        { id: Date.now(), nome: novoProjeto.nome },
      ],
      isModalOpen: false,
    }));
  };
  toggleSidebar = () => {
    this.setState((prevState) => ({ isSidebarOpen: !prevState.isSidebarOpen }));
  };

  render() {
    return (
      <div className="bg-slate-50 h-screen flex flex-col">
        <NavbarPrincipal onToggleSidebar={this.toggleSidebar} />

        <div className="flex flex-1 overflow-hidden">
          <div className="lg:block">
            <BarraLateral />
          </div>
          <div className={`
            fixed lg:static top-0 left-0 h-full z-20 transition-transform duration-300 ease-in-out
            ${this.state.isSidebarOpen ? 'translate-x-0' : '-translate-x-full'} 
            lg:translate-x-0`
        }>
            <BarraLateralProjetos 
                projetos={this.state.projetos} 
                onOpenModal={this.handleOpenModal} 
            />
        </div>

        {this.state.isSidebarOpen && (
            <div 
                className="fixed inset-0 bg-black/50 z-10 lg:hidden"
                onClick={this.toggleSidebar}
            ></div>
        )}

          <div className="p-2 md:p-4 flex-1 flex flex-col min-w-0 h-full">
            <NavbarProjetos />

            <Outlet />
          </div>
        </div>

        <ModalProjetos
          isOpen={this.state.isModalOpen}
          onClose={this.handleCloseModal}
          onAddProject={this.handleAddProject}
        />
      </div>
    );
  }
}
