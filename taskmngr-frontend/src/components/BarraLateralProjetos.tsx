import React from "react";

export interface Projeto {
    id: number;
    nome: string;
}

interface BarraLateralProjetosProps {
    projetos: Projeto[];
    onOpenModal: () => void;
    
    
    activeProjectId?: number; 
}


interface BarraLateralProjetosState {
    activeProjectId: number | null;
}

export default class BarraLateralProjetos extends React.Component<BarraLateralProjetosProps, BarraLateralProjetosState> {
    

    constructor(props: BarraLateralProjetosProps) {
        super(props);
        this.state = {

            activeProjectId: props.activeProjectId ?? (props.projetos.length > 0 ? props.projetos[0].id : null)
        };
    }


    handleProjectClick = (projectId: number) => {
        this.setState({ activeProjectId: projectId });

    }

    render() {
        const { projetos } = this.props;

        return (

            <div className="h-full w-64 bg-white border-r border-slate-200 flex flex-col">
                
                <div className="p-4 border-b border-slate-200">
                    <div className="flex items-center gap-3">
                        <i className="fa-solid fa-folder-open text-indigo-600 text-2xl"></i>
                        <h2 className="text-xl font-bold text-slate-800">Projetos</h2>
                    </div>
                </div>

                <div className="p-4">
                    <div className="flex justify-between items-center mb-4 pb-3">
                        <h1 className="font-bold text-slate-500 text-sm tracking-wider uppercase">Workspaces</h1>
                        <button 
                            onClick={this.props.onOpenModal} 
                            className="flex items-center gap-1 text-indigo-600 hover:text-indigo-800 font-semibold text-sm transition-colors group bg-indigo-100 hover:bg-indigo-200 rounded-md px-1.5"
                        >
                            <i className="fa-solid fa-plus bg-indigo-100 group-hover:bg-indigo-200 rounded-full"></i>
                            <span>Add</span>
                        </button>
                    </div>

                    <div className="flex flex-col gap-1">
                        {projetos.map((p) => {
                            const isActive = this.state.activeProjectId === p.id;
                            
                            return (
                                <div 
                                    key={p.id}
                                    onClick={() => this.handleProjectClick(p.id)}
                                    className={`
                                        flex items-center justify-between w-full rounded-md p-2 text-md cursor-pointer transition-all duration-150
                                        ${isActive 
                                            ? 'bg-indigo-100 text-indigo-700 font-bold' 
                                            : 'text-slate-600 hover:bg-slate-100 hover:text-slate-900'
                                        }
                                    `}
                                >
                                    <div className="flex items-center gap-2">
                                        <span className={`
                                            w-1.5 h-1.5 rounded-full
                                            ${isActive ? 'bg-indigo-500' : 'bg-slate-300 group-hover:bg-slate-400'}
                                        `}></span>
                                        <p className="leading-none">{p.nome}</p>
                                    </div>
                                    <i className={`
                                        fa-solid fa-ellipsis-vertical cursor-pointer p-1 rounded-full
                                        ${isActive ? 'text-indigo-600' : 'text-slate-400 hover:bg-slate-200'}
                                    `}></i>
                                </div>
                            );
                        })}
                    </div>
                </div>

            </div>
        )
    }
}