import React from "react";
import type { Projeto } from "../components/BarraLateralProjetos";
import ColunaKanban from "../components/ColunaKanban";

const projetosMock: Projeto[] = [
  { id: 1, nome: "API-3sem" },
  { id: 2, nome: "Faculdade" },
  { id: 3, nome: "Projeto Pessoal 1" },
  { id: 4, nome: "Projeto Pessoal 2" },
];

const tarefas = [
  { id: 1, titulo: "Implementar login", descricao: "Adicionar autenticação com JWT", prioridade: "Alta", entrega: "22/08/2025", responsavel: "Matheus", status: "Pendente" },
  { id: 2, titulo: "Criar dashboard", prioridade: "Média", entrega: "30/09/2025", responsavel: "Ana Júlia", status: "Em desenvolvimento" },
  { id: 3, titulo: "Documentar API", prioridade: "Baixa", entrega: "15/10/2025", responsavel: "Lavínia", status: "Feito" },
  { id: 4, titulo: "Configurar ambiente", descricao: "", prioridade: "Média", entrega: "22/08/2025", responsavel: "Gabriel", status: "Pendente" },
  { id: 5, titulo: "Configurar rotas", prioridade: "Baixa", entrega: "30/09/2025", responsavel: "Ana Beatriz", status: "Pendente" },
  { id: 6, titulo: "Autenticação de usuário", prioridade: "Média", entrega: "15/10/2025", responsavel: "Giovanni", status: "Feito" },
  { id: 7, titulo: "Implementar login", descricao: "Adicionar autenticação com JWT", prioridade: "Alta", entrega: "22/08/2025", responsavel: "Emmanuel", status: "Pendente" },
  { id: 8, titulo: "Página Projetos", prioridade: "Alta", entrega: "30/09/2025", responsavel: "Gabriel Calebbe", status: "Em desenvolvimento" },
  { id: 9, titulo: "Documentação README", prioridade: "Baixa", entrega: "15/10/2025", responsavel: "Lavínea", status: "Feito" },
];

type Coluna = { titulo: string; status: string; corClasse: string; corFundo: string; };
type HomeState = { projetos: Projeto[]; isModalOpen: boolean; colunas: Coluna[]; };

export default class Home extends React.Component<object, HomeState> {
  state = {
    projetos: projetosMock,
    isModalOpen: false,
    colunas: [
      { titulo: "Pendente", status: "Pendente", corClasse: "orange-400", corFundo: "bg-orange-400/35" },
      { titulo: "Em desenvolvimento", status: "Em desenvolvimento", corClasse: "blue-400", corFundo: "bg-blue-400/40" },
      { titulo: "Feito", status: "Feito", corClasse: "green-500", corFundo: "bg-green-500/40" },
    ],
  };

  handleOpenModal = () => this.setState({ isModalOpen: true });
  handleCloseModal = () => this.setState({ isModalOpen: false });
  handleAddProject = (novoProjeto: { nome: string }) => {
    this.setState((prevState) => ({
      projetos: [...prevState.projetos, { id: Date.now(), nome: novoProjeto.nome }],
      isModalOpen: false,
    }));
  };
  handleAddColumn = () => {
    const nomeNovaColuna = prompt("Digite o nome da nova coluna:");
    if (nomeNovaColuna && nomeNovaColuna.trim() !== "") {
      const novaColuna: Coluna = {
        titulo: nomeNovaColuna, status: nomeNovaColuna, corClasse: "gray-400", corFundo: "bg-gray-400/35",
      };
      this.setState((prevState) => ({ colunas: [...prevState.colunas, novaColuna] }));
    }
  };

  render() {
    return (
      <>
        <div className="flex flex-col h-full lg:flex-row items-center lg:items-start gap-6 pt-5 pb-4 lg:pr-4 flex-1 overflow-y-auto lg:overflow-x-auto lg:overflow-y-hidden">
            {this.state.colunas.map((coluna) => (
              <ColunaKanban key={coluna.status} titulo={coluna.titulo} corClasse={coluna.corClasse} corFundo={coluna.corFundo}
                tarefas={tarefas.filter((t) => t.status === coluna.status)
                  .map((t) => ({ id: t.id, titulo: t.titulo, status: t.status, entrega: t.entrega, prioridade: t.prioridade as "Alta" | "Média" | "Baixa", responsavel: t.responsavel, descricao: t.descricao ?? "", anexo: null }))}
              />
            ))}
            <div className="w-full lg:w-80 flex-shrink-0 mt-auto lg:mt-0">
              <button onClick={this.handleAddColumn} className="w-full rounded-lg bg-gray-200/70 p-3 text-center transition-colors hover:bg-gray-300 lg:h-full flex items-center justify-center" >
                <h2 className="text-xl font-bold tracking-wider text-gray-600"> + Adicionar Card </h2>
              </button>
            </div>
          </div>
    </>
    );
  }
}

