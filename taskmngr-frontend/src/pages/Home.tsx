import React from "react";
import NavbarPrincipal from "../components/headers/NavbarPrincipal";
import BarraLateral from "../components/BarraLateral";
import BarraLateralProjetos from "../components/BarraLateralProjetos";
import type { Projeto } from "../components/BarraLateralProjetos";
import NavbarProjetos from "../components/NavbarProjetos";
import CardTarefa from "../components/CardTarefa";
import ModalProjetos from "../components/ModalProjetos";

const projetosMock: Projeto[] = [
  {
    id: 1,
    nome: "API-3sem",
  },
  {
    id: 2,
    nome: "Faculdade",
  },
  {
    id: 3,
    nome: "Projeto Pessoal 1",
  },
  {
    id: 4,
    nome: "Projeto Pessoal 2",
  },
];

const tarefas = [
  {
    id: 1,
    titulo: "Implementar login",
    descricao: "Adicionar autenticação com JWT",
    prioridade: "Alta",
    prazo: "22/08/2025",
    responsavel: { nome: "Matheus" },
    status: "Pendente",
  },
  {
    id: 2,
    titulo: "Criar dashboard",
    prioridade: "Média",
    prazo: "30/09/2025",
    responsavel: { nome: "Ana Júlia" },
    status: "Em desenvolvimento",
  },
  {
    id: 3,
    titulo: "Documentar API",
    prioridade: "Baixa",
    prazo: "15/10/2025",
    responsavel: { nome: "Lavínia" },
    status: "Feito",
  },
  {
    id: 4,
    titulo: "Configurar ambiente",
    descricao: "",
    prioridade: "Média",
    prazo: "22/08/2025",
    responsavel: { nome: "Gabriel" },
    status: "Pendente",
  },
  {
    id: 5,
    titulo: "Configurar rotas",
    prioridade: "Baixa",
    prazo: "30/09/2025",
    responsavel: { nome: "Ana Beatriz" },
    status: "Pendente",
  },
  {
    id: 6,
    titulo: "Autenticação de usuário",
    prioridade: "Média",
    prazo: "15/10/2025",
    responsavel: { nome: "Giovanni" },
    status: "Feito",
  },
  {
    id: 7,
    titulo: "Implementar login",
    descricao: "Adicionar autenticação com JWT",
    prioridade: "Alta",
    prazo: "22/08/2025",
    responsavel: { nome: "Emmanuel" },
    status: "Pendente",
  },
  {
    id: 8,
    titulo: "Página Projetos",
    prioridade: "Alta",
    prazo: "30/09/2025",
    responsavel: { nome: "Gabriel Calebbe" },
    status: "Em desenvolvimento",
  },
  {
    id: 9,
    titulo: "Documentação README",
    prioridade: "Baixa",
    prazo: "15/10/2025",
    responsavel: { nome: "Lavínea" },
    status: "Feito",
  },
];

type HomeState = {
  projetos: Projeto[];
  isModalOpen: boolean;
};

export default class Home extends React.Component<object, HomeState> {
  state = {
    projetos: projetosMock,
    isModalOpen: false,
  };

  handleOpenModal = () => {
    this.setState({ isModalOpen: true });
  };

  handleCloseModal = () => {
    this.setState({ isModalOpen: false });
  };

  handleAddProject = (novoProjeto: { nome: string }) => {
    this.setState((prevState) => ({
      projetos: [
        ...prevState.projetos,
        {
          id: Date.now(),
          nome: novoProjeto.nome,
        },
      ],

      isModalOpen: false,
    }));
  };

  render() {
    return (
      <div>
        <NavbarPrincipal></NavbarPrincipal>
        <div className="flex">
          <BarraLateral></BarraLateral>
          <BarraLateralProjetos
            projetos={this.state.projetos}
            onOpenModal={this.handleOpenModal}
          ></BarraLateralProjetos>
          <div className="p-2 w-full">
            <NavbarProjetos></NavbarProjetos>
            <div className="flex justify-between gap-4 pt-4">
              <div className="w-1/3 m-4 rounded-md shadow-md border border-gray-300 bg-indigo-300/30 flex flex-col h-[73vh]">
                <div className="flex justify-center py-2 text-white text-2xl font-medium border-b border-gray-300 bg-orange-400 rounded-t-sm">
                  <h1>Pendente</h1>
                </div>

                <div className="p-4 flex flex-col gap-4 overflow-y-auto">
                  {tarefas
                    .filter((t) => t.status === "Pendente")
                    .map((t) => (
                      <CardTarefa
                        key={t.id}
                        id={t.id}
                        titulo={t.titulo}
                        descricao={t.descricao}
                        prioridade={t.prioridade as "Alta" | "Média" | "Baixa"}
                        prazo={t.prazo}
                        responsavel={t.responsavel}
                      />
                    ))}
                  <div className="flex justify-center text-2xl p-2 bg-white rounded-md">
                    <i className="fa-solid fa-plus"></i>
                  </div>
                </div>
              </div>

              <div className="w-1/3 m-4 rounded-md shadow-md border border-gray-300 bg-indigo-300/30 flex flex-col h-[73vh]">
                <div className="flex justify-center py-2 text-white text-2xl font-medium border-b border-gray-300 bg-blue-400 rounded-t-sm">
                  <h1>Em desenvolvimento</h1>
                </div>

                <div className="p-4 flex flex-col gap-4">
                  {tarefas
                    .filter((t) => t.status === "Em desenvolvimento")
                    .map((t) => (
                      <CardTarefa
                        key={t.id}
                        id={t.id}
                        titulo={t.titulo}
                        descricao={t.descricao}
                        prioridade={t.prioridade as "Alta" | "Média" | "Baixa"}
                        prazo={t.prazo}
                        responsavel={t.responsavel}
                      />
                    ))}
                  <div className="flex justify-center text-2xl p-2 bg-white rounded-md">
                    <i className="fa-solid fa-plus"></i>
                  </div>
                </div>
              </div>

              <div className="w-1/3 m-4 rounded-md shadow-md border border-gray-300 bg-indigo-300/30 flex flex-col h-[73vh]">
                <div className="flex justify-center text-white py-2 text-2xl font-medium border-b border-gray-300 bg-green-400 rounded-t-sm">
                  <h1>Feito</h1>
                </div>

                <div className="p-4 flex flex-col gap-4">
                  {tarefas
                    .filter((t) => t.status === "Feito")
                    .map((t) => (
                      <CardTarefa
                        key={t.id}
                        id={t.id}
                        titulo={t.titulo}
                        descricao={t.descricao}
                        prioridade={t.prioridade as "Alta" | "Média" | "Baixa"}
                        prazo={t.prazo}
                        responsavel={t.responsavel}
                      />
                    ))}
                  <div className="flex justify-center text-2xl p-2 bg-white rounded-md">
                    <i className="fa-solid fa-plus"></i>
                  </div>
                </div>
              </div>
            </div>
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
