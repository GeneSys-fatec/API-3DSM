import React from 'react';

type NovoProjeto = {
  nome: string;
}

type ModalProps = {
  isOpen: boolean;
  onClose: () => void;
  onAddProject: (projeto: NovoProjeto) => void;
};

type ModalState = {
  projectName: string;
  description: string;
  team: string;
};

export default class ModalProjetos extends React.Component<ModalProps, ModalState> {
  state: ModalState = {
    projectName: '',
    description: '',
    team: 'Select Team',
  };

  handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = event.target;
    this.setState({ [name]: value } as Pick<ModalState, keyof ModalState>);
  };

  handleSubmit = (event: React.FormEvent) => {
    event.preventDefault(); 

    if (!this.state.projectName.trim()) {
      alert('Por favor, insira o nome do projeto.');
      return;
    }

    const newProject: NovoProjeto = {
      nome: this.state.projectName,
    };

    this.props.onAddProject(newProject);

    this.setState({
      projectName: '',
      description: '',
      team: 'Select Team',
    });
  };

  render() {
    const { isOpen, onClose } = this.props;

    if (!isOpen) {
      return null;
    }

    return (
      <div className="fixed inset-0 bg-gray-600/60 flex items-center justify-center z-50 p-4 font-sans">
        <div className="bg-white rounded-lg shadow-2xl w-full max-w-3xl p-6 flex flex-col gap-4">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-gray-800">Add Project</h2>
            <button onClick={onClose} className="text-gray-400 hover:text-gray-600 text-3xl transition-colors duration-200">
              &times;
            </button>
          </div>

          {/* Adicionado o manipulador de envio ao formulário */}
          <form onSubmit={this.handleSubmit} className='flex flex-col gap-y-5'>
            <div>
              <label htmlFor="Nome do Projeto" className="block text-sm font-medium text-gray-700 mb-1">Nome do Projeto</label>
              <input
                type="text"
                id="Nome do Projeto"
                name="Nome do Projeto"
                placeholder="Nome do Projeto"
                value={this.state.projectName}
                onChange={this.handleChange}
                className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
              />
            </div>

            <div>
              <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">Descrição</label>
              <textarea
                id="description"
                name="description"
                rows={3}
                placeholder="Description"
                value={this.state.description}
                onChange={this.handleChange}
                className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
              ></textarea>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Datas</label>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <input
                  type="date"
                  aria-label="Data Inicio"
                  className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2 text-gray-500"
                />
                <input
                  type="date"
                  aria-label="Data Entrega"
                  className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2 text-gray-500"
                />
              </div>
            </div>

            <div>
              <label htmlFor="team" className="block text-sm font-medium text-gray-700 mb-1">Team</label>
              <select
                id="team"
                name="team"
                value={this.state.team}
                onChange={this.handleChange}
                className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
              >
                <option>Selecione o Time</option>
                <option>GSW</option>
                <option>GeneSys</option>
                <option>Fatec</option>
              </select>
            </div>

            <div className="flex justify-center mt-6">
              <button
                type="submit"
                className="w-full px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors duration-200"
              >
                Criar projeto
              </button>
            </div>
          </form>
        </div>
      </div>
    );
  }
}

