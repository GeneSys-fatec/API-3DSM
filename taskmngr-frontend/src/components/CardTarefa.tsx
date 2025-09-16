import React from "react";

interface CardTarefaProps {
    id: number;
    titulo: string;
    descricao?: string;
    prioridade: "Alta" | "Média" | "Baixa";
    prazo: string;
    responsavel: {
        nome: string;
        fotoURL?: string;
    };
}

export default class CardTarefa extends React.Component<CardTarefaProps> {
    render() {

        const { titulo, prioridade, prazo } = this.props

        const prioridadeCor = {
            Alta: "bg-red-400",
            Média: "bg-yellow-400",
            Baixa: "bg-green-400",
        }[prioridade]

        return (
            <div className="flex justify-between p-2 bg-white rounded-md shadow-md">
                <div>
                    <h1 className="text-lg font-medium">{titulo}</h1>
                    <div className="flex gap-2">
                        <div className={`rounded-xl ${prioridadeCor} px-2 py-1 text-white text-xs`}>
                            <h1>{prioridade}</h1>
                        </div>
                        <div>
                            <p>{prazo}</p>
                        </div>
                    </div>
                </div>
                <div className="flex flex-col items-center justify-between gap-2 pt-1">
                    <i className="fa-solid fa-ellipsis-vertical cursor-pointer w-8 text-center"></i>
                    <i className="fa-solid fa-user text-2xl cursor-pointer hover:text-indigo-200 w-8 text-center"></i>
                </div>
            </div>
        )
    }
}