import React, { useState, useEffect, useCallback, useContext } from 'react';
import { Calendar, momentLocalizer, Views } from 'react-big-calendar';
import moment from 'moment';
import 'moment/locale/pt-br';
import 'react-big-calendar/lib/css/react-big-calendar.css';
import '@/styles/Calendario.css';
import { useOutletContext } from "react-router-dom";
import { authFetch } from "@/utils/api";
import { ModalContext } from "@/context/ModalContext";
import ModalCriarTarefas from "./ModalCriarTarefas";
import ModalEditarTarefas from "./ModalEditarTarefas";
import ModalConfirmacao from "@/components/ui/ModalConfirmacao";
import ModalGoogle from "../profile/ModalGoogle";
import type { Tarefa } from "@/types/types";

moment.locale('pt-br');
moment.updateLocale('pt-br', {
  week: {
    dow: 1,
    doy: 4
  },
  months: [
    'Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho',
    'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'
  ],
  monthsShort: [
    'Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun',
    'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'
  ],
  weekdays: [
    'Domingo', 'Segunda-feira', 'Terça-feira', 'Quarta-feira',
    'Quinta-feira', 'Sexta-feira', 'Sábado'
  ],
  weekdaysShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'],
  weekdaysMin: ['Do', 'Se', 'Te', 'Qu', 'Qu', 'Se', 'Sá']
});

const localizer = momentLocalizer(moment);

interface EventoTarefa {
    id: string;
    title: string;
    start: Date;
    end: Date;
    resource: {
        status: string;
        prioridade: string;
        responsavel: string;
        tarefaCompleta: Tarefa;
    };
}

const CalendarioTarefas: React.FC = () => {
    const [eventos, setEventos] = useState<EventoTarefa[]>([]);
    const [eventosFiltrados, setEventosFiltrados] = useState<EventoTarefa[]>([]);
    const [view, setView] = useState<'month' | 'week'>('month');
    const [loading, setLoading] = useState(true);
    const [mensagemVazia, setMensagemVazia] = useState<string>('');
    const [currentDate, setCurrentDate] = useState(new Date());
    
    const [responsaveis, setResponsaveis] = useState<string[]>([]);
    const [responsavelSelecionado, setResponsavelSelecionado] = useState<string>('');
    
    const [showModal, setShowModal] = useState(false);
    const [selectedTask, setSelectedTask] = useState<EventoTarefa | null>(null);
    const [modalPosition, setModalPosition] = useState({ x: 0, y: 0 });
    
    const [tarefaParaExcluir, setTarefaParaExcluir] = useState<Tarefa | null>(null);
    const [showGoogleModal, setShowGoogleModal] = useState(false);
    const [isGoogleLogged, setIsGoogleLogged] = useState(false);
    
    const modalContext = useContext(ModalContext);
    
    const { selectedProjectId } = useOutletContext<{
        selectedProjectId: string | null;
    }>();

    const carregarTarefas = useCallback(async () => { //pega as tarefas do backend usando essa função
        if (!selectedProjectId) {
            setEventos([]);
            setEventosFiltrados([]);
            setLoading(false);
            setMensagemVazia('Selecione um projeto para ver as tarefas no calendário.');
            return;
        }

        setLoading(true);
        try {
            const response = await authFetch(
                `http://localhost:8080/tarefa/por-projeto/${selectedProjectId}`
            );
            
            if (!response.ok) {
                throw new Error('Erro ao carregar tarefas');
            }

            const data: any[] = await response.json();

            if (data.length === 0) {
                setMensagemVazia('Não há tarefas disponíveis para exibir no calendário.');
                setEventos([]);
                setEventosFiltrados([]);
                setResponsaveis([]);
                setLoading(false);
                return;
            }

            const tarefasConvertidas: Tarefa[] = data.map((item) => ({
                tarId: item.tarId,
                tarTitulo: item.tarTitulo,
                tarStatus: item.tarStatus,
                usuNome: item.usuNome,
                usuId: item.usuId,
                tarPrazo: item.tarPrazo,
                tarPrioridade: item.tarPrioridade,
                tarDescricao: item.tarDescricao,
                projId: item.projId,
                tarAnexo: null,
            }));

            const eventosConvertidos = converterTarefasParaEventos(tarefasConvertidas);
            setEventos(eventosConvertidos);
            setEventosFiltrados(eventosConvertidos);
            
            const responsaveisUnicos = [...new Set(tarefasConvertidas.map(t => t.usuNome))];
            setResponsaveis(responsaveisUnicos);
            
            setMensagemVazia('');
            setLoading(false);
            
        } catch (error) {
            console.error('Erro ao carregar tarefas:', error);
            setMensagemVazia('Erro ao carregar tarefas. Tente novamente.');
            setLoading(false);
        }
    }, [selectedProjectId]);

    useEffect(() => {
        carregarTarefas();
    }, [carregarTarefas]);

    useEffect(() => {
        if (responsavelSelecionado === '') {
            setEventosFiltrados(eventos);
        } else {
            const filtrados = eventos.filter(evento =>
                evento.resource.responsavel === responsavelSelecionado
            );
            setEventosFiltrados(filtrados);
        }
    }, [responsavelSelecionado, eventos]);

    const converterTarefasParaEventos = (tarefas: Tarefa[]): EventoTarefa[] => {
        return tarefas.map(tarefa => {
            const dataVencimento = new Date(tarefa.tarPrazo + 'T00:00:00');
            
            return {
                id: tarefa.tarId,
                title: tarefa.tarTitulo,
                start: dataVencimento,
                end: new Date(dataVencimento.getTime() + (23 * 60 * 60 * 1000)),
                resource: {
                    status: tarefa.tarStatus,
                    prioridade: tarefa.tarPrioridade,
                    responsavel: tarefa.usuNome,
                    tarefaCompleta: tarefa
                }
            };
        });
    };

    const getCorPorStatus = (status: string): string => {
        const cores = {
            'PENDENTE': '#f59e0b',
            'EM DESENVOLVIMENTO': '#3b82f6',
            'CONCLUÍDA': '#10b981',
            'OUTROS': '#764b9c'
        } as const;
        
        const statusNormalizado = status.trim().toUpperCase();
        
        return cores[statusNormalizado as keyof typeof cores] || '#764b9c';
    };

    const eventStyleGetter = (event: EventoTarefa) => {
        const cor = getCorPorStatus(event.resource.status);
        
        return {
            style: {
                backgroundColor: cor,
                borderRadius: '6px',
                color: 'white',
                border: 'none',
                fontSize: '12px',
                fontWeight: 'bold' as const,
                padding: '0 6px',
                height: '28px',
                lineHeight: 'normal',
                display: 'flex' as const,
                alignItems: 'center' as const,
                justifyContent: 'center' as const,
                textAlign: 'center' as const,
                width: '100%',
                margin: '0',
                boxSizing: 'border-box' as const
            }
        };
    };

    const handleSelectEvent = (event: EventoTarefa, e: any) => {
        e.preventDefault();
        
        setModalPosition({
            x: e.clientX,
            y: e.clientY
        });
        
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
        
        if (modalContext) {
            modalContext.closeModal();
        }
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

        try {
            
            const response = await authFetch(
                `http://localhost:8080/tarefa/apagar/${tarefaParaExcluir.tarId}`,
                {
                    method: 'DELETE'
                }
            );

            if (response.ok) {
                setEventos(eventos.filter(evento => evento.id !== tarefaParaExcluir.tarId));
                setEventosFiltrados(eventosFiltrados.filter(evento => evento.id !== tarefaParaExcluir.tarId));
                
                window.dispatchEvent(new CustomEvent('taskUpdated', { 
                    detail: { 
                        action: 'deleted', 
                        projectId: selectedProjectId,
                        taskId: tarefaParaExcluir.tarId,
                        timestamp: Date.now()
                    } 
                }));
                
                console.log(' Tarefa excluída com sucesso!');
            } else {
                const errorText = await response.text();
                console.error('Erro na resposta:', response.status, errorText);
                throw new Error(`Erro ${response.status}: ${errorText}`);
            }
        } catch (error) {
            console.error(' Erro ao apagar tarefa:', error);
            alert('Erro ao excluir tarefa. Verifique o console para mais detalhes.');
        } finally {
            setTarefaParaExcluir(null);
        }
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
        <div className="space-y-2 pb-20 sm:pb-56">
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
                        onMouseDown={(e) => {
                            e.stopPropagation();
                        }}
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
                        onMouseDown={(e) => {
                            e.stopPropagation();
                        }}
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
                    <div className="flex flex-col gap-4 lg:flex-row lg:justify-between lg:items-center lg:gap-4">
                        
                        <div className="flex gap-2 justify-center sm:justify-start">
                            <button
                                onClick={() => setView('month')}
                                className={`px-3 py-2 sm:px-4 sm:py-2 rounded-lg font-semibold transition-colors text-sm ${
                                    view === 'month' 
                                        ? 'bg-blue-600 text-white' 
                                        : 'bg-gray-100 text-gray-700 hover:bg-gray-200 active:bg-gray-300'
                                }`}
                            >
                                <i className="fa-solid fa-calendar mr-1 sm:mr-2"></i>
                                <span className="hidden xs:inline">Mensal</span>
                                <span className="xs:hidden">Mês</span>
                            </button>
                            <button
                                onClick={() => setView('week')}
                                className={`px-3 py-2 sm:px-4 sm:py-2 rounded-lg font-semibold transition-colors text-sm ${
                                    view === 'week' 
                                        ? 'bg-blue-600 text-white' 
                                        : 'bg-gray-100 text-gray-700 hover:bg-gray-200 active:bg-gray-300'
                                }`}
                            >
                                <i className="fa-solid fa-calendar-week mr-1 sm:mr-2"></i>
                                <span className="hidden xs:inline">Semanal</span>
                                <span className="xs:hidden">Sem</span>
                            </button>
                            {!isGoogleLogged && (
                                <button
                                    onClick={() => setShowGoogleModal(true)}
                                    className="flex items-center gap-2 bg-red-500 hover:bg-red-600 text-white font-semibold px-3 py-1.5 rounded-lg transition-colors shadow ml-2"
                                >
                                    <i className="fa-brands fa-google"></i>
                                    <span className="hidden xs:inline">Sincronizar Google</span>
                                    <span className="xs:hidden">Sincronizar Google</span>
                                </button>
                            )}
                        </div>

                        <div className="flex gap-2 items-center justify-between sm:gap-3">
                            <div className="flex items-center gap-2 flex-1 min-w-0">
                                <label className="text-sm font-medium text-gray-700">
                                    <i className="fa-solid fa-user mr-1"></i>
                                    <span className="hidden sm:inline">Responsável:</span>
                                </label>
                                <select
                                    value={responsavelSelecionado}
                                    onChange={(e) => setResponsavelSelecionado(e.target.value)}
                                    className="flex-1 min-w-0 px-2 py-1.5 border border-gray-300 rounded-md text-xs focus:outline-none focus:ring-2 focus:ring-blue-500 sm:px-3 sm:py-2 sm:text-sm sm:rounded-lg"
                                >
                                    <option value="">Todos</option>
                                    {responsaveis.map((responsavel) => (
                                        <option key={responsavel} value={responsavel}>
                                            {responsavel}
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <button
                                onClick={carregarTarefas}
                                className="px-2.5 py-1.5 bg-green-100 hover:bg-green-200 active:bg-green-300 text-green-700 rounded-md font-medium transition-colors text-xs whitespace-nowrap sm:px-3 sm:py-2 sm:text-sm sm:rounded-lg"
                            >
                                <i className="fa-solid fa-refresh mr-1 sm:mr-1"></i>
                                <span className="hidden sm:inline">Atualizar</span>
                                <span className="sm:hidden">Atualizar</span>
                            </button>
                        </div>
                    </div>
                </div>

                <div className="h-96 sm:h-[500px] lg:h-[650px]">
                    <Calendar
                        localizer={localizer}
                        events={eventosFiltrados}
                        startAccessor="start"
                        endAccessor="end"
                        style={{ height: '100%' }}
                        view={view}
                        date={currentDate}
                        onNavigate={setCurrentDate}
                        views={[Views.MONTH, Views.WEEK]}
                        eventPropGetter={eventStyleGetter}
                        onSelectEvent={handleSelectEvent}
                        onSelectSlot={handleSelectSlot}
                        selectable={true}
                        popup={true}
                        popupOffset={5}
                        step={1440}
                        timeslots={1}
                        showMultiDayTimes={false}
                        dayPropGetter={() => ({})}
                        messages={{
                            next: 'Próximo',
                            previous: 'Anterior',
                            today: 'Hoje',
                            month: 'Mês',
                            week: 'Semana',
                            day: 'Dia',
                            agenda: 'Agenda',
                            noEventsInRange: 'Não há tarefas neste período',
                            showMore: (total) => `+${total} mais`,
                            date: 'Data',
                            time: 'Horário',
                            event: 'Evento',
                            allDay: 'Dia inteiro',
                            work_week: 'Semana de trabalho',
                            yesterday: 'Ontem',
                            tomorrow: 'Amanhã'
                        }}
                        formats={{
                            timeGutterFormat: () => '',
                            eventTimeRangeFormat: () => '',
                            dayFormat: 'DD',
                            dayHeaderFormat: (date) => moment(date).format('dddd, DD/MM'),
                            dayRangeHeaderFormat: ({ start, end }) =>
                                moment(start).format('DD [de] MMMM YYYY') + ' - ' + moment(end).format('DD [de] MMMM YYYY'),
                            monthHeaderFormat: (date) => moment(date).format('MMMM YYYY'),
                            
                            weekdayFormat: (date) =>
                                isMobile
                                    ? moment(date).format('ddd')
                                    : moment(date).format('dddd')
                        }}
                        components={{
                            toolbar: (props) => (
                                <div className="flex flex-col gap-3 mb-4 pb-3 border-b sm:flex-row sm:justify-between sm:items-center sm:gap-0">
                                    <div className="flex gap-2 justify-center sm:justify-start order-2 sm:order-1">
                                        <button
                                            onClick={() => props.onNavigate('PREV')}
                                            className="px-2 py-1 bg-white-100 hover:bg-gray-200 active:bg-gray-300 rounded text-gray-700 sm:px-3"
                                        >
                                            <i className="fa-solid fa-chevron-left"></i>
                                        </button>
                                        <button
                                            onClick={() => props.onNavigate('TODAY')}
                                            className="px-3 py-1 bg-blue-100 hover:bg-blue-200 active:bg-blue-300 rounded text-blue-700 font-medium text-xs sm:text-sm"
                                        >
                                            Hoje
                                        </button>
                                        <button
                                            onClick={() => props.onNavigate('NEXT')}
                                            className="px-2 py-1 bg-white-100 hover:bg-gray-200 active:bg-gray-300 rounded text-gray-700 sm:px-3"
                                        >
                                            <i className="fa-solid fa-chevron-right"></i>
                                        </button>
                                    </div>
                                    
                                    <div className="flex items-center justify-center gap-2 relative w-full order-1 sm:order-2">
                                      <h2 className="text-lg font-bold text-gray-800 text-center sm:text-xl">
                                        {moment(props.date).format(view === 'month' ? 'MMMM YYYY' : 'DD [de] MMMM YYYY')}
                                      </h2>
                                    </div>
                                    
                                    <div className="text-xs text-gray-600 text-center order-3 sm:text-sm">
                                        <span className="hidden sm:inline">
                                            {eventosFiltrados.length} tarefa{eventosFiltrados.length !== 1 ? 's' : ''}
                                        </span>
                                        <span className="sm:hidden">
                                            {eventosFiltrados.length}
                                        </span>
                                    </div>
                                </div>
                            ),
                            week: {
                                header: ({ date }) => (
                                    <div className="text-center py-1 font-medium text-xs sm:py-2 sm:text-sm">
                                        {isMobile
                                            ? moment(date).format('ddd')
                                            : moment(date).format('dddd DD')}
                                    </div>
                                )
                            },
                            timeGutterHeader: () => null
                        }}
                    />
                </div>
            </div>

            <div className="bg-white rounded-lg shadow-md p-3 sm:p-4">
                <div className="grid grid-cols-4 gap-1 sm:hidden">
                    <div className="flex flex-col items-center gap-1">
                        <div className="w-2.5 h-2.5 bg-yellow-500 rounded"></div>
                        <span className="text-xs text-gray-700 font-medium leading-tight">Pendente</span>
                    </div>
                    <div className="flex flex-col items-center gap-1">
                        <div className="w-2.5 h-2.5 bg-blue-500 rounded"></div>
                        <span className="text-xs text-gray-700 font-medium leading-tight">Em Desenv.</span>
                    </div>
                    <div className="flex flex-col items-center gap-1">
                        <div className="w-2.5 h-2.5 bg-green-500 rounded"></div>
                        <span className="text-xs text-gray-700 font-medium leading-tight">Concluída</span>
                    </div>
                    <div className="flex flex-col items-center gap-1">
                        <div className="w-2.5 h-2.5 rounded" style={{ backgroundColor: '#764B9C' }}></div>
                        <span className="text-xs text-gray-700 font-medium leading-tight">Outros</span>
                    </div>
                </div>
                
                <div className="hidden sm:grid grid-cols-2 gap-6 justify-items-center md:grid-cols-4">
                    <div className="flex items-center justify-center gap-3">
                        <div className="w-4 h-4 bg-yellow-500 rounded"></div>
                        <span className="text-sm text-gray-700">Pendente</span>
                    </div>
                    <div className="flex items-center justify-center gap-3">
                        <div className="w-4 h-4 bg-blue-500 rounded"></div>
                        <span className="text-sm text-gray-700">Em Desenvolvimento</span>
                    </div>
                    <div className="flex items-center justify-center gap-3">
                        <div className="w-4 h-4 bg-green-500 rounded"></div>
                        <span className="text-sm text-gray-700">Concluída</span>
                    </div>
                    <div className="flex items-center justify-center gap-3">
                        <div className="w-4 h-4 rounded" style={{ backgroundColor: '#764B9C' }}></div>
                        <span className="text-sm text-gray-700">Outros</span>
                    </div>
                </div>
            </div>

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
                onLogin={(_usuEmail, _usuSenha) => {
                    //aqui vai a logica do login com o google, mas como ainda não foi implementado, as variaveis usuEmail e usuSenha nao estao sendo usadas, eu adicionei esse comentario porque ele faz parar de dar erro
                    setShowGoogleModal(false);
                    setIsGoogleLogged(true);
                }}
            />
        </div>
    );
};

export default CalendarioTarefas;