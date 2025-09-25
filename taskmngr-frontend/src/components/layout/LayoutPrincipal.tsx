import { Component } from "react";
import { Outlet, Link } from "react-router-dom";

import NavbarPrincipal from "../headers/NavbarPrincipal";
import BarraLateral from "../BarraLateral";
import BarraLateralProjetos, { type Projeto } from "../BarraLateralProjetos";
import NavbarProjetos from "../NavbarProjetos";
import ModalProjetos from "../ModalProjetos";


interface LayoutState {
  projetos: Projeto[];
  isModalOpen: boolean;
  isSidebarOpen: boolean;
}

// eslint-disable-next-line react-refresh/only-export-components
const BottomNavbar = () => (
  <div className="lg:hidden fixed bottom-0 left-0 right-0 bg-indigo-950 shadow-lg z-30">
    <div className="flex justify-around items-center h-16">
      <Link className="flex-1 flex justify-center p-2" to="/home"><i className="fa-solid fa-house text-2xl text-white"></i></Link>
      <Link className="flex-1 flex justify-center p-2" to="/equipes"><i className="fa-solid fa-people-group text-2xl text-white"></i></Link>
      <Link className="flex-1 flex justify-center p-2" to="/calendario"><i className="fa-solid fa-calendar text-2xl text-white"></i></Link>
      <Link className="flex-1 flex justify-center p-2" to="/info"><i className="fa-solid fa-info-circle text-2xl text-white"></i></Link>
    </div>
  </div>
);

export default class LayoutPrincipal extends Component<object, LayoutState> {
  state = {
    projetos: [],
    isModalOpen: false,
    isSidebarOpen: false,
  };

  handleOpenModal = () => this.setState({ isModalOpen: true });
  handleCloseModal = () => this.setState({ isModalOpen: false });

  handleAddProject = (novoProjeto: { nome: string }) => {
    this.setState((prevState) => ({
      projetos: [
        ...prevState.projetos,
        { id: Date.now().toString(), nome: novoProjeto.nome },
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
          <div className="hidden lg:block">
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
        
        <BottomNavbar />
      </div>
    );
  }
}

