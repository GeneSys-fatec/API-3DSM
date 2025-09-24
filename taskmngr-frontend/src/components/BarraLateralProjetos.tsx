import React from "react";
import ModalEditarProjetos from './ModalEditarProjetos';

export interface Projeto {
    id: string;
    nome: string;
    descricao?: string;
    dataCriacao?: string;
}

interface BarraLateralProjetosProps {
    projetos: Projeto[];
    onOpenModal: () => void;
    activeProjectId?: string | number; 
}

interface BarraLateralProjetosState {
    activeProjectId: string | null;
    projetos: Projeto[];
    optionsProjectId: string | null;
    isDeleting: boolean;
    optionsMenuPos: { top: number; left: number } | null; 
    isEditModalOpen: boolean;
    projetoParaEditar: Projeto | null;
}

export default class BarraLateralProjetos extends React.Component<BarraLateralProjetosProps, BarraLateralProjetosState> {
    constructor(props: BarraLateralProjetosProps) {
        super(props);
        this.state = {
            activeProjectId: null,
            projetos: [],
            optionsProjectId: null,
            isDeleting: false,
            optionsMenuPos: null,
            isEditModalOpen: false,
            projetoParaEditar: null,
        };
    }

    private handleProjetoCreated = () => {
        this.fetchProjetos();
    };

    async componentDidMount() {
        window.addEventListener('projeto:created', this.handleProjetoCreated);
        await this.fetchProjetos();
    }

    componentWillUnmount(): void {
        window.removeEventListener('projeto:created', this.handleProjetoCreated);
    }

    fetchProjetos = async () => {
        try {
            const response = await fetch("http://localhost:8080/projeto/listar");
            if (!response.ok) throw new Error("Erro ao buscar projetos");
            const data = await response.json();

            type BackendProjeto = {
                proj_id?: string | number; id?: string | number;
                proj_nome?: string; nome?: string;
                proj_descricao?: string; descricao?: string;
                proj_dataCriacao?: string; dataCriacao?: string;
            };

            const normalized: Projeto[] = Array.isArray(data)
                ? data.map((d: BackendProjeto) => ({
                    id: String(d.proj_id ?? d.id ?? ""),
                    nome: String(d.proj_nome ?? d.nome ?? ""),
                    descricao: String(d.proj_descricao ?? d.descricao ?? ""),
                    dataCriacao: String(d.proj_dataCriacao ?? d.dataCriacao ?? ""),
                }))
                : [];

            this.setState(prev => ({
                projetos: normalized,
                activeProjectId: prev.activeProjectId ?? (normalized.length ? normalized[0].id : null)
            }));
        } catch (error) {
            console.error(error);
        }
    };

    handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await this.fetchProjetos();
    };

    // Agora abre o modal direto ao clicar no projeto
    handleProjectClick = (projectId: string) => {
        this.setState({
            activeProjectId: projectId
        });
    };

    openOptions = (e: React.MouseEvent, projectId: string) => {
        e.stopPropagation();
        const rect = (e.currentTarget as HTMLElement).getBoundingClientRect();
        const top = rect.top + window.scrollY + rect.height / 2;
        const left = rect.left + window.scrollX;
        this.setState({ optionsProjectId: projectId, optionsMenuPos: { top, left } });
    };

    closeOptions = () => {
        this.setState({ optionsProjectId: null, optionsMenuPos: null });
    };

    handleEdit = () => {
        const { optionsProjectId, projetos } = this.state;
        if (!optionsProjectId) return;
        const projeto = projetos.find(p => p.id === optionsProjectId) ?? null;
        this.setState({ projetoParaEditar: projeto, isEditModalOpen: true });
        this.closeOptions();
    };

    closeEditModal = () => {
        this.setState({ isEditModalOpen: false, projetoParaEditar: null });
    };

    handleProjetoSalvo = () => {
        this.fetchProjetos();
        this.closeEditModal();
    };

    handleDelete = async () => {
        const { optionsProjectId } = this.state;
        if (!optionsProjectId) return;

        try {
            this.setState({ isDeleting: true });
            const resp = await fetch(`http://localhost:8080/projeto/apagar/${encodeURIComponent(optionsProjectId)}`, { method: 'DELETE' });
            if (!resp.ok) {
                const text = await resp.text();
                throw new Error(`Falha ao excluir: ${resp.status} ${resp.statusText} - ${text}`);
            }
            await this.fetchProjetos();
            this.closeOptions();
        } catch (err) {
            console.error(err);
            alert('Não foi possível excluir o projeto.');
        } finally {
            this.setState({ isDeleting: false });
        }
    };

    render() {
        const { onOpenModal } = this.props;
        const { projetos, optionsProjectId, optionsMenuPos, isDeleting, isEditModalOpen, projetoParaEditar } = this.state;

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
                            onClick={onOpenModal} 
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
                                    <i
                                        role="button"
                                        aria-label="Mais opções"
                                        title="Mais opções"
                                        tabIndex={0}
                                        onClick={(e) => this.openOptions(e, p.id)}
                                        className={`
                                            fa-solid fa-ellipsis-vertical cursor-pointer p-0 rounded-full
                                            ${isActive ? 'text-indigo-600' : 'text-slate-400 hover:bg-slate-200'}
                                        `}
                                    ></i>
                                </div>
                            );
                        })}
                    </div>
                </div>

                {optionsProjectId && optionsMenuPos && (
                    <>
                        <div className="fixed inset-0 z-40" onClick={this.closeOptions}></div>

                        <div
                            className="fixed z-50 bg-white border border-slate-200 rounded-md shadow-lg w-44 p-2"
                            style={{
                                top: optionsMenuPos.top,
                                left: optionsMenuPos.left,
                                transform: 'translate(calc(-100% - 6px), -50%)'
                            }}
                            onClick={(e) => e.stopPropagation()}
                        >
                            <button
                                onClick={this.handleEdit}
                                className="w-full text-left px-3 py-2 rounded hover:bg-slate-100 flex items-center gap-2"
                            >
                                <i className="fa-solid fa-pen text-slate-600"></i>
                                <span>Editar</span>
                            </button>
                            <button
                                onClick={this.handleDelete}
                                disabled={isDeleting}
                                className="w-full text-left px-3 py-2 rounded hover:bg-red-50 text-red-700 flex items-center gap-2 disabled:opacity-60"
                            >
                                <i className="fa-solid fa-trash"></i>
                                <span>{isDeleting ? 'Excluindo...' : 'Excluir'}</span>
                            </button>
                        </div>
                    </>
                )}

                {isEditModalOpen && projetoParaEditar && (
                    <ModalEditarProjetos
                        isOpen={isEditModalOpen}
                        onClose={this.closeEditModal}
                        projeto={projetoParaEditar}
                        onSaved={this.handleProjetoSalvo}
                    />
                )}
            </div>
        )
    }
}
