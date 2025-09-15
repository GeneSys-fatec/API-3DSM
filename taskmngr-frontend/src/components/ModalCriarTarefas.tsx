import React from 'react';

type ModalProps = {
  isOpen: boolean;
  onClose: () => void;
};

export default class ModalCriarTarefas extends React.Component<ModalProps> {
  render() {
    const { isOpen, onClose } = this.props;

    if (!isOpen) {
      return null;
    }

    return (
      <div className="fixed inset-0 bg-gray-600/60 flex items-center justify-center z-50 p-4">
        <div className="bg-white rounded-lg shadow-2xl w-full max-w-xl p-8 flex flex-col gap-4">
          <div className="flex justify-between items-center mb-8">
            <h2 className="text-2xl font-bold text-gray-800">Adicionar Nova Tarefa</h2>
            <button onClick={onClose} className="text-gray-400 hover:text-gray-600 text-3xl transition-colors duration-200">
              &times;
            </button>
          </div>

          <form className='flex flex-col gap-y-6'>
            <div>
              <label htmlFor="titulo" className="py-2 block text-sm font-medium text-gray-700">Título da Tarefa</label>
              <input
                type="text"
                id="titulo"
                placeholder="Título da Tarefa"
                className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
              />
            </div>
            <div className="flex items-center gap-4">
              <div>
                <select
                  id="tabela"
                  className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
                >
                  <option>Pendente</option>
                  <option>Em Desenvolvimento</option>
                  <option>Concluída</option>
                </select>
              </div>
              <div>
                <label htmlFor="file-upload" className="cursor-pointer bg-white p-3 border border-gray-300 rounded-md shadow-sm text-sm leading-4 font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 flex items-center gap-2">
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 text-gray-600" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
                    <path strokeLinecap="round" strokeLinejoin="round" d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13" />
                  </svg>
                  <span>Anexo</span>
                  <input id="file-upload" name="file-upload" type="file" className="sr-only" />
                </label>
              </div>
            </div>
            <div>
              <label htmlFor="descricao" className="block text-sm font-medium text-gray-700">Descrição</label>
              <textarea
                id="descricao"
                rows={3}
                placeholder="Adicione uma breve descrição da tarefa."
                className="mt-2 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 min-h-24 p-2"
              ></textarea>
            </div>
            <div className="bg-white border  mt-2 flex flex-col gap-6 p-4">
              <h3 className="text-lg font-semibold text-gray-800">Detalhes</h3>
              <hr />
              <div className="grid grid-cols-1 gap-6">
                <div className="flex flex-col gap-2">
                  <label className="block text-sm font-medium text-gray-700">Responsável</label>
                  <select className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2">
                    <option>Selecione um membro</option>
                    <option>Matheus</option>
                    <option>Ana Júlia</option>
                  </select>
                </div>
                <div className="flex flex-col gap-2">
                  <label className="block text-sm font-medium text-gray-700">Prioridade</label>
                  <select className="block w-full rounded-md border-gray-400 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2">
                    <option>Alta</option>
                    <option>Média</option>
                    <option>Baixa</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Data de entrega</label>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <input type="date" aria-label="Planned Start" className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2 text-gray-500"/>
                  </div>
                </div>
              </div>
            </div>
            <div className="flex justify-end mt-8 gap-x-9">
              <button
                type="button"
                onClick={onClose}
                className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-500 transition-colors duration-200"
              >
                Cancelar
              </button>
              <button
                type="submit"
                className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors duration-200"
              >
                Criar
              </button>
            </div>
          </form>
        </div>
      </div>
    );
  }
}

