import React from 'react';

// Define o tipo de dados para um novo projeto que será criado
type NovoProjeto = {
  nome: string;
  // Se precisar enviar mais dados, adicione os campos aqui.
  // Ex: description: string;
}

// Atualiza as props para incluir a função que o componente Home irá passar
type ModalProps = {
  isOpen: boolean;
  onClose: () => void;
  onAddProject: (projeto: NovoProjeto) => void;
};

// Define o tipo para o estado interno que controlará os campos do formulário
type ModalState = {
  projectName: string;
  description: string;
  team: string;
};

export default class ModalProjetos extends React.Component<ModalProps, ModalState> {
  // Inicializa o estado com valores vazios para os campos
  state: ModalState = {
    projectName: '',
    description: '',
    team: 'Select Team', // Valor inicial para o select
  };

  // Esta função é chamada toda vez que o usuário digita em um campo
  handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = event.target;
    // Atualiza a parte correspondente do estado (ex: 'projectName', 'description')
    this.setState({ [name]: value } as Pick<ModalState, keyof ModalState>);
  };

  // Esta função é chamada quando o botão "Create Project" é clicado
  handleSubmit = (event: React.FormEvent) => {
    event.preventDefault(); // Impede que a página recarregue

    // Validação simples: verifica se o nome do projeto não está vazio
    if (!this.state.projectName.trim()) {
      alert('Por favor, insira o nome do projeto.'); // Em um app real, usaríamos um componente de notificação
      return;
    }

    // Cria um objeto com os dados do formulário
    const newProject: NovoProjeto = {
      nome: this.state.projectName,
    };

    // Chama a função `onAddProject` que foi passada pelo componente pai (Home)
    this.props.onAddProject(newProject);

    // Limpa o formulário para a próxima vez que for aberto
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
              <label htmlFor="projectName" className="block text-sm font-medium text-gray-700 mb-1">Project Name</label>
              <input
                type="text"
                id="projectName"
                name="projectName" // 'name' é crucial para o handleChange
                placeholder="Project Name"
                value={this.state.projectName} // Conecta o campo ao estado
                onChange={this.handleChange} // Conecta a digitação à função
                className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2"
              />
            </div>

            <div>
              <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">Description</label>
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
              <label className="block text-sm font-medium text-gray-700 mb-1">Dates</label>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <input
                  type="date"
                  aria-label="Planned Start"
                  className="block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 p-2 text-gray-500"
                />
                <input
                  type="date"
                  aria-label="Planned Due"
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
                <option>Select Team</option>
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
                Create Project
              </button>
            </div>
          </form>
        </div>
      </div>
    );
  }
}

