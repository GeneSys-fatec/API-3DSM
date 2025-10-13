import React, { useState, useContext, useEffect } from 'react';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import '@/styles/Calendario.css';
import { useOutletContext } from "react-router-dom";
import { ModalContext } from "@/context/ModalContext";
import ModalCriarTarefas from "../ModalCriarTarefas";
import ModalEditarTarefas from "../ModalEditarTarefas";
import ModalConfirmacao from "@/components/ui/ModalConfirmacao";
import ModalGoogle from "../ModalGoogle";
import CalendarLegenda from './CalendarioLegenda';
import CalendarioToolbar from './CalendarioToolbar';
import CalendarioComponent from './CalendarioComponent';
import { localizer, eventStyleGetter } from '@/config/calendarioConfig';
import { useTarefas } from '@/hooks/useTarefas';
import type { Tarefa } from "@/types/types";

const CalendarioTarefas: React.FC = () => {
    const [view, setView] = useState<'month' | 'week'>('month');
    const [currentDate, setCurrentDate] = useState(new Date());
    const [showModal, setShowModal] = useState(false);
    const [selectedTask, setSelectedTask] = useState<any>(null);
    const [modalPosition, setModalPosition] = useState({ x: 0, y: 0 });
    const [tarefaParaExcluir, setTarefaParaExcluir] = useState<Tarefa | null>(null);
    const [showGoogleModal, setShowGoogleModal] = useState(false);
    const [isGoogleLogged, setIsGoogleLogged] = useState(false);

    const modalContext = useContext(ModalContext);
    const { selectedProjectId } = useOutletContext<{ selectedProjectId: string | null }>();

    const {
        eventosFiltrados,
        responsaveis,
        responsavelSelecionado,
        setResponsavelSelecionado,
        loading,
        mensagemVazia,
        carregarTarefas,
        excluirTarefa
    } = useTarefas(selectedProjectId);

    const handleSelectEvent = (event: any, e: any) => {
        e.preventDefault();
        setModalPosition({ x: e.clientX, y: e.clientY });
        setSelectedTask(event);
        setShowModal(true);
    };

    const handleEditTask = () => {
        if (selectedTask && modalContext) {
            const tarefaParaEditar = selectedTask.resource.tarefaCompleta;
            setShowModal(false);
            setSelectedTask(null);
            setTimeout(() => {
                modalContext.openModal(
                    <ModalEditarTarefas 
                        tarefa={tarefaParaEditar}
                        onSave={carregarTarefas}
                    />
                );
            }, 200);
        }
    };

    const handleCreateTaskSuccess = () => {
        carregarTarefas();
        window.dispatchEvent(new CustomEvent('taskUpdated', { 
            detail: { 
                action: 'created', 
                projectId: selectedProjectId,
                timestamp: Date.now()
            } 
        }));
        if (modalContext) modalContext.closeModal();
    };

    const handleDeleteTask = () => {
        if (selectedTask) {
            setShowModal(false);
            setTarefaParaExcluir(selectedTask.resource.tarefaCompleta);
            setSelectedTask(null);
        }
    };

    const executarExclusao = async () => {
        if (!tarefaParaExcluir) return;
        const ok = await excluirTarefa(tarefaParaExcluir.tarId);
        if (ok) {
            window.dispatchEvent(new CustomEvent('taskUpdated', { 
                detail: { 
                    action: 'deleted', 
                    projectId: selectedProjectId,
                    taskId: tarefaParaExcluir.tarId,
                    timestamp: Date.now()
                } 
            }));
        } else {
            alert('Erro ao excluir tarefa. Verifique o console para mais detalhes.');
        }
        setTarefaParaExcluir(null);
    };

    const handleSelectSlot = (slotInfo: { start: Date }) => {
        if (!selectedProjectId) return;
        if (modalContext) {
            modalContext.openModal(
                <ModalCriarTarefas
                    onSuccess={handleCreateTaskSuccess}
                    statusInicial="Pendente"
                    selectedProjectId={selectedProjectId}
                    tarPrazo={slotInfo.start} 
                />
            );
        }
    };

    useEffect(() => {
        const handleTaskUpdate = (event: CustomEvent) => {
            const { action, projectId } = event.detail;
            if (projectId === selectedProjectId) {
                if (action === 'created' || action === 'deleted' || action === 'updated') {
                    carregarTarefas();
                }
            }
        };
        window.addEventListener('taskUpdated', handleTaskUpdate as EventListener);
        return () => {
            window.removeEventListener('taskUpdated', handleTaskUpdate as EventListener);
        };
    }, [selectedProjectId, carregarTarefas]);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (showModal) {
                const target = event.target as HTMLElement;
                const modalElement = document.querySelector('.fixed.bg-white.shadow-lg');
                if (modalElement && !modalElement.contains(target)) {
                    setShowModal(false);
                    setSelectedTask(null);
                }
            }
        };
        if (showModal) {
            const timeoutId = setTimeout(() => {
                document.addEventListener('mousedown', handleClickOutside);
            }, 100);
            return () => {
                clearTimeout(timeoutId);
                document.removeEventListener('mousedown', handleClickOutside);
            };
        }
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [showModal]);

    const isMobile = window.innerWidth <= 768;

    if (loading) {
        return (
            <div className="flex items-center justify-center h-96">
                <div className="text-center">
                    <i className="fa-solid fa-spinner fa-spin text-4xl text-blue-600 mb-4"></i>
                    <p className="text-gray-600">Carregando calendário...</p>
                </div>
            </div>
        );
    }

    if (mensagemVazia) {
        return (
            <div className="flex items-center justify-center h-96">
                <div className="text-center">
                    <i className="fa-solid fa-calendar-xmark text-6xl text-gray-400 mb-4"></i>
                    <h3 className="text-xl font-semibold text-gray-700 mb-2">
                        Calendário Vazio
                    </h3>
                    <p className="text-gray-500 mb-4">{mensagemVazia}</p>
                </div>
            </div>
        );
    }

    return (
        <div className="space-y-2 pb-20 sm:pb-0">
            {showModal && selectedTask && (
                <div 
                    className="fixed bg-white shadow-lg border border-gray-300 rounded-lg py-2 z-50"
                    style={{
                        left: window.innerWidth < 640 && modalPosition.x > window.innerWidth - 180 
                            ? modalPosition.x - 160 
                            : modalPosition.x,
                        top: window.innerWidth < 640 && modalPosition.y > window.innerHeight - 120 
                            ? modalPosition.y - 100 
                            : modalPosition.y,
                        minWidth: '150px'
                    }}
                    onMouseDown={(e) => e.stopPropagation()}
                >
                    <button
                        onMouseDown={(e) => e.stopPropagation()}
                        onClick={(e) => {
                            e.preventDefault();
                            e.stopPropagation();
                            handleEditTask();
                        }}
                        className="w-full text-left px-3 py-2 sm:py-2 text-sm hover:bg-blue-50 text-blue-600 flex items-center gap-2 active:bg-blue-100"
                    >
                        <i className="fa-solid fa-edit"></i>
                        Editar
                    </button>
                    <button
                        onMouseDown={(e) => e.stopPropagation()}
                        onClick={(e) => {
                            e.preventDefault();
                            e.stopPropagation();
                            handleDeleteTask();
                        }}
                        className="w-full text-left px-3 py-2 sm:py-2 text-sm hover:bg-red-50 text-red-600 flex items-center gap-2 active:bg-red-100"
                    >
                        <i className="fa-solid fa-trash"></i>
                        Apagar
                    </button>
                </div>
            )}

            <div className="bg-white rounded-lg shadow-md p-2">
                <div className="bg-white p-2">
                    <CalendarioToolbar
                        view={view}
                        setView={setView}
                        isGoogleLogged={isGoogleLogged}
                        setShowGoogleModal={setShowGoogleModal}
                        responsaveis={responsaveis}
                        responsavelSelecionado={responsavelSelecionado}
                        setResponsavelSelecionado={setResponsavelSelecionado}
                        carregarTarefas={carregarTarefas}
                    />
                </div>
                <div className="h-96 sm:h-[500px] lg:h-[650px]">
                    <CalendarioComponent
                        localizer={localizer}
                        eventosFiltrados={eventosFiltrados}
                        view={view}
                        setView={setView}
                        currentDate={currentDate}
                        setCurrentDate={setCurrentDate}
                        eventStyleGetter={eventStyleGetter}
                        handleSelectEvent={handleSelectEvent}
                        handleSelectSlot={handleSelectSlot}
                        isMobile={isMobile}
                        eventosFiltradosLength={eventosFiltrados.length}
                    />
                </div>
            </div>

            <CalendarLegenda />

            {tarefaParaExcluir && (
                <ModalConfirmacao
                    titulo="Tem certeza?"
                    mensagem={
                        <p>
                            A tarefa "
                            <span className="font-bold">
                                {tarefaParaExcluir.tarTitulo}
                            </span>
                            " será excluída permanentemente.
                        </p>
                    }
                    onConfirm={executarExclusao}
                    onCancel={() => setTarefaParaExcluir(null)}
                    confirmText="Excluir"
                    cancelText="Cancelar"
                />
            )}

            <ModalGoogle
                open={showGoogleModal}
                onClose={() => setShowGoogleModal(false)}
                // eslint-disable-next-line @typescript-eslint/no-unused-vars
                onLogincode={(_usuEmail: string, _usuSenha: string) => {
                    setShowGoogleModal(false);
                    setIsGoogleLogged(true);
                }}
            />
        </div>
    );
};

export default CalendarioTarefas;