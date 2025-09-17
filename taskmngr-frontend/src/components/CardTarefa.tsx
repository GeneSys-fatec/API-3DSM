import React from "react";

interface CardTarefaProps {
  id: number;
  titulo: string;
  descricao?: string;
  prioridade: string;
  prazo: string;
  responsavel: {
    nome: string;
    fotoURL?: string;
  };
  corClasse: string;
}

export default class CardTarefa extends React.Component<CardTarefaProps> {
  render() {
    
    const { titulo, prioridade, prazo, responsavel, corClasse } = this.props;

    const prioridadeEstilo = {
      Alta: "bg-red-100 text-red-700",
      Média: "bg-yellow-100 text-yellow-700",
      Baixa: "bg-green-100 text-green-700",
    }[prioridade];

    const mapaDeCoresHover = {
      "orange-400": "hover:border-orange-400",
      "blue-400": "hover:border-blue-400",
      "green-500": "hover:border-green-500",
    };

    const classeBordaHover = mapaDeCoresHover[corClasse as keyof typeof mapaDeCoresHover] || "hover:border-blue-400";
    
    const iniciais =
      responsavel && responsavel.nome
        ? responsavel.nome.charAt(0).toUpperCase()
        : "?";

    return (
      
      <div className={`bg-white p-3 rounded-lg shadow-md hover:shadow-xl border-l-4 border-transparent ${classeBordaHover} group transition-all duration-200 transform hover:scale-105`}>
        
        <div className="flex justify-between h-full">
          
          <div className="flex flex-col justify-between">
            
            <h2 className="font-semibold text-gray-800 pr-2">{titulo}</h2>

            
            <div className="flex items-center gap-3 mt-2">
              <span
                className={`text-xs font-semibold px-2.5 py-0.5 rounded-full ${prioridadeEstilo}`}
              >
                {prioridade}
              </span>
              <span className="text-sm text-gray-500">{prazo}</span>
            </div>
          </div>

          
          <div className="flex flex-col justify-between items-center gap-0.5">
            
            <button className="text-gray-400 hover:text-gray-800 opacity-0 group-hover:opacity-100 transition-opacity -mt-1">
              <i className="fa-solid fa-ellipsis-vertical"></i>
            </button>

            
            <div
              title={responsavel.nome}
              className="w-6 h-6 rounded-full bg-indigo-500 text-white flex items-center justify-center text-sm font-bold"
            >
              {iniciais}
            </div>
          </div>
        </div>
      </div>
    );
  }
}
