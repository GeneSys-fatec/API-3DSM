import { useState } from "react";
import { Outlet, Link } from "react-router-dom";

import NavbarPrincipal from "../headers/NavbarPrincipal";
import BarraLateral from "../BarraLateral";
import BarraLateralProjetos from "../BarraLateralProjetos";
import NavbarProjetos from "../NavbarProjetos";
import ModalProjetos from "../ModalProjetos";

const BottomNavbar = () => (
  <div className="lg:hidden fixed bottom-0 left-0 right-0 bg-indigo-950 shadow-lg z-30">
    <div className="flex justify-around items-center h-16">
      <Link className="flex-1 flex justify-center p-2" to="/home">
        <i className="fa-solid fa-house text-2xl text-white"></i>
      </Link>
      <Link className="flex-1 flex justify-center p-2" to="/equipes">
        <i className="fa-solid fa-people-group text-2xl text-white"></i>
      </Link>
      <Link className="flex-1 flex justify-center p-2" to="/calendario">
        <i className="fa-solid fa-calendar text-2xl text-white"></i>
      </Link>
      <Link className="flex-1 flex justify-center p-2" to="/info">
        <i className="fa-solid fa-info-circle text-2xl text-white"></i>
      </Link>
    </div>
  </div>
);

export default function LayoutPrincipal() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const [isModalProjetosOpen, setIsModalProjetosOpen] = useState(false);

  const [selectedProjectId, setSelectedProjectId] = useState<string | null>(
    null
  );

  const toggleSidebar = () => setIsSidebarOpen(!isSidebarOpen);

  const handleOpenModal = () => setIsModalProjetosOpen(true);
  const handleCloseModal = () => setIsModalProjetosOpen(false);

  return (
    <div className="bg-slate-50 h-screen flex flex-col">
      <NavbarPrincipal onToggleSidebar={toggleSidebar} />

      <div className="flex flex-1 overflow-hidden">
        <div className="hidden lg:block">
          <BarraLateral />
        </div>
        <div
          className={`
            fixed lg:static top-0 left-0 h-full z-20 transition-transform duration-300 ease-in-out
            ${isSidebarOpen ? "translate-x-0" : "-translate-x-full"} 
            lg:translate-x-0`}
        >
          <BarraLateralProjetos
            onOpenModal={handleOpenModal}
            onProjectSelect={setSelectedProjectId}
          />
        </div>

        {isSidebarOpen && (
          <div
            className="fixed inset-0 bg-black/50 z-10 lg:hidden"
            onClick={toggleSidebar}
          ></div>
        )}

        <div className="p-2 md:p-4 flex-1 flex flex-col min-w-0 h-full">
          <NavbarProjetos />

          <Outlet context={{ selectedProjectId }} />
        </div>
      </div>

      {/* CORRIGIDO: Renderizando o ModalProjetos com as props que ele espera */}
      <ModalProjetos isOpen={isModalProjetosOpen} onClose={handleCloseModal} />

      <BottomNavbar />
    </div>
  );
}
